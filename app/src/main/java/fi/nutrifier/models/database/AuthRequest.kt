package fi.nutrifier.models.database

data class AuthRequest(
    val email: String,
    val password: String,
)

data class AuthResponse(
    val token: String,
    val userId: String,
    val userEmail: String,
)
