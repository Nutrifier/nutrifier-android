package fi.nutrifier.models.database

import fi.nutrifier.utils.Constants

data class User(
    val id: String,
    val email: String,
    val role: Constants.Role,
    val settings: UserSettings,
) {
    fun isAdmin(): Boolean {
        return this.role == Constants.Role.ADMIN
    }
    fun hasPremium(): Boolean {
        return this.role == Constants.Role.ADMIN || this.role == Constants.Role.PREMIUM
    }
}