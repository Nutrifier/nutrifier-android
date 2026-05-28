package fi.nutrifier.models.database

import fi.nutrifier.utils.Enums

data class User(
    val id: String,
    val email: String,
    val role: Enums.Role,
    val settings: UserSettings,
    val goal: Goal,
    val mealPlans: List<UserMealPlan>,
    val weightEntries: List<UserWeight>,
) {
    fun isAdmin(): Boolean {
        return this.role == Enums.Role.ADMIN
    }
    fun hasPremium(): Boolean {
        return this.role == Enums.Role.ADMIN || this.role == Enums.Role.PREMIUM
    }
}