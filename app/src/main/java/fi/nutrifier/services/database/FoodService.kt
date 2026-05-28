package fi.nutrifier.services.database

import fi.nutrifier.models.database.Food
import fi.nutrifier.models.database.FoodRequest
import fi.nutrifier.models.database.PaginatedResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

interface FoodService {
    @GET("api/v1/foods")
    suspend fun getFoods(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Header("Authorization") authHeader: String,
    ): Response<PaginatedResponse<Food>>

    @GET("api/v1/foods/{id}")
    suspend fun getFoodById(
        @Path("id") id: String,
        @Header("Authorization") authHeader: String,
    ): Response<Food>

    @GET("api/v1/foods/query")
    suspend fun getFoodByQuery(
        @Query("query") query: String,
        @Header("Authorization") authHeader: String,
    ): Response<List<Food>>

    @GET("api/v1/foods/barcode")
    suspend fun getFoodByBarcode(
        @Query("query") query: String,
        @Header("Authorization") authHeader: String,
    ): Response<List<Food>>

    @GET("api/v1/foods/recent")
    suspend fun getRecentFoods(
        @Header("Authorization") authHeader: String,
    ): Response<List<Food>>

    @POST("api/v1/foods")
    suspend fun saveFood(
        @Body foodRequest: FoodRequest,
        @Header("Authorization") authHeader: String,
    ): Response<Food>
}