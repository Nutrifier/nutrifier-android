package fi.nutrifier.services.database

import fi.nutrifier.models.database.Log
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

interface LogService {
    @GET("api/logs")
    suspend fun getLogs(): Response<List<Log>>

    @GET("api/logs/by-date")
    suspend fun getLogsByDateAndUser(
        @Query("date") date: String,
        @Query("userId") userId: String,
        @Header("Authorization") authHeader: String,
    ): Response<List<Log>>

    @POST("api/logs")
    suspend fun saveLog(
        @Body log: Log,
        @Header("Authorization") authHeader: String,
    ): Response<Log>

    @PATCH("api/logs/{id}")
    suspend fun updateLog(
        @Path("id") id: String,
        @Body log: Log,
        @Header("Authorization") authHeader: String,
    ): Response<Log>

    @DELETE("api/logs/{id}")
    suspend fun deleteLog(
        @Path("id") id: String,
        @Header("Authorization") authHeader: String,
    ): Response<Void>
}