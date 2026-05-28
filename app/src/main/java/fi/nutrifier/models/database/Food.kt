package fi.nutrifier.models.database

data class Food(
    val name: String,
    val brand: String? = null,
    val barcode: String,
    val servingSize: Int,
    val calories: Double,
    val carbs: Double,
    val protein: Double,
    val fat: Double,
    val createdBy: String,
    val editedBy: String,
    val created: String? = null,
    val edited: String? = null,
    val id: String? = null,
    val fineliId: Int? = null,
)