package fi.nutrifier.utils


import fi.nutrifier.R
import fi.nutrifier.models.other.Category

/**
 * Utility functions and constants used throughout the application.
 */
object Constants {
    const val LANDSCAPE_ASPECT_RATIO: Float = 1.7777778f
    const val IMAGE_WIDTH: Int = 288
    const val IMAGE_HEIGHT: Int = 162
    const val KCAL_TO_KJ_MULTIPLIER = 4.184
    const val GRAM_TO_LB_DIVIDER = 453.59237
    const val GRAM_TO_OZ_DIVIDER = 28.349523125
    const val IS_DEV = true

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
        DASHBOARD,
        LOGIN,
        FOOD_ENTRIES,
        MEAL,
        RECIPE_EDIT,
        RECIPE_VIEW,
        SHOPPING_LIST,
        BARCODE,
        SETTINGS,
    }

    enum class Role(val displayName: String) {
        ADMIN("Admin"),
        PREMIUM("Premium"),
        REGULAR("Regular"),
    }

    enum class Diet(val displayName: String) {
        STANDARD("Standard"),
        VEGETARIAN("Vegetarian"),
        VEGAN("Vegan"),
        KETO("Keto"),
        LOW_CALORIE("Low calorie"),
        HIGH_PROTEIN("High protein"),
        PALEO("Paleo"),
        INTERMITTENT_FASTING("Intermittent fasting"),
        GLUTEN_FREE("Gluten free"),
    }

    enum class EnergyUnit(val displayName: String) {
        KCAL("kcal"),
        KJ("kJ"),
    }

    enum class WeightUnit(val displayName: String) {
        G("g"),
        LB("lb"),
        OZ("oz"),
    }

    enum class MacroWeightUnit(val displayName: String) {
        G("g"),
        OZ("oz"),
    }

    enum class IngredientUnit(val displayName: String) {
        ML("ml"),
        L("l"),
        TSP("tsp"),
        TBSP("tbsp"),
        MG("mg"),
        G("g"),
        KG("kg"),
        PINCH("pinch"),
        LB("lb"),
        OZ("oz"),
    }

    enum class Language(val displayName: String) {
        EN("English"),
        FI("Finnish"),
    }

    enum class Goal(val displayName: String) {
        MAINTAIN_WEIGHT("Maintain weight"),
        LOOSE_WEIGHT("Loose weight"),
        GAIN_WEIGHT("Gain weight"),
        JUST_FOR_FUN("Just for fun!"),
    }

    enum class Weekday(val index: Int, val displayName: String) {
        MONDAY(1, "Mon"),
        TUESDAY(2, "Tue"),
        WEDNESDAY(3, "Wed"),
        THURSDAY(4, "Thu"),
        FRIDAY(5, "Fri"),
        SATURDAY(6, "Sat"),
        SUNDAY(7, "Sun");

        companion object {
            fun fromIndex(index: Int): Weekday? {
                return entries.firstOrNull { it.index == index }
            }
        }
    }

    enum class NutrientDisplayMode(val displayName: String) {
        LEGACY_CIRCLE("Legacy circle"),
        FULL_CIRCLE("Full circle"),
        LINE("Line"),
    }
}

