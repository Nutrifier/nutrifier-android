package fi.nutrifier.viewmodels

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import fi.nutrifier.models.database.Food
import fi.nutrifier.models.database.FoodLog
import fi.nutrifier.models.database.Log
import fi.nutrifier.models.database.MealType
import fi.nutrifier.models.database.NutrientSummary
import fi.nutrifier.repositories.database.FineliRepository
import fi.nutrifier.repositories.database.FoodRepository
import fi.nutrifier.repositories.database.LogRepository
import fi.nutrifier.utils.AlertType
import fi.nutrifier.utils.ConversionUtils.emptyFood
import fi.nutrifier.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class LogsScreenViewModel(application: Application): BaseViewModel(application) {
    private val logRepository = LogRepository(this.encryptedSharedPreferences)
    private val foodRepository = FoodRepository(this.encryptedSharedPreferences)
    private val fineliRepository = FineliRepository()

    private var _logs: MutableState<List<Log>> = mutableStateOf(emptyList())
    private var _breakfastLogs: MutableState<List<FoodLog>> = mutableStateOf(emptyList())
    val breakfastLogs get() = _breakfastLogs.value
    private var _lunchLogs: MutableState<List<FoodLog>> = mutableStateOf(emptyList())
    val lunchLogs get() = _lunchLogs.value
    private var _dinnerLogs: MutableState<List<FoodLog>> = mutableStateOf(emptyList())
    val dinnerLogs get() = _dinnerLogs.value
    private var _snacksLogs: MutableState<List<FoodLog>> = mutableStateOf(emptyList())
    val snacksLogs get() = _snacksLogs.value

    // NOTE: Can be exploited by changing the devices date
    private var _date: MutableState<LocalDate> = mutableStateOf(LocalDate.now())
    val date get() = _date.value
    val setDate: (LocalDate) -> Unit = { _date.value = it; loadLogs() }

    private var _overallNutrients = mutableStateOf(NutrientSummary(0.0, 0.0, 0.0, 0.0))
    val overallNutrients get() = _overallNutrients.value
    val setOverallNutrients: (NutrientSummary) -> Unit = { _overallNutrients.value = it }

    private var _mealNutrients = mutableStateOf<Map<MealType, NutrientSummary>>(emptyMap())
    val nutrients get() = _mealNutrients.value
    val setNutrients: (Map<MealType, NutrientSummary>) -> Unit = { _mealNutrients.value = it }

    suspend fun fetchFoodById(id: String): Food?  {
        val result = foodRepository.getFoodById(id)
        return if (result.isSuccessful()) result.value else null
    }

    private var _selectedMeal = mutableStateOf<MealType?>(null)
    val selectedMeal get() = _selectedMeal.value
    val setSelectedMeal: (MealType?) -> Unit = { _selectedMeal.value = it }

    private var _selectedLog = mutableStateOf<Log?>(null)
    val selectedLog get() = _selectedLog.value
    val setSelectedLog: (Log?) -> Unit = { _selectedLog.value = it }

    private var _currentAmount = mutableStateOf("100")
    val currentAmount get() = _currentAmount.value
    val setCurrentAmount: (String) -> Unit = { _currentAmount.value = it }

    private suspend fun calculateNutrients(logs: List<Log>): NutrientSummary {
        var calories = 0.0
        var fats = 0.0
        var carbs = 0.0
        var protein = 0.0

        logs.forEach { log ->
            val food = fetchFoodById(log.foodId)
            food?.calories?.let { calories += it * (log.amount / 100) }
            food?.fat?.let { fats += it * (log.amount / 100) }
            food?.carbs?.let { carbs += it * (log.amount / 100) }
            food?.protein?.let { protein += it * (log.amount / 100) }
        }
        return NutrientSummary(calories, fats, carbs, protein)
    }

    fun loadLogs(triggerLoading: Boolean = true) {
        if (triggerLoading) setLoading(true)

        viewModelScope.launch(Dispatchers.IO) {
            val result: Result<List<Log>> = logRepository.getLogsByDate(_date.value)

            if (result.isSuccessful()) {
                _logs.value = result.value ?: emptyList()

                val nutrientSummary = result.value?.let { calculateNutrients(it) }
                if (nutrientSummary != null) setOverallNutrients(nutrientSummary)

                val newBreakfastLogs = result.value?.filter { it.meal == "BREAKFAST" } ?: emptyList()
                val newLunchLogs = result.value?.filter { it.meal == "LUNCH" } ?: emptyList()
                val newDinnerLogs = result.value?.filter { it.meal == "DINNER" } ?: emptyList()
                val newSnacksLogs = result.value?.filter {
                    it.meal == "SNACKS" || (it.meal != "BREAKFAST"
                            && it.meal != "LUNCH"
                            && it.meal != "DINNER")
                } ?: emptyList()

                _breakfastLogs.value = newBreakfastLogs.map {
                    FoodLog(it, fetchFoodById(it.foodId) ?: emptyFood.copy())
                }
                _lunchLogs.value = newLunchLogs.map {
                    FoodLog(it, fetchFoodById(it.foodId) ?: emptyFood.copy())
                }
                _dinnerLogs.value = newDinnerLogs.map {
                    FoodLog(it, fetchFoodById(it.foodId) ?: emptyFood.copy())
                }
                _snacksLogs.value = newSnacksLogs.map {
                    FoodLog(it, fetchFoodById(it.foodId) ?: emptyFood.copy())
                }

                val newMealNutrients = MealType.entries.associateWith { mealType ->
                    when (mealType) {
                        MealType.BREAKFAST -> calculateNutrients(newBreakfastLogs)
                        MealType.LUNCH -> calculateNutrients(newLunchLogs)
                        MealType.DINNER -> calculateNutrients(newDinnerLogs)
                        MealType.SNACKS -> calculateNutrients(newSnacksLogs)
                    }
                }
                setNutrients(newMealNutrients)
            } else {
                showAlert("Error occurred in loading logs (${result.errorCode}).")
            }
            if (triggerLoading) setLoading(false)
        }
    }

    fun updateLog(log: Log) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = logRepository.updateLog(log)
            if (result.isSuccessful()) {
                _logs.value = _logs.value.map {
                    if (it.id == log.id) log
                    else it
                }
                loadLogs(false)
                showAlert("Log updated!", AlertType.INFO)
            } else {
                showAlert("Error occurred while updating log (${result.errorCode}).")
            }
        }
    }

    fun saveLog(log: Log) {
        setLoading(true)

        viewModelScope.launch(Dispatchers.IO) {
            val result = logRepository.saveLog(log)
            if (result.isSuccessful()) {
                showAlert("Log saved!", AlertType.INFO)
                loadLogs()
            } else {
                showAlert("Error occurred while saving the log (${result.errorCode}).")
                setLoading(false)
            }

        }
    }

    fun deleteLog(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = logRepository.deleteLog(id)
            if (result.isSuccessful()) {
                _logs.value = _logs.value.filter { id != it.id }
                loadLogs(false)
                showAlert("Log deleted!", AlertType.INFO)
            } else {
                showAlert("Error occurred while deleting the log (${result.errorCode}).")
                setLoading(false)
            }
        }
    }
}