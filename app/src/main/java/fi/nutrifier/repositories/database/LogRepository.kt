package fi.nutrifier.repositories.database

import android.content.SharedPreferences
import fi.nutrifier.BuildConfig
import fi.nutrifier.models.database.Log
import fi.nutrifier.services.database.RetrofitInstance
import fi.nutrifier.utils.Result
import fi.nutrifier.utils.SharedPreferencesManager
import java.time.LocalDate
import java.util.UUID

class LogRepository(private val encryptedPrefs: SharedPreferences) {
    private val retrofitInstance = RetrofitInstance()
    private val service = retrofitInstance.logsService

    suspend fun getLogsByDate(date: LocalDate): Result<List<Log>> {
        val token: String? = SharedPreferencesManager.getAuthToken(encryptedPrefs)
        val userId: String? = SharedPreferencesManager.getUser(encryptedPrefs)?.id

        return try {
            android.util.Log.d("LogRepository", "Haetaan avaimella: $token")
            android.util.Log.d("LogRepository", "Haetaan käyttäjällä: ${SharedPreferencesManager.getUser(encryptedPrefs)}")

            android.util.Log.d("LogRepository", "Userid: $userId")

            val response = service.getLogsByDateAndUser(
                date.toString(),
                userId ?: BuildConfig.APP_UUID,
                "Bearer $token"
            )
            if (response.isSuccessful && response.body() != null) Result.success(response.body())
            else Result.fail(response.code())
        } catch (e: Exception) {
            android.util.Log.d("LogRepository", "Error: ${e.localizedMessage}")
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }

    suspend fun saveLog(log: Log): Result<Boolean> {
        val token: String? = SharedPreferencesManager.getAuthToken(encryptedPrefs)

        return try {
            val response = service.saveLog(log, "Bearer $token")
            if (response.isSuccessful) Result.success(true)
            else Result.fail(response.code())
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }

    suspend fun deleteLog(id: String): Result<Boolean> {
        val token: String? = SharedPreferencesManager.getAuthToken(encryptedPrefs)

        return try {
            val response = service.deleteLog(id, "Bearer $token")
            if (response.isSuccessful) Result.success(true)
            else Result.fail(response.code())
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }

    suspend fun updateLog(log: Log): Result<Log> {
        val token: String? = SharedPreferencesManager.getAuthToken(encryptedPrefs)

        return try {
            if (log.id != null) {
                val response = service.updateLog(log.id, log, "Bearer $token")
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