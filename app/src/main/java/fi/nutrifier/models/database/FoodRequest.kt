package fi.nutrifier.models.database

data class FoodRequest(
    val name: String,
    val brand: String,
    val category: String,
    val barcode: String,
    val servingSize: Int,
    val calories: Double,
    val carbs: Double,
    val protein: Double,
    val fat: Double,
)