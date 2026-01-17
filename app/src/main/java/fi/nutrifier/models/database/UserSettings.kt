package fi.nutrifier.models.database

data class UserSettings(
    val weightUnit: String,
    val energyUnit: String,
    val language: String,
    val timeBetweenMeals: Int,
    val diet: String,
    val weekStartsOn: Int,
    val proteinEfficiencyEnabled: Boolean,
    val mealReminderEnabled: Boolean,
    val weighInReminderEnabled: Boolean,
    val motivationMessagesEnabled: Boolean,
)