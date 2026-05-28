package fi.nutrifier.services.database

import fi.nutrifier.models.database.PageResult
import fi.nutrifier.models.database.UserWeight
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface WeightService {

    @POST("api/v1/weight")
    suspend fun addWeighIn(
        @Body newWeighIn: Double,
        @Header("Authorization") authHeader: String,
    ): Response<UserWeight>

    @GET("api/v1/weight")
    suspend fun getWeighIns(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Header("Authorization") authHeader: String
    ): Response<PageResult<UserWeight>>
}