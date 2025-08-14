package fi.nutrifier.models.room

data class ShoppingListItem(
    val name: String,
    val note: String,
    val checked: Boolean = false,
)