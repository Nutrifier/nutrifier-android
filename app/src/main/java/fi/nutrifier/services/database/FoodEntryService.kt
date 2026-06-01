package fi.nutrifier.services.database

import fi.nutrifier.models.database.FoodEntry
import fi.nutrifier.models.database.FoodEntryRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FoodEntryService {
    @GET("api/v1/food-entries")
    suspend fun getFoodEntriesByDateAndMealTypeAndUserId(
        @Query("date") date: String,
        @Query("mealType") mealType: String? = null,
        @Header("Authorization") authHeader: String,
    ): Response<List<FoodEntry>>

    @POST("api/v1/food-entries")
    suspend fun saveFoodEntry(
        @Body foodEntryRequest: FoodEntryRequest,
        @Header("Authorization") authHeader: String,
    ): Response<FoodEntry>

    @PATCH("api/v1/food-entries/{id}")
    suspend fun updateFoodEntry(
        @Path("id") id: String,
        @Body foodEntry: FoodEntry,
        @Header("Authorization") authHeader: String,
    ): Response<FoodEntry>

    @DELETE("api/v1/food-entries/{id}")
    suspend fun deleteFoodEntry(
        @Path("id") id: String,
        @Header("Authorization") authHeader: String,
    ): Response<Void>
}