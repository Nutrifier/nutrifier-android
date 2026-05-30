package fi.nutrifier.utils

import fi.nutrifier.R
import fi.nutrifier.models.database.Goal
import fi.nutrifier.models.database.UserMealPlan
import fi.nutrifier.models.database.UserProfile
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
    const val MIN_WEIGHT = 30.0
    const val MAX_WEIGHT = 300.0
    const val MIN_HEIGHT = 54.6
    const val MAX_HEIGHT = 272.0
    const val MIN_KCAL = 800.0
    const val MAX_KCAL = 6000.0
    const val MIN_PROTEIN = 0.0
    const val MAX_PROTEIN = 400.0
    const val MIN_CARBS = 0.0
    const val MAX_CARBS = 700.0
    const val MIN_FAT = 0.0
    const val MAX_FAT = 300.0
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

    val emptyGoal = Goal(
        goalType = null,
        startDate = null,
        targetDate = null,
        startWeight = null,
        targetWeight = null,
        isReached = false,
        dailyTDEE = 0.0,
        dailyCalorieBalance = 0.0,
        dailyCalorieTarget = 0.0,
        dailyFatTarget = 0.0,
        dailyCarbTarget = 0.0,
        dailyProteinTarget = 0.0
    )

    val emptyProfile = UserProfile(
        height = null,
        age = null,
        sex = null,
        activityLevel = null
    )

    val emptyMealPlan = UserMealPlan(
        id = null,
        name = null,
        periods = listOf(),
        createdAt = null
    )
}

