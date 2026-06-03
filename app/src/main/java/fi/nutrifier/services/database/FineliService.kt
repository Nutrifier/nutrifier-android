package fi.nutrifier.services

import fi.nutrifier.models.database.FineliResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// TODO: Remove these!!! Backend should handle the fetching from Fineli API all together
interface FineliService {
    @GET("fineli/api/v1/foods")
    suspend fun getFoods(
        @Query("q") query: String,
    ): Response<List<FineliResponse>>

    @GET("fineli/api/v1/foods/{id}")
    suspend fun getFoodById(
        @Path("id") id: Int,
    ): Response<FineliResponse>
}