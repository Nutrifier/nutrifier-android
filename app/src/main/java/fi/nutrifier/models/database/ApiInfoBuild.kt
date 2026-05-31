package fi.nutrifier.models.database

data class ApiInfoBuild(
    val artifact: String,
    val name: String,
    val time: String,
    val version: String,
    val group: String,
)
