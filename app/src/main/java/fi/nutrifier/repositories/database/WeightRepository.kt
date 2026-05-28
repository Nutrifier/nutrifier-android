package fi.nutrifier.repositories.database

import android.content.SharedPreferences
import android.util.Log
import fi.nutrifier.models.database.PageResult
import fi.nutrifier.models.database.UserWeight
import fi.nutrifier.services.database.RetrofitInstance
import fi.nutrifier.utils.Result
import fi.nutrifier.utils.SharedPreferencesManager

class WeightRepository(private val encryptedPrefs: SharedPreferences) {
    private val service = RetrofitInstance().weightService

    suspend fun getWeighIns(page: Int = 0, size: Int = 10): Result<PageResult<UserWeight>> {
        val savedToken: String? = SharedPreferencesManager.getAuthToken(encryptedPrefs)

        if (savedToken == null) {
            Log.d("WeightRepository", "Token not found")
            return Result.fail(403, "Trying to access without a token")
        }

        return try {
            val response = service.getWeighIns(page, size,"Bearer $savedToken")
            if (response.isSuccessful) {
                Result.success(response.body())
            }
            else Result.fail(response.code())
        } catch (e: Exception) {
            Log.d("WeightRepository", "Error occurred in getting weight: $e")
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }

    suspend fun addNewWeighIn(weight: Double): Result<UserWeight> {
        val savedToken: String? = SharedPreferencesManager.getAuthToken(encryptedPrefs)

        if (savedToken == null) return Result.fail(403, "Trying to access without a token")

        return try {
            val response = service.addWeighIn(weight,"Bearer $savedToken")
            if (response.isSuccessful) {
                Result.success(response.body())
            }
            else Result.fail(response.code())
        } catch (e: Exception) {
            Log.d("UserSettingsRepository", "Error occurred in adding weight: $e")
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }
}
