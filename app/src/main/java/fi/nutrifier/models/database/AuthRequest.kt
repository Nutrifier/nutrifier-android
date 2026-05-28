package fi.nutrifier.models.database

data class AuthRequest(
    val email: String,
    val password: String,
)
