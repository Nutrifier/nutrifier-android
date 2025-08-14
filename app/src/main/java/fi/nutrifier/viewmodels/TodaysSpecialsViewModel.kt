package fi.nutrifier.viewmodels

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import fi.nutrifier.models.database.Recipe
import fi.nutrifier.utils.SharedPreferencesKeys.PREFS_NAME
import fi.nutrifier.models.room.FavouriteRecipe
import fi.nutrifier.repositories.room.TodaysSpecialsRepository
import fi.nutrifier.utils.ConversionUtils.toRecipe
import kotlinx.coroutines.launch

/**
 * ViewModel class responsible for managing today's specials functionality.
 *
 * This class retrieves today's special recipes from the repository and encapsulates them along with loading and error states.
 *
 * @property application The application context associated with the ViewModel.
 */
class TodaysSpecialsViewModel(application: Application): BaseViewModel(application) {
    private val repository: TodaysSpecialsRepository

    init {
        val prefs = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        repository = TodaysSpecialsRepository(prefs)
    }

    // Specials state encapsulation
    private val _recipes = mutableStateListOf<FavouriteRecipe>()
    val recipes: List<Recipe> get() = _recipes.map { it.toRecipe() }

    init {
        setLoading(true)
        viewModelScope.launch {
            val responseHandler = repository.getTodaysSpecials()
            if (responseHandler.success != null) {
                _recipes.addAll(responseHandler.success)
            }

            if (responseHandler.error != null) showAlert(responseHandler.error)

            setLoading(false)
        }
    }
}
