package fi.nutrifier.utils

import fi.nutrifier.models.room.FavouriteRecipe
import fi.nutrifier.models.database.Food
import fi.nutrifier.models.room.RecipeNutrition
import fi.nutrifier.models.other.Fraction
import fi.nutrifier.models.room.PersonalRecipe
import fi.nutrifier.models.room.Recipe
import java.time.LocalDate
import java.time.ZoneId
import java.util.UUID
import kotlin.time.Instant
import kotlin.time.toJavaInstant

object ConversionUtils {
    val emptyRecipe = Recipe(-1, "", "", 1.0, emptyList(), emptyList(), RecipeNutrition(emptyList()))
    val emptyFood = Food("", null, "", "", 0,0.0,0.0,0.0,0.0, UUID.randomUUID().toString(), null)

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

    fun FavouriteRecipe.toRecipe(): Recipe {
        return emptyRecipe.copy(
            id = id,
            title = title,
            image = image,
        )
    }

    fun convertToFraction(number: Double): List<String> {
        val decimal = number - number.toInt()

        // Max point tolerance should not exceed 0.08
        val fractionRanges = listOf(
            Fraction(0.21..0.28, "1/4"),    // Tolerance 0.03/0.02
            Fraction(0.29..0.41, "1/3"),    // Tolerance 0.03/0.07
            Fraction(0.42..0.58, "1/2"),    // Tolerance 0.07/0.07
            Fraction(0.59..0.70, "2/3"),    // Tolerance 0.06/0.03
            Fraction(0.71..0.79, "3/4"),    // Tolerance 0.03/0.03
        )

        if (number < 100) {
            fractionRanges.forEach {
                if (decimal in it.range) {
                    return if (number.toInt() >= 1) {
                        listOf(number.toInt().toString(), it.fraction)
                    } else {
                        listOf(it.fraction)
                    }
                }
            }
            return listOf(number.toString())
        }

        return listOf(doubleToString(number))
    }

    private fun doubleToString(number: Double): String {
        return if (number < 1) {
            String.format("%.1f", number).replace(",", ".")
        } else {
            String.format("%.0f", number).replace(",", ".")
        }
    }

    private fun floatToString(number: Float): String {
        return if (number < 1) {
            String.format("%.1f", number).replace(",", ".")
        } else {
            String.format("%.0f", number).replace(",", ".")
        }
    }

    fun calculatePev(calories: Double?, proteins: Double?): Double {
        if (calories == 0.0) return 0.0

        if (calories !== null && proteins !== null) {
            val result = String.format("%.2f", (proteins.times(4).div(calories)).times(100))
            return result.replace(",", ".").toDouble()
        }
        return 0.0
    }

    fun convertEnergy(
        energy: Double,
        energyUnit: Enums.EnergyUnit? = Enums.EnergyUnit.KCAL,
        toKcal: Boolean = false,
        roundUp: Boolean = false,
    ): Double {
        val converted = when (energyUnit) {
            Enums.EnergyUnit.KJ ->
                if (toKcal) energy / Constants.KCAL_TO_KJ_MULTIPLIER
                else energy * Constants.KCAL_TO_KJ_MULTIPLIER
            else -> energy
        }
        return if (roundUp) FormattingUtils.roundUp(converted) else converted
    }

    // Used to only convert food entry weights
    fun convertWeight(
        weight: Double,
        foodWeightUnit: Enums.FoodWeightUnit? = Enums.FoodWeightUnit.GRAMS,
        toGrams: Boolean = false,
        roundUp: Boolean = false,
    ): Double {
        val converted = when (foodWeightUnit) {
            Enums.FoodWeightUnit.POUNDS ->
                if (toGrams) weight * Constants.GRAM_TO_LB_DIVIDER
                else weight / Constants.GRAM_TO_LB_DIVIDER
            Enums.FoodWeightUnit.OUNCES ->
                if (toGrams) weight * Constants.GRAM_TO_OZ_DIVIDER
                else weight / Constants.GRAM_TO_OZ_DIVIDER
            else -> weight
        }
        return if (roundUp) FormattingUtils.roundUp(converted) else converted
    }

    fun convertMacroWeight(
        value: Double,
        weightUnit: Enums.MacroWeightUnit? = Enums.MacroWeightUnit.GRAMS,
        toGrams: Boolean = false,
        roundUp: Boolean = false,
    ): Double {
        val converted = when (weightUnit) {
            Enums.MacroWeightUnit.OUNCES ->
                if (toGrams) value * Constants.GRAM_TO_OZ_DIVIDER
                else value / Constants.GRAM_TO_OZ_DIVIDER
            else -> value
        }
        return if (roundUp) FormattingUtils.roundUp(converted) else converted
    }

    fun dateStrToMillis(dateStr: String, zone: ZoneId = ZoneId.systemDefault()): Long {
        return LocalDate.parse(dateStr)
                .atStartOfDay(zone)
                .toInstant()
                .toEpochMilli()
    }

    fun millisToLocalDate(millis: Long?, zone: ZoneId = ZoneId.systemDefault()): LocalDate?  {
        return millis?.let {
            Instant.fromEpochMilliseconds(it)
                    .toJavaInstant()
                    .atZone(zone)
                    .toLocalDate()
        }
    }
}
