package fi.nutrifier.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import fi.nutrifier.models.database.Recipe
import fi.nutrifier.models.room.DatabaseProvider
import fi.nutrifier.models.room.PersonalRecipe
import fi.nutrifier.repositories.room.PersonalRecipeRepository
import fi.nutrifier.utils.ConversionUtils.toRecipe
import kotlinx.coroutines.launch

/**
 * ViewModel class for managing personal recipes.
 *
 * This class provides methods to load, add, edit, and delete personal recipes.
 *
 * @property application The application context.
 */
class PersonalRecipesViewModel(
    application: Application
): BaseViewModel(application), RecipesViewModel {

    private val database = DatabaseProvider.getInstance(application.applicationContext)
    private val repository = PersonalRecipeRepository(database)

    // Mutable state variables for holding personal recipes, loading state, and error message
    private var _recipes = mutableStateListOf<PersonalRecipe>()

    init { loadData() }

    // Public properties for observing personal recipes, loading state, and error message
    override val recipes: List<Recipe> get() = _recipes.map { it.toRecipe() }

    /**
     * Loads personal recipes from the repository.
     * Updates the loading state and error message accordingly.
     */
    override fun loadData() {
        viewModelScope.launch {
            val responseHandler = repository.getAll()
            if (responseHandler.success != null) {
                _recipes.clear()
                _recipes.addAll(responseHandler.success)
            }

            if (responseHandler.error != null) showAlert(responseHandler.error)

            setLoading(false)
        }
    }

    /**
     * Adds a recipe to the list of personal recipes.
     *
     * @param r The recipe to add.
     */
    override fun add(r: Recipe) {
        setLoading(true)
        viewModelScope.launch {
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
        setLoading(true)
        viewModelScope.launch {
            val responseHandler = repository.delete(r.toPersonal())
            if (responseHandler.error != null) showAlert(responseHandler.error)
            loadData()
        }
    }

    /**
     * Edits a personal recipe.
     *
     * @param r The updated recipe.
     */
    fun edit(r: Recipe) {
        setLoading(true)
        viewModelScope.launch {
            val responseHandler = repository.update(r.toPersonal())
            if (responseHandler.error != null) showAlert(responseHandler.error)
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
        setLoading(true)
        viewModelScope.launch {
            val responseHandler = repository.isRecipeInDatabase(recipe.id)
            if (responseHandler.success != null) callback(responseHandler.success)
            if (responseHandler.error != null) showAlert(responseHandler.error)
            loadData()
        }
    }
}
