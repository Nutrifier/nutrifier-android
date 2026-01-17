package fi.nutrifier.repositories.database

import android.content.SharedPreferences
import fi.nutrifier.models.database.User
import fi.nutrifier.models.database.UserSettings
import fi.nutrifier.services.database.RetrofitInstance
import fi.nutrifier.utils.Result
import fi.nutrifier.utils.SharedPreferencesManager

class UserRepository(private val encryptedPrefs: SharedPreferences) {
    private val service = RetrofitInstance().userService
    private val token: String? = SharedPreferencesManager.getAuthToken(encryptedPrefs)

    suspend fun getUser(): Result<User> {
        return try {
            val response = service.getUser("Bearer $token")
            if (response.isSuccessful) {
                Result.success(response.body())
            }
            else Result.fail(response.code())
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }

    suspend fun updateSettings(updatedSettings: UserSettings): Result<UserSettings> {
        return try {
            val response = service.updateSettings(updatedSettings,"Bearer $token")
            if (response.isSuccessful) {
                Result.success(response.body())
            }
            else Result.fail(response.code())
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }
}
