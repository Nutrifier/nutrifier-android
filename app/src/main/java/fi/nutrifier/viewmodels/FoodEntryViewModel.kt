package fi.nutrifier.viewmodels

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import fi.nutrifier.models.database.FineliResponse
import fi.nutrifier.models.database.Food
import fi.nutrifier.models.database.FoodEntryFood
import fi.nutrifier.models.database.FoodEntry
import fi.nutrifier.models.database.MealType
import fi.nutrifier.models.database.NutrientSummary
import fi.nutrifier.repositories.database.AuthRepository
import fi.nutrifier.repositories.database.FineliRepository
import fi.nutrifier.repositories.database.FoodRepository
import fi.nutrifier.repositories.database.FoodEntryRepository
import fi.nutrifier.utils.AlertType
import fi.nutrifier.utils.Constants
import fi.nutrifier.utils.ConversionUtils
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
    private val fineliRepository = FineliRepository()

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
    fun setDate(date: LocalDate) {
        Log.d("FoodEntryViewModel", "Setting date... $date")
        _date.value = date
        loadFoodEntries()
    }

    private val emptyNutrientSummary = NutrientSummary(
        energy = Constants.EnergyUnit.entries.associateWith { 0.0 },
        fats = Constants.MacroWeightUnit.entries.associateWith { 0.0 },
        carbs = Constants.MacroWeightUnit.entries.associateWith { 0.0 },
        protein = Constants.MacroWeightUnit.entries.associateWith { 0.0 },
    )

    private var _overallNutrients = mutableStateOf(emptyNutrientSummary)
    val overallNutrients get() = _overallNutrients.value
    val setOverallNutrients: (NutrientSummary) -> Unit = { _overallNutrients.value = it }

    private var _mealNutrients = mutableStateOf<Map<MealType, NutrientSummary>>(emptyMap())
    val nutrients get() = _mealNutrients.value
    val setNutrients: (Map<MealType, NutrientSummary>) -> Unit = { _mealNutrients.value = it }

    suspend fun fetchFoodById(id: String): Food?  {
        val result = foodRepository.getFoodById(id)
        Log.d("FoodEntryViewModel", "fetchFoodById $result")
        return if (result.isSuccessful()) result.value else null
    }

    private var _selectedMeal = mutableStateOf<MealType?>(null)
    val selectedMeal get() = _selectedMeal.value
    val setSelectedMeal: (MealType?) -> Unit = { _selectedMeal.value = it }

    private var _selectedFoodEntry = mutableStateOf<FoodEntry?>(null)
    val selectedFoodEntry get() = _selectedFoodEntry.value
    val setSelectedFoodEntry: (FoodEntry?) -> Unit = { _selectedFoodEntry.value = it }

    private var _currentAmount = mutableStateOf(100.0)
    val currentAmount get() = _currentAmount.value

    fun setCurrentAmount(value: Double) {
        _currentAmount.value = value
    }

    private suspend fun calculateNutrientsForEntries(
        entries: List<FoodEntry>,
        getFood: suspend (FoodEntry) -> Food?,
    ): NutrientSummary {
        var calories = 0.0
        var fats = 0.0
        var carbs = 0.0
        var protein = 0.0

        entries.forEach { entry ->
            val food = getFood(entry)
            if (food != null) {
                val multiplier = entry.amount / 100
                calories += food.calories.times(multiplier)
                fats += food.fat.times(multiplier)
                carbs += food.carbs.times(multiplier)
                protein += food.protein.times(multiplier)
            }
        }

        val result = NutrientSummary(
            energy = Constants.EnergyUnit.entries.associateWith { energyUnit ->
                ConversionUtils.convertEnergy(fats, energyUnit)
            },
            fats = Constants.MacroWeightUnit.entries.associateWith { weightUnit ->
                ConversionUtils.convertMacroWeight(fats, weightUnit)
            },
            carbs = Constants.MacroWeightUnit.entries.associateWith { weightUnit ->
                ConversionUtils.convertMacroWeight(carbs, weightUnit)
            },
            protein = Constants.MacroWeightUnit.entries.associateWith { weightUnit ->
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
        if (triggerLoading) setLoading(true)

        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getFoodEntriesByDate(_date.value)
            val entries = result.value.orEmpty()

            if (!result.isSuccessful()) {
                showAlert("Error occured in loading food entries (${result.errorCode}")
                if (triggerLoading) setLoading(false)
                return@launch
            }

            val (fineliEntries, dbEntries) = entries.partition { it.fineliId != null }
            _entries.value = dbEntries

            val fineliFoods = fetchFineliFoods(fineliEntries.mapNotNull { it.fineliId })

            suspend fun FoodEntry.toFoodEntryFood(): FoodEntryFood {
                val food = when {
                    this.fineliId != null -> fineliFoods.find { it.fineliId == this.fineliId } ?: emptyFood
                    else -> fetchFoodById(this.foodId) ?: emptyFood
                }
                return FoodEntryFood(foodEntry = this, food = food)
            }

            val entriesByMeal = entries.groupBy { it.meal }

            val dbNutrientsByMeal = MealType.entries.associateWith { mealType ->
                val mealEntries = entriesByMeal[mealType]?.filter { it.fineliId == null }.orEmpty()
                calculateNutrientsForEntries(mealEntries) { fetchFoodById(it.foodId) }
            }

            val fineliNutrientsByMeal = MealType.entries.associateWith { mealType ->
                val mealEntries = entriesByMeal[mealType]?.filter { it.fineliId != null }.orEmpty()
                calculateNutrientsForEntries(mealEntries) { entry ->
                    fineliFoods.find { it.fineliId == entry.fineliId }
                }
            }

            val mealNutrients = MealType.entries.associateWith { mealType ->
                val listOfNutrientsByMeal = listOf(
                    dbNutrientsByMeal[mealType] ?: emptyNutrientSummary,
                    fineliNutrientsByMeal[mealType] ?: emptyNutrientSummary,
                )
                combineNutrientSummaries(listOfNutrientsByMeal)
            }
            setNutrients(mealNutrients)

            _breakfastEntries.value = entriesByMeal[MealType.BREAKFAST]?.map { it.toFoodEntryFood() } ?: emptyList()
            _lunchEntries.value = entriesByMeal[MealType.LUNCH]?.map { it.toFoodEntryFood() } ?: emptyList()
            _dinnerEntries.value = entriesByMeal[MealType.DINNER]?.map { it.toFoodEntryFood() } ?: emptyList()
            _snacksEntries.value = entriesByMeal[MealType.SNACKS]?.map { it.toFoodEntryFood() } ?: emptyList()

            val overallNutrients = combineNutrientSummaries(listOf(
                calculateNutrientsForEntries(dbEntries) { fetchFoodById(it.foodId) },
                calculateNutrientsForEntries(fineliEntries) { entry ->
                    fineliFoods.find { it.fineliId == entry.fineliId }
                }
            ))
            setOverallNutrients(overallNutrients)

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
            Log.d("FoodEntryViewModel", "Saving new entry: $foodEntry")

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