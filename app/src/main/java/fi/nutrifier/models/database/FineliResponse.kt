package fi.nutrifier.models.database

import fi.nutrifier.BuildConfig
import java.util.UUID

data class FineliName(
    val fi: String,
    val sv: String,
    val en: String,
    val la: String,
)

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
            name = this.name.en,
            barcode = "",
            servingSize = 100,
            calories = this.energyKcal,
            carbs = this.carbohydrate,
            createdBy = BuildConfig.FINELI_UUID,
            editedBy = BuildConfig.FINELI_UUID,
            fat = this.fat,
            protein = this.protein

        )
    }
}