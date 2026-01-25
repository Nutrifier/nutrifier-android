package fi.nutrifier.repositories.room

import android.util.Log
import fi.nutrifier.BuildConfig
import fi.nutrifier.models.database.SpoonacularRecipe
import fi.nutrifier.services.database.RetrofitInstance
import java.util.UUID

/**
 * Repository class for fetching detailed information about a recipe under inspection from the Spoonacular API.
 */
class RecipeUnderInspectionRepository {
    /**
     * Fetches detailed information about a recipe under inspection from the Spoonacular API.
     *
     * @param recipeId The ID of the recipe to fetch.
     * @return The fetched [fi.nutrifier.models.database.SpoonacularRecipe] if successful, or null if the request fails.
     */
    suspend fun fetchRecipe(recipeId: Int, hasPremium: Boolean = false): SpoonacularRecipe? {
        val response = RetrofitInstance().recipeService.getRecipeInformation(
            apiKey = BuildConfig.API_KEY,
            id = recipeId,
            includeNutrition = hasPremium,
        )
        Log.d("RecipeFetchResponse", "Response: ${response.body()}")
        if (response.isSuccessful) {
            return response.body()
        }
        return null
    }
}