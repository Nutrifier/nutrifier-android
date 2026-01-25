package fi.nutrifier.models.database

import fi.nutrifier.utils.Constants

data class NutrientSummary(
    val energy: Map<Constants.EnergyUnit, Double>,
    val fats: Map<Constants.MacroWeightUnit, Double>,
    val carbs: Map<Constants.MacroWeightUnit, Double>,
    val protein: Map<Constants.MacroWeightUnit, Double>,
)