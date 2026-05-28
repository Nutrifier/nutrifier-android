package fi.nutrifier.models.database

import fi.nutrifier.utils.Enums

data class UserSettings(
    val weightUnit: Enums.FoodWeightUnit,
    val energyUnit: Enums.EnergyUnit,
    val nutrientDisplayMode: Enums.NutrientDisplayMode,
    val language: Enums.Language,
    val timeBetweenMeals: Int,
    val diet: Enums.Diet,
    val weekStartsOn: Int,
    val proteinEfficiencyEnabled: Boolean,
    val mealReminderEnabled: Boolean,
    val weighInReminderEnabled: Boolean,
    val motivationMessagesEnabled: Boolean,
)