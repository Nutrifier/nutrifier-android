package fi.nutrifier.services.database

import fi.nutrifier.models.database.ApiInfo
import fi.nutrifier.models.database.UserSettings
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH

interface SettingsService {
    @GET("api/v1/settings")
    suspend fun getSettings(
        @Header("Authorization") authHeader: String,
    ): Response<UserSettings>

    @PATCH("api/v1/settings")
    suspend fun updateSettings(
        @Body settings: UserSettings,
        @Header("Authorization") authHeader: String
    ): Response<UserSettings>

    @GET("actuator/info")
    suspend fun getApiInfo(
        @Header("Authorization") authHeader: String,
    ): Response<ApiInfo>
}