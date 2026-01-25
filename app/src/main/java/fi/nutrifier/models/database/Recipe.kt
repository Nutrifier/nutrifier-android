package fi.nutrifier.models.database

import fi.nutrifier.models.room.FavouriteRecipe
import fi.nutrifier.models.room.PersonalRecipe

/**
 * Represents a recipe, which can be either a personal recipe or a favorite recipe.
 *
 * @property id The unique identifier of the recipe.
 * @property title The title of the recipe.
 * @property image The URL or local path to the image of the recipe.
 * @property servings The number of servings the recipe yields.
 * @property ingredients The list of ingredients required for the recipe.
 * @property instructions The list of instructions to prepare the recipe.
 * @property nutrition The list of nutritional information about the recipe.
 * @property isPersonalRecipe Flag indicating whether the recipe is a personal recipe.
 */
data class Recipe(
    val id: Int,
    val title: String,
    val image: String,
    val servings: Int,
    val ingredients: List<Ingredient>,
    val instructions: List<Instruction>,
    val nutrition: Nutrition?,
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

data class Nutrition(
    val nutrients: List<Nutrient>
)

/**
 * Represents a nutrient or nutritional value for a food item.
 *
 * @property name The name of the nutrient (e.g., "Protein", "Vitamin C").
 * @property amount The quantity of the nutrient present.
 * @property unit The unit of measurement for [amount] (e.g., "g", "mg", "IU").
 * @property percentOfDailyNeeds The percentage of the recommended daily intake that this nutrient provides.
 */
data class Nutrient(
    val name: String,
    val amount: Float,
    val unit: String,
    val percentOfDailyNeeds: Float,
)