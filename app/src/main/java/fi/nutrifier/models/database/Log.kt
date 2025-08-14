package fi.nutrifier.models.database

data class Log(
    val id: String? = null,
    val date: String,
    val time: String,
    val meal: String,
    val userId: String,
    val foodId: String,
    var amount: Double,
)