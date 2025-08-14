package fi.nutrifier.repositories.database

import android.util.Log
import fi.nutrifier.models.database.AuthRequest
import fi.nutrifier.models.database.AuthResponse
import fi.nutrifier.services.database.RetrofitInstance
import fi.nutrifier.utils.Result

class AuthRepository {
    private val service = RetrofitInstance().authService

    suspend fun register(authRequest: AuthRequest): Result<AuthResponse> {
        return try {
            val response = service.register(authRequest)
            if (response.isSuccessful) {
                Result.success(response.body())
            }
            else Result.fail(response.code())
        } catch (e: Exception) {
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }

    suspend fun login(authRequest: AuthRequest): Result<AuthResponse> {
        return try {
            val response = service.login(authRequest)
            Log.d("AuthRepository", "$response")
            if (response.isSuccessful) {
                Result.success(response.body())
            }
            else Result.fail(response.code())
        } catch (e: Exception) {
            Log.d("AuthRepository", "Exception in login $e")
            Result.fail(500, e.localizedMessage ?: "Unknown error occurred.")
        }
    }
}
