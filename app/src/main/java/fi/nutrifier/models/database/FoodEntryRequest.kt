package fi.nutrifier.models.database

import fi.nutrifier.utils.Enums

data class FoodEntryRequest(
    var amount: Double,
    val date: String,
    val time: String,
    val mealType: Enums.MealType,
    val unit: Enums.FoodWeightUnit,
    val fineliId: Int? = null,
    val foodId: String? = null,
)