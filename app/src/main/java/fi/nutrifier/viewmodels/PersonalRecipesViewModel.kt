package fi.nutrifier.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import fi.nutrifier.models.database.FoodEntry
import fi.nutrifier.models.database.FoodEntryFood
import fi.nutrifier.models.room.Recipe
import fi.nutrifier.models.room.PersonalRecipe
import fi.nutrifier.repositories.room.PersonalRecipeRepository
import fi.nutrifier.utils.ConversionUtils.emptyFood
import fi.nutrifier.utils.ConversionUtils.toRecipe
import fi.nutrifier.utils.Enums
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.collections.filter
import kotlin.collections.map
import kotlin.collections.orEmpty

/**
 * ViewModel class for managing personal recipes.
 *
 * This class provides methods to load, add, edit, and delete personal recipes.
 */
class PersonalRecipesViewModel(
    private val repository: PersonalRecipeRepository,
    encryptedSharedPreferences: SharedPreferences
): BaseViewModel(encryptedSharedPreferences), RecipesViewModel {

    // Mutable state variables for holding personal recipes, loading state, and error message
    private var _recipes = mutableStateListOf<PersonalRecipe>()
    override val recipes: List<Recipe> get() = _recipes.map { it.toRecipe() }

    init {
        loadData()
    }

    override fun loadData() {
        viewModelScope.launch {
            val responseHandler = repository.getAll()
            if (responseHandler.success != null) {
                _recipes.clear()
                _recipes.addAll(responseHandler.success)
            }
            if (responseHandler.error != null) {
                showAlert(responseHandler.error)
            }
            setLoading(false)
        }
    }

    /**
     * Adds a recipe to the list of personal recipes.
     *
     * @param r The recipe to add.
     */
    override fun add(r: Recipe) {
        viewModelScope.launch {
            setLoading(true)
            val personalRecipe = r.toPersonal()
            val responseHandler = repository.add(personalRecipe)
            if (responseHandler.error != null) {
                showAlert(responseHandler.error)
            }
            loadData()
        }
    }

    /**
     * Deletes a recipe from the list of personal recipes.
     *
     * @param r The recipe to delete.
     */
    override fun delete(r: Recipe) {
        //setLoading(true)
        viewModelScope.launch {
            val responseHandler = repository.delete(r.toPersonal())
            if (responseHandler.error != null) {
                showAlert(responseHandler.error)
            }
            loadData()
        }
    }

    /**
     * Edits a personal recipe.
     *
     * @param r The updated recipe.
     */
    fun edit(r: Recipe) {
        //setLoading(true)
        viewModelScope.launch {
            val responseHandler = repository.update(r.toPersonal())
            if (responseHandler.error != null) {
                showAlert(responseHandler.error)
            }
            loadData()
        }
    }

    /**
     * Checks if a recipe exists in the database.
     *
     * @param recipe The recipe to check.
     * @param callback The callback function to handle the result.
     */
    fun isRecipeInDatabase(recipe: Recipe, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            setLoading(true)
            val responseHandler = repository.isRecipeInDatabase(recipe.id)
            if (responseHandler.success != null) callback(responseHandler.success)
            if (responseHandler.error != null) {
                showAlert(responseHandler.error)
            }
            loadData()
        }
    }
}
