package fi.nutrifier.viewmodels

import android.app.Application
import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import fi.nutrifier.models.database.Food
import fi.nutrifier.models.database.FoodEntryFood
import fi.nutrifier.models.database.FoodEntry
import fi.nutrifier.models.database.MealType
import fi.nutrifier.models.database.NutrientSummary
import fi.nutrifier.repositories.database.AuthRepository
import fi.nutrifier.repositories.database.FoodRepository
import fi.nutrifier.repositories.database.FoodEntryRepository
import fi.nutrifier.utils.AlertType
import fi.nutrifier.utils.ConversionUtils.emptyFood
import fi.nutrifier.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class FoodEntryViewModel(
    private val repository: FoodEntryRepository,
    encryptedSharedPreferences: SharedPreferences,
): BaseViewModel(encryptedSharedPreferences) {
    private val foodRepository = FoodRepository(this.encryptedSharedPreferences)

    private var _entries: MutableState<List<FoodEntry>> = mutableStateOf(emptyList())
    private var _breakfastEntries: MutableState<List<FoodEntryFood>> = mutableStateOf(emptyList())
    val breakfastEntries get() = _breakfastEntries.value
    private var _lunchEntries: MutableState<List<FoodEntryFood>> = mutableStateOf(emptyList())
    val lunchEntries get() = _lunchEntries.value
    private var _dinnerEntries: MutableState<List<FoodEntryFood>> = mutableStateOf(emptyList())
    val dinnerEntries get() = _dinnerEntries.value
    private var _snacksEntries: MutableState<List<FoodEntryFood>> = mutableStateOf(emptyList())
    val snacksEntries get() = _snacksEntries.value

    // NOTE: Can be exploited by changing the devices date
    private var _date: MutableState<LocalDate> = mutableStateOf(LocalDate.now())
    val date get() = _date.value
    val setDate: (LocalDate) -> Unit = { _date.value = it; loadFoodEntries() }

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

    private var _selectedFoodEntry = mutableStateOf<FoodEntry?>(null)
    val selectedFoodEntry get() = _selectedFoodEntry.value
    val setSelectedFoodEntry: (FoodEntry?) -> Unit = { _selectedFoodEntry.value = it }

    private var _currentAmount = mutableStateOf("100")
    val currentAmount get() = _currentAmount.value
    val setCurrentAmount: (String) -> Unit = { _currentAmount.value = it }

    private suspend fun calculateNutrients(foodEntries: List<FoodEntry>): NutrientSummary {
        var calories = 0.0
        var fats = 0.0
        var carbs = 0.0
        var protein = 0.0

        foodEntries.forEach { log ->
            val food = fetchFoodById(log.foodId)
            food?.calories?.let { calories += it * (log.amount / 100) }
            food?.fat?.let { fats += it * (log.amount / 100) }
            food?.carbs?.let { carbs += it * (log.amount / 100) }
            food?.protein?.let { protein += it * (log.amount / 100) }
        }
        return NutrientSummary(calories, fats, carbs, protein)
    }

    fun loadFoodEntries(triggerLoading: Boolean = true) {
        if (triggerLoading) setLoading(true)

        viewModelScope.launch(Dispatchers.IO) {
            val result: Result<List<FoodEntry>> = repository.getFoodEntriesByDate(_date.value)

            if (result.isSuccessful()) {
                _entries.value = result.value ?: emptyList()

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

                _breakfastEntries.value = newBreakfastLogs.map {
                    FoodEntryFood(it, fetchFoodById(it.foodId) ?: emptyFood.copy())
                }
                _lunchEntries.value = newLunchLogs.map {
                    FoodEntryFood(it, fetchFoodById(it.foodId) ?: emptyFood.copy())
                }
                _dinnerEntries.value = newDinnerLogs.map {
                    FoodEntryFood(it, fetchFoodById(it.foodId) ?: emptyFood.copy())
                }
                _snacksEntries.value = newSnacksLogs.map {
                    FoodEntryFood(it, fetchFoodById(it.foodId) ?: emptyFood.copy())
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

    fun updateFoodEntry(foodEntry: FoodEntry) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.updateFoodEntry(foodEntry)
            if (result.isSuccessful()) {
                _entries.value = _entries.value.map {
                    if (it.id == foodEntry.id) foodEntry
                    else it
                }
                loadFoodEntries(false)
                showAlert("Log updated!", AlertType.INFO)
            } else {
                showAlert("Error occurred while updating log (${result.errorCode}).")
            }
        }
    }

    fun saveFoodEntry(foodEntry: FoodEntry) {
        setLoading(true)

        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.saveFoodEntry(foodEntry)
            if (result.isSuccessful()) {
                showAlert("Log saved!", AlertType.INFO)
                loadFoodEntries()
            } else {
                showAlert("Error occurred while saving the log (${result.errorCode}).")
                setLoading(false)
            }

        }
    }

    fun deleteFoodEntry(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.deleteFoodEntry(id)
            if (result.isSuccessful()) {
                _entries.value = _entries.value.filter { id != it.id }
                loadFoodEntries(false)
                showAlert("Log deleted!", AlertType.INFO)
            } else {
                showAlert("Error occurred while deleting the log (${result.errorCode}).")
                setLoading(false)
            }
        }
    }
}