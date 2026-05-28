package fi.nutrifier.repositories.database

import android.content.SharedPreferences
import android.util.Log
import fi.nutrifier.models.database.Analytics
import fi.nutrifier.models.database.DailySummary
import fi.nutrifier.services.database.RetrofitInstance
import fi.nutrifier.utils.Enums
import fi.nutrifier.utils.Result
import fi.nutrifier.utils.SharedPreferencesManager
import java.time.LocalDate

class AnalyticsRepository(private val encryptedPrefs: SharedPreferences) {
    private val retrofitInstance = RetrofitInstance()
    private val service = retrofitInstance.analyticsService

    suspend fun getByDateAndPeriod(date: LocalDate, period: Enums.AnalyticsTimePeriod): Result<Analytics> {
        val token: String? = SharedPreferencesManager.getAuthToken(encryptedPrefs)

        return try {
            val response = service.getAnalyticsByDateAndPeriod(
                date.toString(),
                period.name,
                "Bearer $token"
            )
            if (response.isSuccessful && response.body() != null) {
                Log.d("AnalyticsRepository", "response: ${response.body()}")
                Result.success(response.body())
            }
            else Result.fail(response.code())
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }
}