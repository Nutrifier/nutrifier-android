package fi.nutrifier.services.database

import fi.nutrifier.models.database.Food
import fi.nutrifier.models.database.User
import fi.nutrifier.models.database.UserSettings
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH

interface UserService {
    @GET("api/users")
    suspend fun getUser(
        @Header("Authorization") authHeader: String
    ): Response<User>

    @PATCH("api/users/settings")
    suspend fun updateSettings(
        @Body settings: UserSettings,
        @Header("Authorization") authHeader: String
    ): Response<UserSettings>
}