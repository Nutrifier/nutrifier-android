package fi.nutrifier.services.room

import fi.nutrifier.models.database.SearchResponse
import fi.nutrifier.models.database.SpoonacularRecipe
import fi.nutrifier.models.database.SpoonacularResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

/**
 * Service interface defining methods for interacting with recipe-related endpoints of the Spoonacular API.
 */
interface RecipeService {
    @GET("recipes/random")
    suspend fun getRandomRecipes(
        @Query("apiKey") apiKey: String,
        @Query("include-tags") includeTags: String,
        @Query("includeNutrition") includeNutrition: Boolean,
        @Query("number") number: Int,
    ): Response<SpoonacularResponse>

    @GET("recipes/{id}/information")
    suspend fun getRecipeInformation(
        @Path("id") id: Int,
        @Query("includeNutrition") includeNutrition: Boolean,
        @Query("apiKey") apiKey: String
    ): Response<SpoonacularRecipe>

    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("apiKey") apiKey: String,
        @Query("query") query: String,
        @Query("number") number: Int
    ): Response<SearchResponse>
}