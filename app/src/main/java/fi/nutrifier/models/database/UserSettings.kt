package fi.nutrifier.models.database

import fi.nutrifier.utils.Constants

data class UserSettings(
    val weightUnit: Constants.WeightUnit,
    val macroWeightUnit: Constants.MacroWeightUnit,
    val energyUnit: Constants.EnergyUnit,
    val nutrientDisplayMode: Constants.NutrientDisplayMode,
    val language: Constants.Language,
    val timeBetweenMeals: Int,
    val diet: Constants.Diet,
    val weekStartsOn: Int,
    val proteinEfficiencyEnabled: Boolean,
    val mealReminderEnabled: Boolean,
    val weighInReminderEnabled: Boolean,
    val motivationMessagesEnabled: Boolean,
)