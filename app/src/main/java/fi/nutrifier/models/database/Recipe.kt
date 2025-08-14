package fi.nutrifier.models.database

import fi.nutrifier.BuildConfig
import fi.nutrifier.models.room.FavouriteRecipe
import fi.nutrifier.models.room.PersonalRecipe
import java.util.UUID

/**
 * Represents a recipe, which can be either a personal recipe or a favorite recipe.
 *
 * @property id The unique identifier of the recipe.
 * @property title The title of the recipe.
 * @property image The URL or local path to the image of the recipe.
 * @property servings The number of servings the recipe yields.
 * @property ingredients The list of ingredients required for the recipe.
 * @property instructions The list of instructions to prepare the recipe.
 * @property isPersonalRecipe Flag indicating whether the recipe is a personal recipe.
 */
data class Recipe(
    val id: Int,
    val title: String,
    val image: String,
    val servings: Int,
    val ingredients: List<Ingredient>,
    val instructions: List<Instruction>,
    val isPersonalRecipe: Boolean = false,
){
    fun toPersonal(): PersonalRecipe {
        return if (id == -1) {
            PersonalRecipe(title, image, servings, ingredients, instructions)
        } else {
            PersonalRecipe(title, image, servings, ingredients, instructions, id)
        }
    }
    fun toFavourite() = FavouriteRecipe(id, image, title)
}

/**
 * Represents an ingredient used in a recipe.
 *
 * @property name The name of the ingredient.
 * @property unit The unit of measurement for the ingredient (e.g., "g", "ml").
 * @property amount The amount of the ingredient, typically measured in units specified by [unit].
 */
data class Ingredient(
    val name: String,
    val unit: String,
    val amount: Float,
)

/**
 * Represents an instruction step in a recipe.
 *
 * @property number The sequential number of the instruction step.
 * @property text The text describing the instruction.
 */
data class Instruction(
    val number: Int,
    val text: String,
)