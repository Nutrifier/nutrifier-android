package fi.nutrifier.services.database

import fi.nutrifier.models.database.Analytics
import fi.nutrifier.models.database.DailySummary
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface AnalyticsService {
    @GET("api/v1/analytics")
    suspend fun getAnalyticsByDateAndPeriod(
        @Query("date") date: String,
        @Query("period") period: String,
        @Header("Authorization") authHeader: String,
    ): Response<Analytics>
}