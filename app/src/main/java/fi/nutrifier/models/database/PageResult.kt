package fi.nutrifier.models.database

data class PageResult<T>(
    val content: List<T>,
    val totalPages: Int,
    val totalElements: Int,
    val number: Int,
    val size: Int,
)
