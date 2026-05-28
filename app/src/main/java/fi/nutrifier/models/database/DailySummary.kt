package fi.nutrifier.models.database

import fi.nutrifier.utils.Enums

data class DailySummary(
    val calorieTarget: Double,
    val fatTarget: Double,
    val carbTarget: Double,
    val proteinTarget: Double,
    val confirmed: Boolean,
    val caloriesConsumed: Double,
    val fatConsumed: Double,
    val carbsConsumed: Double,
    val proteinConsumed: Double,
    val mealSummaries: Map<Enums.MealType, DailyMealSummary>
)