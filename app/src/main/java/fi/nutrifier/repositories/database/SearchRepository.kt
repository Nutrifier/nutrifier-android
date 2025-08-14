package fi.nutrifier.repositories.database

import fi.nutrifier.BuildConfig
import fi.nutrifier.models.database.SpoonacularRecipe
import fi.nutrifier.repositories.shared.RepositoryResponseHandler
import fi.nutrifier.services.database.RetrofitInstance

/**
 * Repository class for searching recipes.
 */
class SearchRepository {
    /**
     * Searches for recipes based on the provided query.
     *
     * @param query The search query.
     * @return A [RepositoryResponseHandler] containing either the list of found recipes on success
     * or an error message on failure.
     */
    suspend fun search(query: String): RepositoryResponseHandler<List<SpoonacularRecipe>> {
        try {
            val response = RetrofitInstance().recipeService.searchRecipes(BuildConfig.API_KEY, query, number = 10)
            if (response.isSuccessful) {
                response.body()?.let {
                    return RepositoryResponseHandler(success = it.results)
                }
            }
            return when (response.code()) {
                401 -> RepositoryResponseHandler(error = "Unauthorized! Contact the developer!")
                402 -> RepositoryResponseHandler(error = "Limit reached! Contact the developer!")
                else -> RepositoryResponseHandler(error = "Error with the code of ${response.code()} occurred!")
            }
        } catch (e: Exception) {
            return RepositoryResponseHandler(
                error = "Internal error occurred! Make sure you are connected to the internet."
            )
        }
    }
}