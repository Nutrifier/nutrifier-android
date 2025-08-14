package fi.nutrifier.utils

/**
 * Validator for recipe title.
 */
object ValidatorUtils {
    fun recipeTitle(title: String): Boolean {
        return title.length > 2
    }
    fun recipeServings(servings: Int): Boolean {
        return servings in 1..40
    }
    fun ingredientName(name: String): Boolean {
        return name.length > 2
    }
    fun ingredientAmount(amount: Int): Boolean {
        return amount in 1..999
    }
    fun ingredientUnit(unit: String): Boolean {
        return unit in Constants.INGREDIENT_UNITS
    }
}