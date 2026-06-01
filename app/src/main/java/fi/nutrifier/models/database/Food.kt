package fi.nutrifier.models.database

data class Food(
    val name: String,
    val brand: String? = null,
    val category: String? = null,
    val barcode: String? = null,
    val servingSize: Int,
    val calories: Double,
    val carbs: Double,
    val protein: Double,
    val fat: Double,
    val id: String? = null,
    val fineliId: Int? = null,
)