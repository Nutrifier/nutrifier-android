package fi.nutrifier.services.database

import fi.nutrifier.models.database.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface UserService {
    @GET("api/v1/users")
    suspend fun getUser(
        @Header("Authorization") authHeader: String
    ): Response<User>
}