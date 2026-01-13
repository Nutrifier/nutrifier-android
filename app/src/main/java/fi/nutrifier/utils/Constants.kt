package fi.nutrifier.utils


import fi.nutrifier.R
import fi.nutrifier.models.other.Category

/**
 * Utility functions and constants used throughout the application.
 */
object Constants {
    val INGREDIENT_UNITS = listOf("ml", "l", "tsp", "tbsp", "mg", "g", "kg", "pinch", "piece")
    const val LANDSCAPE_ASPECT_RATIO: Float = 1.7777778f
    const val IMAGE_WIDTH: Int = 288
    const val IMAGE_HEIGHT: Int = 162
    const val IS_DEV = false

    val categories = listOf(
        Category(R.drawable.chicken, "Chicken", "chicken"),
        Category(R.drawable.beef, "Beef", "beef"),
        Category(R.drawable.pork, "Pork", "pork"),
        Category(R.drawable.fish, "Seafood", "seafood"),
        Category(R.drawable.spaghetti, "Pasta", "pasta"),
        Category(R.drawable.rice, "Rice", "rice"),
        Category(R.drawable.vegetable, "Veggies", "vegetable"),
        Category(R.drawable.fruit, "Fruit", "fruit"),
    )

    enum class Screen {
        FOOD_ADD,
        FOOD_EDIT,
        COOKBOOK,
        DISCOVER,
        LOGIN,
        LOGS,
        MEAL,
        RECIPE_EDIT,
        RECIPE_VIEW,
        SHOPPING_LIST,
        BARCODE,
        SETTINGS,
    }
}

