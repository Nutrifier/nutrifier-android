package fi.nutrifier.services

import fi.nutrifier.models.database.AuthRequest
import fi.nutrifier.models.database.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("api/register")
    suspend fun register(@Body authRequest: AuthRequest): Response<AuthResponse>

    @POST("api/login")
    suspend fun login(@Body authRequest: AuthRequest): Response<AuthResponse>
}