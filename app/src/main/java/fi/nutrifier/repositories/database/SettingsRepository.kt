package fi.nutrifier.repositories.database

import android.content.SharedPreferences
import android.util.Log
import fi.nutrifier.models.database.ApiInfo
import fi.nutrifier.models.database.UserSettings
import fi.nutrifier.services.database.RetrofitInstance
import fi.nutrifier.utils.Result
import fi.nutrifier.utils.SharedPreferencesManager

class SettingsRepository(private val encryptedPrefs: SharedPreferences) {
    private val service = RetrofitInstance().settingsService

    suspend fun getSettings(): Result<UserSettings> {
        val savedToken: String? = SharedPreferencesManager.getAuthToken(encryptedPrefs)

        if (savedToken == null) {
            Log.d("UserSettingsRepository", "Token not found")
            return Result.fail(403, "Trying to access without a token")
        }

        return try {
            val response = service.getSettings("Bearer $savedToken")
            if (response.isSuccessful) {
                Result.success(response.body())
            }
            else Result.fail(response.code())
        } catch (e: Exception) {
            Log.d("UserSettingsRepository", "Error occurred in getting settings: $e")
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }

    suspend fun getApiInfo(): Result<ApiInfo> {
        val savedToken: String? = SharedPreferencesManager.getAuthToken(encryptedPrefs)

        if (savedToken == null) {
            Log.d("UserSettingsRepository", "Token not found")
            return Result.fail(403, "Trying to access without a token")
        }

        return try {
            val response = service.getApiInfo("Bearer $savedToken")
            if (response.isSuccessful) {
                Result.success(response.body())
            }
            else Result.fail(response.code())
        } catch (e: Exception) {
            Log.d("UserSettingsRepository", "Error occurred in getting api info: $e")
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }

    suspend fun updateSettings(updatedSettings: UserSettings): Result<UserSettings> {
        val savedToken: String? = SharedPreferencesManager.getAuthToken(encryptedPrefs)

        if (savedToken == null) {
            return Result.fail(403, "Trying to access without a token")
        }

        return try {
            val response = service.updateSettings(updatedSettings,"Bearer $savedToken")
            if (response.isSuccessful) {
                Result.success(response.body())
            }
            else Result.fail(response.code())
        } catch (e: Exception) {
            Log.d("UserSettingsRepository", "Error occurred in updating settings: $e")
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }
}
