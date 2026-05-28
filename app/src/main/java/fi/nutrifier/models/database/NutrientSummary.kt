package fi.nutrifier.models.database

import fi.nutrifier.utils.Enums

data class NutrientSummary(
    val energy: Map<Enums.EnergyUnit, Double>,
    val fats: Map<Enums.MacroWeightUnit, Double>,
    val carbs: Map<Enums.MacroWeightUnit, Double>,
    val protein: Map<Enums.MacroWeightUnit, Double>,
)