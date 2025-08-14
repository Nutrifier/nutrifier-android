package fi.nutrifier.models.other

/**
 * Represents a category of recipes.
 *
 * @property drawableId The resource ID of the drawable associated with the category.
 * @property title The title of the category.
 * @property query The query string associated with the category, used for searching recipes.
 */
data class Category(
    val drawableId: Int,
    val title: String,
    val query: String,
)