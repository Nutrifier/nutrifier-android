package fi.nutrifier.services.database

import fi.nutrifier.models.database.UserProfile
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH

interface ProfileService {

    @GET("api/v1/profile")
    suspend fun getProfile(
        @Header("Authorization") authHeader: String,
    ): Response<UserProfile>

    @PATCH("api/v1/profile")
    suspend fun updateProfile(
        @Body profile: UserProfile,
        @Header("Authorization") authHeader: String
    ): Response<UserProfile>
}