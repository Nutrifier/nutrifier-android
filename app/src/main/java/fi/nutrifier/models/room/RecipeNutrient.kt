package fi.nutrifier.models.room

/**
 * Represents a nutrient or nutritional value for a food item.
 *
 * @property name The name of the nutrient (e.g., "Protein", "Vitamin C").
 * @property amount The quantity of the nutrient present.
 * @property unit The unit of measurement for [amount] (e.g., "g", "mg", "IU").
 * @property percentOfDailyNeeds The percentage of the recommended daily intake that this nutrient provides.
 */
data class RecipeNutrient(
    val name: String,
    val amount: Float,
    val unit: String,
    val percentOfDailyNeeds: Float,
)
