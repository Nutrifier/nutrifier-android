package fi.nutrifier.viewmodels

import android.app.Application
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import fi.nutrifier.models.database.Recipe
import fi.nutrifier.models.database.SpoonacularRecipe
import fi.nutrifier.repositories.database.SearchRepository
import fi.nutrifier.repositories.room.PersonalRecipeRepository
import fi.nutrifier.utils.ConversionUtils.emptyRecipe
import kotlinx.coroutines.launch

/**
 * ViewModel class responsible for managing the search functionality.
 *
 * This class provides methods for searching recipes based on a query string and encapsulates the search results,
 * loading state, and error state.
 */
class SearchViewModel(
    private val repository: SearchRepository,
    encryptedSharedPreferences: SharedPreferences
): BaseViewModel(encryptedSharedPreferences) {

    private val _searchResults = mutableStateListOf<SpoonacularRecipe>()
    val searchResults: List<Recipe?> get() = _searchResults.map { it.toRecipe() ?: emptyRecipe } ?: listOf()

    /**
     * Updates the search results with the provided [newSearchResult].
     *
     * @param newSearchResult The new list of search results to be set.
     */
    private fun update(newSearchResult: List<SpoonacularRecipe>) {
        _searchResults.clear()
        _searchResults.addAll(newSearchResult)
    }

    /**
     * Performs a search based on the provided [query].
     * Updates the search results accordingly and sets the loading and error states.
     *
     * @param query The search query string.
     */
    fun search(query: String) {
        setLoading(true)
        viewModelScope.launch {
            val responseHandler = repository.search(query)
            if (responseHandler.success != null) {
                update(responseHandler.success)
            }

            if (responseHandler.error != null) showAlert(responseHandler.error)

            setLoading(false)
        }
    }
}
