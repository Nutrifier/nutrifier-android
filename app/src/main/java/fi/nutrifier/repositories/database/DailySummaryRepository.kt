package fi.nutrifier.repositories.database

import android.content.SharedPreferences
import android.util.Log
import fi.nutrifier.models.database.DailySummary
import fi.nutrifier.services.database.RetrofitInstance
import fi.nutrifier.utils.Result
import fi.nutrifier.utils.SharedPreferencesManager
import java.time.LocalDate

class DailySummaryRepository(private val encryptedPrefs: SharedPreferences) {
    private val retrofitInstance = RetrofitInstance()
    private val service = retrofitInstance.dailySummaryService

    suspend fun getByDate(date: LocalDate): Result<DailySummary> {
        val token: String? = SharedPreferencesManager.getAuthToken(encryptedPrefs)

        Log.d("DailyNutritionSummaryRepository", "Getting summary by date")

        return try {
            val response = service.getDailyNutritionSummaryByDate(
                date.toString(),
                "Bearer $token"
            )
            if (response.isSuccessful && response.body() != null) {
                Log.d("DailyNutritionSummaryRepository", "response: ${response.body()}")
                Result.success(response.body())
            }
            else Result.fail(response.code())
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }

    suspend fun confirmDay(date: LocalDate): Result<DailySummary> {
        val token: String? = SharedPreferencesManager.getAuthToken(encryptedPrefs)

        Log.d("DailyNutritionSummaryRepository", "Getting summary by date")

        return try {
            val response = service.confirmDay(
                date.toString(),
                "Bearer $token"
            )
            if (response.isSuccessful && response.body() != null) {
                Log.d("DailyNutritionSummaryRepository", "response: ${response.body()}")
                Result.success(response.body())
            }
            else Result.fail(response.code())
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }
}