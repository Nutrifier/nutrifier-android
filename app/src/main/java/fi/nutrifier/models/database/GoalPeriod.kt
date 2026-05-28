package fi.nutrifier.models.database

data class GoalPeriod(
    val periodType: String?,
    val startDate: String?,
    val endDate: String?,
    val calorieTarget: Double?,
    val fatTarget: Double?,
    val carbTarget: Double?,
    val proteinTarget: Double?,
)