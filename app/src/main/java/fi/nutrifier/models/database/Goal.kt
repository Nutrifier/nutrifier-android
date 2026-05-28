package fi.nutrifier.models.database

import fi.nutrifier.utils.Enums

data class Goal(
    val goalType: Enums.GoalType?,
    val startDate: String?,
    val targetDate: String?,
    val startWeight: Double?,
    val targetWeight: Double?,
    val isReached: Boolean?,
    val dailyTDEE: Double?,
    val dailyCalorieBalance: Double?,
    val dailyCalorieTarget: Double?,
    val dailyFatTarget: Double?,
    val dailyCarbTarget: Double?,
    val dailyProteinTarget: Double?
)