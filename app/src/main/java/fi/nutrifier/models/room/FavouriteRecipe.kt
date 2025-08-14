package fi.nutrifier.models.room

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity class representing a favorite recipe in the Room database.
 *
 * @property id The unique identifier for the recipe.
 * @property image The URL or local path to the image of the recipe.
 * @property title The title of the recipe.
 */
@Entity(tableName = "favourite_recipes") // Setting a custom table name
data class FavouriteRecipe(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val image: String,
    val title: String,
)
