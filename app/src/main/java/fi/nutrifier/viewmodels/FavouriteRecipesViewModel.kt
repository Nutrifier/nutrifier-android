package fi.nutrifier.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import fi.nutrifier.models.database.Recipe
import fi.nutrifier.models.room.DatabaseProvider
import fi.nutrifier.models.room.FavouriteRecipe
import fi.nutrifier.repositories.room.FavouriteRecipeRepository
import fi.nutrifier.utils.ConversionUtils.toRecipe
import kotlinx.coroutines.launch

/**
 * ViewModel class for managing favorite recipes.
 *
 * This class provides methods to load, add, and delete favorite recipes.
 *
 * @property application The application context.
 */
class FavouriteRecipesViewModel(
    application: Application
): BaseViewModel(application), RecipesViewModel {

    private val database = DatabaseProvider.getInstance(application.applicationContext)
    private val repository: FavouriteRecipeRepository = FavouriteRecipeRepository(database)

    // Mutable state variables for holding favorite recipes, loading state, and error message
    private var _recipes = mutableStateListOf<FavouriteRecipe>()

    init {
        loadData()
    }

    // Public properties for observing favorite recipes, loading state, and error message
    override val recipes get() = _recipes.map { it.toRecipe() }

    /**
     * Loads favorite recipes from the repository.
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
     * Adds a recipe to the list of favorite recipes.
     *
     * @param r The recipe to add.
     */
    override fun add(r: Recipe) {
        setLoading(true)
        viewModelScope.launch {
            val favouriteRecipe = r.toFavourite()
            val responseHandler = repository.add(favouriteRecipe)
            if (responseHandler.error != null) showAlert(responseHandler.error)
            loadData()
        }
    }

    /**
     * Deletes a recipe from the list of favorite recipes.
     *
     * @param r The recipe to delete.
     */
    override fun delete(r: Recipe) {
        setLoading(true)
        viewModelScope.launch {
            val favouriteRecipe = r.toFavourite()
            val responseHandler = repository.delete(favouriteRecipe)
            if (responseHandler.error != null) showAlert(responseHandler.error)
            loadData()
        }
    }
}
