package fi.nutrifier.repositories.database

import android.content.SharedPreferences
import fi.nutrifier.BuildConfig
import fi.nutrifier.models.database.FoodEntry
import fi.nutrifier.services.database.RetrofitInstance
import fi.nutrifier.utils.Result
import fi.nutrifier.utils.SharedPreferencesManager
import java.time.LocalDate

class FoodEntryRepository(private val encryptedPrefs: SharedPreferences) {
    private val retrofitInstance = RetrofitInstance()
    private val service = retrofitInstance.foodEntryService

    suspend fun getFoodEntriesByDate(date: LocalDate): Result<List<FoodEntry>> {
        val token: String? = SharedPreferencesManager.getAuthToken(encryptedPrefs)
        val userId: String? = SharedPreferencesManager.getUser(encryptedPrefs)?.id

        return try {
            val response = service.getFoodEntriesByDateAndUser(
                date.toString(),
                userId ?: BuildConfig.APP_UUID,
                "Bearer $token"
            )
            if (response.isSuccessful && response.body() != null) Result.success(response.body())
            else Result.fail(response.code())
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }

    suspend fun saveFoodEntry(foodEntry: FoodEntry): Result<Boolean> {
        val token: String? = SharedPreferencesManager.getAuthToken(encryptedPrefs)

        return try {
            val response = service.saveFoodEntry(foodEntry, "Bearer $token")
            if (response.isSuccessful) Result.success(true)
            else Result.fail(response.code())
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }

    suspend fun deleteFoodEntry(id: String): Result<Boolean> {
        val token: String? = SharedPreferencesManager.getAuthToken(encryptedPrefs)

        return try {
            val response = service.deleteFoodEntry(id, "Bearer $token")
            if (response.isSuccessful) Result.success(true)
            else Result.fail(response.code())
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }

    suspend fun updateFoodEntry(foodEntry: FoodEntry): Result<FoodEntry> {
        val token: String? = SharedPreferencesManager.getAuthToken(encryptedPrefs)

        return try {
            if (foodEntry.id != null) {
                val response = service.updateFoodEntry(foodEntry.id, foodEntry, "Bearer $token")
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body())
                } else {
                    Result.fail(response.code())
                }
            } else {
                Result.fail(400, "Log ID cannot be null.")
            }
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }
}