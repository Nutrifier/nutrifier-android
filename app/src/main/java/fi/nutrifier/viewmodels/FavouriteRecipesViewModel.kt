package fi.nutrifier.viewmodels

import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import fi.nutrifier.models.room.Recipe
import fi.nutrifier.models.room.FavouriteRecipe
import fi.nutrifier.repositories.room.FavouriteRecipeRepository
import fi.nutrifier.utils.ConversionUtils.toRecipe
import kotlinx.coroutines.launch

/**
 * ViewModel class for managing favorite recipes.
 *
 * This class provides methods to load, add, and delete favorite recipes.
 */
class FavouriteRecipesViewModel(
    private val repository: FavouriteRecipeRepository,
    encryptedSharedPreferences: SharedPreferences
): BaseViewModel(encryptedSharedPreferences), RecipesViewModel {

    private var _recipes = mutableStateListOf<FavouriteRecipe>()
    override val recipes get() = _recipes.map { it.toRecipe() }

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

            if (responseHandler.error != null) showAlert(responseHandler.error)

            setLoading(false)
        }
    }

    override fun add(r: Recipe) {
        setLoading(true)
        viewModelScope.launch {
            val favouriteRecipe = r.toFavourite()
            val responseHandler = repository.add(favouriteRecipe)
            if (responseHandler.error != null) showAlert(responseHandler.error)
            loadData()
        }
    }

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
