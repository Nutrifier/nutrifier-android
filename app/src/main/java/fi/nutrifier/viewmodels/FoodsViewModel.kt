package fi.nutrifier.viewmodels

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import fi.nutrifier.models.database.Food
import fi.nutrifier.models.database.FoodBarcodeRequest
import fi.nutrifier.models.database.FoodRequest
import fi.nutrifier.models.database.SelectedFood
import fi.nutrifier.repositories.database.AuthRepository
import fi.nutrifier.repositories.database.FineliRepository
import fi.nutrifier.repositories.database.FoodRepository
import fi.nutrifier.utils.AlertType
import fi.nutrifier.utils.ConversionUtils.calculatePev
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class FoodsViewModel(
    private val repository: FoodRepository,
    encryptedSharedPreferences: SharedPreferences,
): BaseViewModel(encryptedSharedPreferences) {
    private val fineliRepository = FineliRepository()

    private var _foods: MutableState<List<Food>> = mutableStateOf(emptyList())
    val foods get() = _foods.value

    private var _recentFoods: MutableState<List<Food>> = mutableStateOf(emptyList())
    val recentFoods get() = _recentFoods.value

    private var _totalFoodsCount: MutableState<Int> = mutableIntStateOf(0)
    val totalFoodsCount get() = _totalFoodsCount.value

    private var _firstItemIndex: MutableState<Int> = mutableIntStateOf(0)
    val firstItemIndex get() = _firstItemIndex.value

    private var _lastItemIndex: MutableState<Int> = mutableIntStateOf(0)
    val lastItemIndex get() = _lastItemIndex.value

    private var _savableFood: MutableState<FoodRequest?> = mutableStateOf(null)
    val savableFood get() = _savableFood.value
    val setSavableFood: (FoodRequest?) -> Unit = { _savableFood.value = it }

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
        foodPage = 0 // Reset food page

        viewModelScope.launch(Dispatchers.IO) {
            // TODO: Make recents specific to meals
            val recentResult = repository.getRecentFoods()
            if (recentResult.isSuccessful()) {
                _recentFoods.value = recentResult.value ?: emptyList()

            } else {
                showAlert("Error occurred while loading recent foods (${recentResult.errorCode}).")
            }

            val result = repository.getFoods(foodPage, foodPageSize)
            if (result.isSuccessful()) {
                // Filtering out "recentFoods" from "foods" so duplicate values (and id's) are not used in the same LazyList
                val recentFiltered = result.value?.content?.filter { food ->
                    recentResult.value?.find { recent ->
                        food.id == recent.id
                    } == null
                }
                _foods.value = recentFiltered ?: emptyList()
                if (result.value?.content != null && result.value.content.isNotEmpty()) {
                    _totalFoodsCount.value = result.value.totalElements.toInt()
                    _firstItemIndex.value = (result.value.size * result.value.number) + 1
                    _lastItemIndex.value = _firstItemIndex.value + result.value.content.size - 1
                }
            } else {
                showAlert("Error occurred while loading foods (${result.errorCode}).")
            }
        }
    }

    fun loadMoreFoods() {
        if (loading.value) return
        setLoading(true)

        viewModelScope.launch(Dispatchers.IO) {
            foodPage++
            val result = repository.getFoods(foodPage, foodPageSize)
            if (result.isSuccessful()) {
                android.util.Log.d("LogsScreenViewModel", "LoadMoreFoods result: ${result.value}")
                _foods.value += result.value?.content ?: emptyList()
                if (result.value?.content != null && result.value.content.isNotEmpty()) {
                    _totalFoodsCount.value = result.value.totalElements.toInt()
                    _lastItemIndex.value += result.value.content.size
                }
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
                val repositoryResult = repository.getFoodsByQuery(0, 10, query)
                // TODO: Hard coded values here

                Log.d("FoodsViewModel", "Database results: ${repositoryResult}")
                Log.d("FoodsViewModel", "Fineli results: ${fineliResult}")

                // Then database results
                if (repositoryResult.isSuccessful()) {
                    val sorted = repositoryResult.value?.content?.sortedBy { r -> r.name }
                    result.addAll(sorted ?: emptyList())
                    Log.d("FoodsViewModel", "Database results: ${repositoryResult.value}")
                }
                // Then Fineli results
                if (fineliResult.isSuccessful()) {
                    val sorted = fineliResult.value?.map { it.toFood() }?.sortedBy { r -> r.name }
                    result.addAll(sorted ?: emptyList())
                    Log.d("FoodsViewModel", "Fineli results: ${fineliResult.value}")
                }
            }

            _foods.value = result
            _recentFoods.value = emptyList()
        }
    }

    fun saveFood(foodRequest: FoodRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.saveFood(foodRequest)
            if (result.isSuccessful()) {
                loadFoods()
                showAlert("Food saved!", AlertType.INFO)
            } else {
                showAlert("Error occurred while saving the food (${result.errorCode}).")
            }
        }
    }

    fun updateFoodFromSelectedFood() {
        viewModelScope.launch(Dispatchers.IO) {
            if (selectedFood?.food != null) {
                val foodId = selectedFood!!.food!!.id
                val foodRequest = FoodRequest(
                    name = selectedFood!!.food!!.name,
                    brand = selectedFood!!.food!!.brand ?: "",
                    category = selectedFood!!.food!!.category ?: "",
                    barcode = selectedFood!!.food!!.barcode ?: "",
                    servingSize = selectedFood!!.food!!.servingSize,
                    calories = selectedFood!!.food!!.calories,
                    fat = selectedFood!!.food!!.fat,
                    carbs = selectedFood!!.food!!.carbs,
                    protein = selectedFood!!.food!!.protein
                )

                val result = repository.updateFood(UUID.fromString(foodId), foodRequest)
                if (result.isSuccessful()) {
                    loadFoods()
                    showAlert("Food saved!", AlertType.INFO)
                } else {
                    showAlert("Error occurred while saving the food (${result.errorCode}).")
                }
            }
        }
    }

    fun addBarcode(newBarcode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (_selectedFood.value?.food?.id == null) {
                return@launch
            }

            val result = repository.addBarcode(_selectedFood.value!!.food!!.id!!, FoodBarcodeRequest(newBarcode))
            if (result.isSuccessful()) {
                loadFoods()
                showAlert("Food saved!", AlertType.INFO)
            } else {
                showAlert("Error occurred while saving the food (${result.errorCode}).")
            }
        }
    }
}