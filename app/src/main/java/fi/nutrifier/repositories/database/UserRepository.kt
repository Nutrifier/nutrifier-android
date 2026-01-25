package fi.nutrifier.repositories.database

import android.content.SharedPreferences
import android.util.Log
import fi.nutrifier.models.database.User
import fi.nutrifier.models.database.UserSettings
import fi.nutrifier.services.database.RetrofitInstance
import fi.nutrifier.utils.Result
import fi.nutrifier.utils.SharedPreferencesManager

class UserRepository(private val encryptedPrefs: SharedPreferences) {
    private val service = RetrofitInstance().userService

    suspend fun getUser(token: String? = null): Result<User> {
        val savedToken: String? = SharedPreferencesManager.getAuthToken(encryptedPrefs)
        return try {
            val response = service.getUser("Bearer ${token ?: savedToken}")
            if (response.isSuccessful) {
                Result.success(response.body())
            }
            else Result.fail(response.code())
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }

    suspend fun updateSettings(updatedSettings: UserSettings): Result<UserSettings> {
        val savedToken: String? = SharedPreferencesManager.getAuthToken(encryptedPrefs)
        return try {
            Log.d("UserRepository", "updating settings: $updatedSettings token: $savedToken")

            val response = service.updateSettings(updatedSettings,"Bearer $savedToken")
            if (response.isSuccessful) {
                Result.success(response.body())
            }
            else Result.fail(response.code())
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }
}
