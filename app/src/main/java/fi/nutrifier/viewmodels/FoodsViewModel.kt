package fi.nutrifier.viewmodels

import android.app.Application
import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import fi.nutrifier.models.database.Food
import fi.nutrifier.models.database.SelectedFood
import fi.nutrifier.repositories.database.AuthRepository
import fi.nutrifier.repositories.database.FineliRepository
import fi.nutrifier.repositories.database.FoodRepository
import fi.nutrifier.utils.AlertType
import fi.nutrifier.utils.ConversionUtils.calculatePev
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FoodsViewModel(
    private val repository: FoodRepository,
    encryptedSharedPreferences: SharedPreferences,
): BaseViewModel(encryptedSharedPreferences) {
    private val fineliRepository = FineliRepository()

    private var _foods: MutableState<List<Food>> = mutableStateOf(emptyList())
    val foods get() = _foods.value
    val setFoods: (List<Food>) -> Unit = { _foods.value = it }

    private var _savableFood: MutableState<Food?> = mutableStateOf(null)
    val savableFood get() = _savableFood.value
    val setSavableFood: (Food?) -> Unit = { _savableFood.value = it }

    private var _selectedFood = mutableStateOf<SelectedFood?>(null)
    val selectedFood get() = _selectedFood.value
    val setSelectedFood: (Food?) -> Unit = {
        _selectedFood.value = SelectedFood(
            food = it,
            pev = calculatePev(it?.calories, it?.protein),
        )
    }

    private var foodPage = 0
    private val foodPageSize = 20

    fun loadFoods() {
        android.util.Log.d("LogsScreenViewModel", "foods")
        foodPage = 0 // Reset food page

        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getFoods(foodPage, foodPageSize)
            if (result.isSuccessful()) {
                _foods.value = result.value ?: emptyList()
            } else {
                showAlert("Error occurred while loading foods (${result.errorCode}).")
            }
        }
    }

    fun loadMoreFoods() {
        android.util.Log.d("LogsScreenViewModel", "MORE foods")
        if (loading.value) return
        setLoading(true)

        viewModelScope.launch(Dispatchers.IO) {
            foodPage++
            val result = repository.getFoods(foodPage, foodPageSize)
            if (result.isSuccessful()) {
                android.util.Log.d("LogsScreenViewModel", "LoadMoreFoods result: ${result.value}")
                _foods.value += result.value ?: emptyList()
            }
            setLoading(false)
        }
    }

    fun searchFoods(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = mutableListOf<Food>()

            // If all characters are digits, presume we are using a barcode
            if (query.all { it.isDigit() }) {
                val barcodeResult = repository.getFoodsByBarcode(query)
                if (barcodeResult.isSuccessful()) {
                    result.addAll(barcodeResult.value ?: emptyList())
                }
            } else {

                // Searching foods from database and Fineli
                val fineliResult = fineliRepository.getFoodsByQuery(query)
                val repositoryResult = repository.getFoodsByQuery(query)

                // TODO: Show frequently used foods first
                // Then database results
                if (repositoryResult.isSuccessful()) {
                    result.addAll(repositoryResult.value ?: emptyList())
                }
                // Then Fineli results
                if (fineliResult.isSuccessful()) {
                    result.addAll(fineliResult.value?.map { it.toFood() } ?: emptyList())
                }

            }

            _foods.value = result
        }
    }

    fun saveFood(food: Food) {
        android.util.Log.d("LogsScreenViewModel", "Saving food: $food")

        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.saveFood(food)
            if (result.isSuccessful()) {
                loadFoods()
                showAlert("Food saved!", AlertType.INFO)
            } else {
                showAlert("Error occurred while saving the food (${result.errorCode}).")
            }
        }
    }
}