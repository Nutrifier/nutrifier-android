package fi.nutrifier.models.database

import fi.nutrifier.utils.Enums

data class FoodEntry(
    val id: String? = null,
    val fineliId: Int? = null,
    val date: String,
    val time: String,
    val mealType: Enums.MealType,
    val unit: Enums.FoodWeightUnit,
    val caloriesSnapshot: Double,
    val fatSnapshot: Double,
    val carbsSnapshot: Double,
    val proteinSnapshot: Double,
    val userId: String,
    val foodId: String,
    var amount: Double,
)