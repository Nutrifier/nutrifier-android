package fi.nutrifier.services.database

import fi.nutrifier.models.database.AuthRequest
import fi.nutrifier.models.database.AuthResponse
import fi.nutrifier.models.database.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {
    @POST("api/v1/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<AuthResponse>

    @POST("api/v1/login")
    suspend fun login(@Body authRequest: AuthRequest): Response<AuthResponse>

    @POST("api/v1/validate")
    suspend fun validate(@Header("Authorization") authHeader: String): Response<Unit>
}