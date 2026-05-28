package fi.nutrifier.models.database

import fi.nutrifier.utils.Enums

data class UserProfile(
    val height: Int?,
    val age: Int?,
    val sex: Enums.Sex?,
    val activityLevel: Enums.ActivityLevel?,
)