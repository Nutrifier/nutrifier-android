package fi.nutrifier.models.database

import fi.nutrifier.utils.Constants

data class User(
    val id: String,
    val email: String,
    val role: Constants.Role,
    val settings: UserSettings,
)