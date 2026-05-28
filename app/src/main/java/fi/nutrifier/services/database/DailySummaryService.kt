package fi.nutrifier.services.database

import fi.nutrifier.models.database.DailySummary
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface DailySummaryService {
    @GET("api/v1/daily-summary/by-date")
    suspend fun getDailyNutritionSummaryByDate(
        @Query("date") date: String,
        @Header("Authorization") authHeader: String,
    ): Response<DailySummary>

    @POST("api/v1/daily-summary/confirm")
    suspend fun confirmDay(
        @Query("date") date: String,
        @Header("Authorization") authHeader: String,
    ): Response<DailySummary>
}