package fi.nutrifier.utils

import android.util.Log
import fi.nutrifier.models.room.FavouriteRecipe
import fi.nutrifier.models.database.Food
import fi.nutrifier.models.database.Nutrition
import fi.nutrifier.models.other.Fraction
import fi.nutrifier.models.room.PersonalRecipe
import fi.nutrifier.models.database.Recipe
import java.util.UUID

object ConversionUtils {
    val emptyRecipe = Recipe(-1, "", "", 1, emptyList(), emptyList(), Nutrition(emptyList()))
    val emptyFood = Food("","",0,0.0,0.0,0.0,0.0, UUID.randomUUID().toString(), UUID.randomUUID().toString())

    /**
     * Converts a [PersonalRecipe] object to a [Recipe] object.
     * @param personalRecipe The personal recipe to convert.
     * @return The converted recipe.
     */
    fun PersonalRecipe.toRecipe(): Recipe {
        return emptyRecipe.copy(
            id = this.id,
            title = this.title,
            image = this.image,
            servings = this.servings,
            ingredients = this.ingredients,
            instructions = this.instructions,
            isPersonalRecipe = true,
        )
    }

    /**
     * Converts a [FavouriteRecipe] object to a [Recipe] object.
     * @param favouriteRecipe The favourite recipe to convert.
     * @return The converted recipe.
     */
    fun FavouriteRecipe.toRecipe(): Recipe {
        return emptyRecipe.copy(
            id = id,
            title = title,
            image = image,
        )
    }

    /**
     * Converts a float number to a fraction if applicable.
     * @param number The number to convert.
     * @return The converted fraction or formatted number.
     */
    fun convertToFraction(number: Float): List<String> {
        val decimal = number - number.toInt()
        val fractionRanges = listOf(
            Fraction(0.24f..0.26f, "1/4"),
            Fraction(0.49f..0.51f, "1/2"),
            Fraction(0.74f..0.76f, "3/4"),
            Fraction(0.32f..0.34f, "1/3"),
            Fraction(0.65f..0.67f, "2/3")
        )
        if (number < 100) {
            fractionRanges.forEach {
                if (decimal in it.range) {
                    if (number.toInt() >= 1) {
                        return listOf(number.toInt().toString(), it.fraction)
                    } else {
                        return listOf(it.fraction)
                    }
                }
            }
        }
        return listOf(formatFloatToString(number))
    }

    private fun formatFloatToString(number: Float): String {
        if (number < 1) {
            return String.format("%.1f", number).replace(",", ".")
        } else {
            return String.format("%.0f", number)
        }
    }

    fun calculatePev(calories: Double?, proteins: Double?): Double {
        if (calories == 0.0) return 0.0

        if (calories !== null && proteins !== null) {
            val result = String.format("%.2f", (proteins.times(4).div(calories)).times(100))
            Log.d("ConversionUtils", "Calories: $calories proteins: $proteins result: $result")
            return result.replace(",", ".").toDouble()
        }
        return 0.0
    }

    fun convertEnergy(
        value: Double,
        energyUnit: Constants.EnergyUnit? = Constants.EnergyUnit.KCAL,
        toKcal: Boolean = false,
        roundUp: Boolean = false,
    ): Double {
        val converted = when (energyUnit) {
            Constants.EnergyUnit.KJ ->
                if (toKcal) value / Constants.KCAL_TO_KJ_MULTIPLIER
                else value * Constants.KCAL_TO_KJ_MULTIPLIER
            else -> value
        }
        return if (roundUp) FormattingUtils.roundUp(converted) else converted
    }

    // Used to only convert food entry weights
    fun convertWeight(
        value: Double,
        weightUnit: Constants.WeightUnit? = Constants.WeightUnit.G,
        toGrams: Boolean = false,
        roundUp: Boolean = false,
    ): Double {
        val converted = when (weightUnit) {
            Constants.WeightUnit.LB ->
                if (toGrams) value * Constants.GRAM_TO_LB_DIVIDER
                else value / Constants.GRAM_TO_LB_DIVIDER
            Constants.WeightUnit.OZ ->
                if (toGrams) value * Constants.GRAM_TO_OZ_DIVIDER
                else value / Constants.GRAM_TO_OZ_DIVIDER
            else -> value
        }
        return if (roundUp) FormattingUtils.roundUp(converted) else converted
    }

    fun convertMacroWeight(
        value: Double,
        weightUnit: Constants.MacroWeightUnit? = Constants.MacroWeightUnit.G,
        toGrams: Boolean = false,
        roundUp: Boolean = false,
    ): Double {
        val converted = when (weightUnit) {
            Constants.MacroWeightUnit.OZ ->
                if (toGrams) value * Constants.GRAM_TO_OZ_DIVIDER
                else value / Constants.GRAM_TO_OZ_DIVIDER
            else -> value
        }
        return if (roundUp) FormattingUtils.roundUp(converted) else converted
    }
}