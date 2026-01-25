package fi.nutrifier.repositories.room

import android.content.SharedPreferences
import fi.nutrifier.BuildConfig
import fi.nutrifier.models.database.MealType
import fi.nutrifier.models.room.FavouriteRecipe
import fi.nutrifier.repositories.shared.RepositoryResponseHandler
import fi.nutrifier.services.database.RetrofitInstance
import fi.nutrifier.utils.SharedPreferencesManager

/**
 * Repository class for managing today's specials.
 *
 * @param prefs The [android.content.SharedPreferences] instance used for storing and retrieving today's specials.
 */
class TodaysSpecialsRepository(private val prefs: SharedPreferences) {
    /**
     * Retrieves today's specials from SharedPreferences if available, otherwise fetches them from the server.
     *
     * @return A [RepositoryResponseHandler] containing either the list of today's specials on success
     * or an error message on failure.
     */
    suspend fun getTodaysSpecials(hasPremium: Boolean = false): RepositoryResponseHandler<List<FavouriteRecipe>> {
        val isLoaded = SharedPreferencesManager.isTodaysSpecialsLoaded(prefs)

        if (isLoaded) {
            val newRecipes = SharedPreferencesManager.getTodaysSpecials(prefs)
            return if (newRecipes != null) {
                RepositoryResponseHandler(success = newRecipes)
            } else {
                RepositoryResponseHandler(error = "Error with getting saved specials!")
            }
        } else {
            val newRecipes = MealType.entries.map {
                val response = RetrofitInstance().recipeService.getRandomRecipes(
                    apiKey = BuildConfig.API_KEY,
                    includeTags = it.toString().lowercase(),
                    includeNutrition = hasPremium,
                    number = 1,
                )
                if (response.isSuccessful) {
                    response.body()?.recipes?.get(0)?.toSavable()
                } else {
                    return RepositoryResponseHandler(error = "Error with the code of ${response.code()} occurred!")
                }
            }

            SharedPreferencesManager.saveTodaysSpecials(prefs, newRecipes.filterNotNull())
            return RepositoryResponseHandler(success = newRecipes.filterNotNull())
        }
    }
}