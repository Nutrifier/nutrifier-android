package fi.nutrifier.models.database

import fi.nutrifier.BuildConfig
import java.util.UUID



data class FineliResponse(
    val id: Int,
    val name: FineliName,
    val salt: Double,
    val energyKcal: Double,
    val energy: Double,
    val fat: Double,
    val protein: Double,
    val carbohydrate: Double,
    val alcohol: Double,
    val organicAcids: Double,
    val sugarAlcohol: Double,
    val saturatedFat: Double,
    val fiber: Double,
    val sugar: Double
) {
    fun toFood(): Food {
        return Food(
            id = UUID.randomUUID().toString(),
            fineliId = this.id,
            name = this.name.fi,
            barcode = "",
            servingSize = 100,
            calories = this.energyKcal,
            carbs = this.carbohydrate,
            fat = this.fat,
            protein = this.protein
        )
    }
}