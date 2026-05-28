package fi.nutrifier.repositories.database

import android.content.SharedPreferences
import android.util.Log
import fi.nutrifier.models.database.UserProfile
import fi.nutrifier.services.database.RetrofitInstance
import fi.nutrifier.utils.Result
import fi.nutrifier.utils.SharedPreferencesManager

class ProfileRepository(private val encryptedPrefs: SharedPreferences) {
    private val service = RetrofitInstance().profileService

    suspend fun getProfile(): Result<UserProfile> {
        val savedToken: String? = SharedPreferencesManager.getAuthToken(encryptedPrefs)

        if (savedToken == null) return Result.fail(403, "Trying to access without a token")

        return try {
            val response = service.getProfile("Bearer $savedToken")
            if (response.isSuccessful) {
                Result.success(response.body())
            }
            else Result.fail(response.code())
        } catch (e: Exception) {
            Log.d("UserProfileRepository", "error: $e")

            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }

    suspend fun updateProfile(updated: UserProfile): Result<UserProfile> {
        val savedToken: String? = SharedPreferencesManager.getAuthToken(encryptedPrefs)

        if (savedToken == null) {
            Log.d("UserProfileRepository", "Token not found")
            return Result.fail(403, "Trying to access without a token")
        }

        return try {
            Log.d("UserProfileRepository", "updating profile: $updated token: $savedToken")

            val response = service.updateProfile(updated,"Bearer $savedToken")
            if (response.isSuccessful) {
                Result.success(response.body())
            }
            else Result.fail(response.code())
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }
}
