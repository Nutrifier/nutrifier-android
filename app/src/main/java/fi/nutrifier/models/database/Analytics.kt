package fi.nutrifier.models.database

import fi.nutrifier.utils.Enums

data class Analytics(
    val startOfPeriod: String,
    val endOfPeriod: String,
    val missedDayCount: Int,
    val successfulDayCount: Int,
    val failedDayCount: Int,
    val totalConsumedCalories: Double,
    val totalGoalCalories: Double,
    val totalCalorieBalance: Double,
    val avgCalorieBalance: Double,
    val dayResults: Map<String, Enums.DayGoalResult>,
    val monthResults: Map<String, Enums.DayGoalResult>
)