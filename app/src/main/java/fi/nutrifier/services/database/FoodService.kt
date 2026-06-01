package fi.nutrifier.services.database

import fi.nutrifier.models.database.Food
import fi.nutrifier.models.database.FoodBarcodeRequest
import fi.nutrifier.models.database.FoodRequest
import fi.nutrifier.models.database.PaginatedResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @GET("api/v1/foods/by-ids")
    suspend fun getFoodByIds(
        @Query("ids") ids: List<String>,
        @Header("Authorization") authHeader: String,
    ): Response<List<Food>>

    @GET("api/v1/foods/query")
    suspend fun getFoodByQuery(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("query") query: String,
        @Header("Authorization") authHeader: String,
    ): Response<PaginatedResponse<Food>>

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

    @PATCH("api/v1/admin/foods/{id}")
    suspend fun updateFood(
        @Path("id") foodId: UUID,
        @Body foodRequest: FoodRequest,
        @Header("Authorization") authHeader: String,
    ): Response<Food>

    @PUT("api/v1/foods/{id}/barcode")
    suspend fun addBarcode(
        @Path("id") foodId: UUID,
        @Body request: FoodBarcodeRequest,
        @Header("Authorization") authHeader: String,
    ): Response<Food>
}