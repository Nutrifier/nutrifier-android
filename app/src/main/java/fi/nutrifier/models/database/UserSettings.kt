package fi.nutrifier.models.database

import fi.nutrifier.models.other.Role

data class UserSettings(
    val id: String,
    val email: String,
    val role: Role,
    val settings: UserSettings,
)