package fi.nutrifier.models.database

import fi.nutrifier.utils.Enums

data class RegisterRequest(
    val email: String,
    val password: String,
    val sex: Enums.Sex,
    val age: Int,
    val height: Int,
    val activityLevel: Enums.ActivityLevel,
    val goalType: Enums.GoalType,
    val currentWeight: Double,
    val targetWeight: Double,
    val targetDate: String,
)
