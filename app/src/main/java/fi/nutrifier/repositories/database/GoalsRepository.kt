package fi.nutrifier.repositories.database

import android.content.SharedPreferences
import android.util.Log
import fi.nutrifier.models.database.Goal
import fi.nutrifier.services.database.RetrofitInstance
import fi.nutrifier.utils.Result
import fi.nutrifier.utils.SharedPreferencesManager

class GoalsRepository(private val encryptedPrefs: SharedPreferences) {
    private val service = RetrofitInstance().goalsService

    suspend fun getGoals(): Result<Goal> {
        val savedToken: String? = SharedPreferencesManager.getAuthToken(encryptedPrefs)

        if (savedToken == null) {
            Log.d("UserGoalsRepository", "Token not found")
            return Result.fail(403, "Trying to access without a token")
        }

        return try {
            val response = service.getGoals("Bearer $savedToken")
            if (response.isSuccessful) {
                Result.success(response.body())
            }
            else Result.fail(response.code())
        } catch (e: Exception) {
            Log.d("UserGoalsRepository", "error: $e")

            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }

    suspend fun updateGoals(updatedGoal: Goal): Result<Goal> {
        val savedToken: String? = SharedPreferencesManager.getAuthToken(encryptedPrefs)

        if (savedToken == null) return Result.fail(403, "Trying to access without a token")

        return try {
            Log.d("UserGoalsRepository", "updating goals: $updatedGoal token: $savedToken")

            val response = service.updateGoals(updatedGoal,"Bearer $savedToken")
            if (response.isSuccessful) {
                Result.success(response.body())
            }
            else Result.fail(response.code())
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }

    suspend fun calculate(): Result<Goal> {
        val savedToken: String? = SharedPreferencesManager.getAuthToken(encryptedPrefs)

        if (savedToken == null) return Result.fail(403, "Trying to access without a token")

        return try {
            Log.d("UserGoalsRepository", "recalculating goals, token: $savedToken")

            val response = service.calculateGoals("Bearer $savedToken")
            if (response.isSuccessful) {
                Result.success(response.body())
            }
            else Result.fail(response.code())
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }
}
