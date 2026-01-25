package fi.nutrifier.services

import fi.nutrifier.models.database.AuthRequest
import fi.nutrifier.models.database.AuthResponse
import fi.nutrifier.models.database.FoodEntry
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {
    @POST("api/register")
    suspend fun register(@Body authRequest: AuthRequest): Response<AuthResponse>

    @POST("api/login")
    suspend fun login(@Body authRequest: AuthRequest): Response<AuthResponse>

    @POST("api/validate")
    suspend fun validate(@Header("Authorization") authHeader: String): Response<Unit>
}