package fi.nutrifier.models.database

import fi.nutrifier.models.room.FavouriteRecipe
import fi.nutrifier.models.room.PersonalRecipe
import fi.nutrifier.models.room.RecipeIngredient
import fi.nutrifier.models.room.RecipeInstruction
import fi.nutrifier.models.room.RecipeNutrition

data class RecipeResponse(
    val id: Int,
    val title: String,
    val image: String,
    val servings: Double,
    val ingredients: List<RecipeIngredient>,
    val instructions: List<RecipeInstruction>,
    val nutrition: RecipeNutrition?,
    val isPersonalRecipe: Boolean = false,
)
