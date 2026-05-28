package fi.nutrifier.services.database

import fi.nutrifier.models.database.Goal
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST

interface GoalsService {

    @GET("api/v1/goals")
    suspend fun getGoals(
        @Header("Authorization") authHeader: String,
    ): Response<Goal>

    @PATCH("api/v1/goals")
    suspend fun updateGoals(
        @Body goal: Goal,
        @Header("Authorization") authHeader: String
    ): Response<Goal>

    @POST("api/v1/goals/recalculate")
    suspend fun calculateGoals(
        @Header("Authorization") authHeader: String
    ): Response<Goal>
}