package fi.nutrifier.models.database

data class FoodEntry(
    val id: String? = null,
    val fineliId: Int? = null,
    val date: String,
    val time: String,
    val meal: MealType,
    val userId: String,
    val foodId: String,
    var amount: Double,
)