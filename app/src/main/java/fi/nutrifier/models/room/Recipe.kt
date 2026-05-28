package fi.nutrifier.models.room

import fi.nutrifier.models.database.RecipeResponse

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
    val servings: Double,
    val ingredients: List<RecipeIngredient>,
    val instructions: List<RecipeInstruction>,
    val nutrition: RecipeNutrition?,
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
