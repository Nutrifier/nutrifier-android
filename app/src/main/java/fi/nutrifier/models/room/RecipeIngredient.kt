package fi.nutrifier.models.room

import fi.nutrifier.utils.Enums

/**
 * Represents an ingredient used in a recipe.
 *
 * @property name The name of the ingredient.
 * @property unit The unit of measurement for the ingredient (e.g., "g", "ml").
 * @property amount The amount of the ingredient, typically measured in units specified by [unit].
 */
data class RecipeIngredient(
    val name: String,
    val unit: Enums.IngredientUnit,
    val amount: Float,
)