package fi.nutrifier.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import fi.nutrifier.models.database.DailySummary
import fi.nutrifier.models.database.Food
import fi.nutrifier.models.database.FoodEntryFood
import fi.nutrifier.models.database.FoodEntry
import fi.nutrifier.models.database.FoodEntryRequest
import fi.nutrifier.models.database.NutrientSummary
import fi.nutrifier.repositories.database.DailySummaryRepository
import fi.nutrifier.repositories.database.FineliRepository
import fi.nutrifier.repositories.database.FoodRepository
import fi.nutrifier.repositories.database.FoodEntryRepository
import fi.nutrifier.repositories.database.SelectedDateRepository
import fi.nutrifier.utils.AlertType
import fi.nutrifier.utils.ConversionUtils
import fi.nutrifier.utils.ConversionUtils.emptyFood
import fi.nutrifier.utils.Enums
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class FoodEntryViewModel(
    private val repository: FoodEntryRepository,
    encryptedSharedPreferences: SharedPreferences,
): BaseViewModel(encryptedSharedPreferences) {
    private val foodRepository = FoodRepository(this.encryptedSharedPreferences)
    private val dailySummaryRepository = DailySummaryRepository(this.encryptedSharedPreferences)
    private val fineliRepository = FineliRepository()
    private val selectedDateRepository = SelectedDateRepository()

    private var _entries: MutableState<List<FoodEntryFood>> = mutableStateOf(emptyList())

    private var _breakfastEntries: MutableState<List<FoodEntryFood>> = mutableStateOf(emptyList())
    val breakfastEntries get() = _breakfastEntries.value

    private var _lunchEntries: MutableState<List<FoodEntryFood>> = mutableStateOf(emptyList())
    val lunchEntries get() = _lunchEntries.value

    private var _dinnerEntries: MutableState<List<FoodEntryFood>> = mutableStateOf(emptyList())
    val dinnerEntries get() = _dinnerEntries.value

    private var _snacksEntries: MutableState<List<FoodEntryFood>> = mutableStateOf(emptyList())
    val snacksEntries get() = _snacksEntries.value

    val selectedDate = selectedDateRepository.selectedDate
    val setSelectedDate: (LocalDate) -> Unit = {
        selectedDateRepository.setSelectedDate(it)
        loadFoodEntries()
    }

    private val emptyNutrientSummary = NutrientSummary(
        energy = Enums.EnergyUnit.entries.associateWith { 0.0 },
        fats = Enums.MacroWeightUnit.entries.associateWith { 0.0 },
        carbs = Enums.MacroWeightUnit.entries.associateWith { 0.0 },
        protein = Enums.MacroWeightUnit.entries.associateWith { 0.0 },
    )

    private var _summary: MutableState<DailySummary?> = mutableStateOf(null)
    val summary get() = _summary.value

    private var _mealNutrients = mutableStateOf<Map<Enums.MealType, NutrientSummary>>(emptyMap())
    val nutrients get() = _mealNutrients.value
    val setNutrients: (Map<Enums.MealType, NutrientSummary>) -> Unit = { _mealNutrients.value = it }

    suspend fun fetchFoodById(id: String): Food?  {
        val result = foodRepository.getFoodById(id)
        Log.d("FoodEntryViewModel", "fetchFoodById $result")
        return if (result.isSuccessful()) result.value else null
    }

    private var _selectedMeal = mutableStateOf<Enums.MealType?>(null)
    val selectedMeal get() = _selectedMeal.value
    val setSelectedMeal: (Enums.MealType?) -> Unit = { _selectedMeal.value = it }

    private var _selectedFoodEntry = mutableStateOf<FoodEntry?>(null)
    val selectedFoodEntry get() = _selectedFoodEntry.value
    val setSelectedFoodEntry: (FoodEntry?) -> Unit = { _selectedFoodEntry.value = it }

    private var _currentAmount = mutableStateOf(100.0)
    val currentAmount get() = _currentAmount.value

    fun setCurrentAmount(value: Double) {
        _currentAmount.value = value
    }

    private fun calculateNutrientsForEntries(entries: List<FoodEntry>): NutrientSummary {
        var calories = 0.0
        var fats = 0.0
        var carbs = 0.0
        var protein = 0.0

        entries.forEach { entry ->
            val multiplier = entry.amount / 100

            calories += entry.caloriesSnapshot.times(multiplier)
            fats += entry.fatSnapshot.times(multiplier)
            carbs += entry.carbsSnapshot.times(multiplier)
            protein += entry.proteinSnapshot.times(multiplier)
        }

        val result = NutrientSummary(
            energy = Enums.EnergyUnit.entries.associateWith { energyUnit ->
                ConversionUtils.convertEnergy(calories, energyUnit)
            },
            fats = Enums.MacroWeightUnit.entries.associateWith { weightUnit ->
                ConversionUtils.convertMacroWeight(fats, weightUnit)
            },
            carbs = Enums.MacroWeightUnit.entries.associateWith { weightUnit ->
                ConversionUtils.convertMacroWeight(carbs, weightUnit)
            },
            protein = Enums.MacroWeightUnit.entries.associateWith { weightUnit ->
                ConversionUtils.convertMacroWeight(protein, weightUnit)
            },
        )
        return result
    }

    private fun <K> sumMaps(maps: List<Map<K, Double>>): Map<K, Double> {
        return maps.flatMap { it.entries }.groupBy { it.key }.mapValues { (_, entries) ->
            entries.sumOf { it.value }
        }
    }

    private fun combineNutrientSummaries(summaries: List<NutrientSummary>): NutrientSummary {
        return NutrientSummary(
            energy = sumMaps(summaries.map { it.energy }),
            fats = sumMaps(summaries.map { it.fats }),
            carbs = sumMaps(summaries.map { it.carbs }),
            protein = sumMaps(summaries.map { it.protein }),
        )
    }

    suspend fun fetchFineliFoods(ids: List<Int>): List<Food> {
        val result = mutableListOf<Food>()

        ids.forEach {
            val singleResult = fineliRepository.getFoodById(it)
            if (singleResult.isSuccessful() && singleResult.value != null) {
                result.add(singleResult.value.toFood())
            }
        }

        return result
    }

    fun loadFoodEntries(triggerLoading: Boolean = true) {
        Log.d("FoodEntryViewModel", "Loading food entries...")

        if (triggerLoading) setLoading(true)

        viewModelScope.launch(Dispatchers.IO) {

            val summaryResult = dailySummaryRepository.getByDate(selectedDate.value)
            Log.d("FoodEntryViewModel", "Got summary: ${summaryResult.value}")
            Log.d("FoodEntryViewModel", "summaryResult.isSuccessful(: ${summaryResult.isSuccessful()} summaryResult.value != null: ${summaryResult.value != null}")
            if (summaryResult.isSuccessful() && summaryResult.value != null) {
                Log.d("FoodEntryViewModel", "Setting new summary: ${summaryResult.value}")
                _summary.value = summaryResult.value
            } else {
                _summary.value = null
            }

            val result = repository.getFoodEntriesByDate(selectedDate.value)
            val entries: List<FoodEntry> = result.value.orEmpty()

            Log.d("FoodEntryViewModel", "Got entries: $entries")

            if (!result.isSuccessful()) {
                showAlert("Error occured in loading food entries (${result.errorCode}")
                if (triggerLoading) setLoading(false)
                return@launch
            }

            val (fineliEntries, dbEntries) = entries.partition { it.fineliId != null }

            val fineliFoods = fetchFineliFoods(fineliEntries.mapNotNull { it.fineliId })

            suspend fun FoodEntry.toFoodEntryFood(): FoodEntryFood {
                val food = when {
                    this.fineliId != null -> fineliFoods.find { it.fineliId == this.fineliId } ?: emptyFood
                    else -> fetchFoodById(this.foodId) ?: emptyFood
                }
                return FoodEntryFood(foodEntry = this, food = food)
            }

            val entriesByMeal = entries.groupBy { it.mealType }

            val dbNutrientsByMeal = Enums.MealType.entries.associateWith { mealType ->
                val mealEntries = entriesByMeal[mealType]?.filter { it.fineliId == null }.orEmpty()
                calculateNutrientsForEntries(mealEntries)
            }

            val fineliNutrientsByMeal = Enums.MealType.entries.associateWith { mealType ->
                val mealEntries = entriesByMeal[mealType]?.filter { it.fineliId != null }.orEmpty()
                calculateNutrientsForEntries(mealEntries)
            }

            val mealNutrients = Enums.MealType.entries.associateWith { mealType ->
                val listOfNutrientsByMeal = listOf(
                    dbNutrientsByMeal[mealType] ?: emptyNutrientSummary,
                    fineliNutrientsByMeal[mealType] ?: emptyNutrientSummary,
                )
                combineNutrientSummaries(listOfNutrientsByMeal)
            }

            Log.d("FoodEntryViewModel", "Calculated meal nutrients: $mealNutrients")

            setNutrients(mealNutrients)

            _entries.value = entries.map { it.toFoodEntryFood() }
            _breakfastEntries.value = entriesByMeal[Enums.MealType.BREAKFAST]?.map { it.toFoodEntryFood() } ?: emptyList()
            _lunchEntries.value = entriesByMeal[Enums.MealType.LUNCH]?.map { it.toFoodEntryFood() } ?: emptyList()
            _dinnerEntries.value = entriesByMeal[Enums.MealType.DINNER]?.map { it.toFoodEntryFood() } ?: emptyList()
            _snacksEntries.value = entriesByMeal[Enums.MealType.SNACKS]?.map { it.toFoodEntryFood() } ?: emptyList()

        }

        if (triggerLoading) setLoading(false)
    }

    fun updateFoodEntry(foodEntry: FoodEntry) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.updateFoodEntry(foodEntry)
            if (result.isSuccessful()) {
                /*_entries.value = _entries.value.map {
                    if (it.foodEntry.id == foodEntry.id) foodEntry
                    else it
                }*/
                loadFoodEntries(false)
                showAlert("Log updated!", AlertType.INFO)
            } else {
                showAlert("Error occurred while updating log (${result.errorCode}).")
            }
        }
    }

    fun saveFoodEntry(foodEntryRequest: FoodEntryRequest) {
        setLoading(true)

        viewModelScope.launch(Dispatchers.IO) {
            Log.d("FoodEntryViewModel", "Saving new entry: $foodEntryRequest")

            val result = repository.saveFoodEntry(foodEntryRequest)
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
                _entries.value = _entries.value.filter { id != it.foodEntry.id }
                loadFoodEntries(false)
                showAlert("Log deleted!", AlertType.INFO)
            } else {
                showAlert("Error occurred while deleting the log (${result.errorCode}).")
                setLoading(false)
            }
        }
    }

    fun confirmDay(triggerLoading: Boolean = true) {
        Log.d("FoodEntryViewModel", "Confirming date...")
        if (triggerLoading) setLoading(true)

        viewModelScope.launch(Dispatchers.IO) {

            val summaryResult = dailySummaryRepository.confirmDay(selectedDate.value)
            if (summaryResult.isSuccessful() && summaryResult.value != null) {
                Log.d("FoodEntryViewModel", "New summary after confirm: ${summaryResult.value}")
                if (_summary.value != null) {
                    _summary.value = _summary.value!!.copy(confirmed = true)
                }
            }
        }

        if (triggerLoading) setLoading(false)
    }
}