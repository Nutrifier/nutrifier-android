package fi.nutrifier.models.database

data class AuthResponse(
    val token: String,
    val userId: String,
    val userEmail: String,
)