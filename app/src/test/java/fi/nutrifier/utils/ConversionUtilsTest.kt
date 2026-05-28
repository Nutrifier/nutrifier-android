package fi.nutrifier.utils

import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneOffset

class ConversionUtilsTest {

    @Test
    fun `convertToFraction returns correct values`() {
        val fractions1 = ConversionUtils.convertToFraction(12.25)
        val fractions2 = ConversionUtils.convertToFraction(12.5)
        val fractions3 = ConversionUtils.convertToFraction(12.75)
        val fractions4 = ConversionUtils.convertToFraction(12.33)
        val fractions5 = ConversionUtils.convertToFraction(12.66)
        val fractions6 = ConversionUtils.convertToFraction(120.66)
        val fractions7 = ConversionUtils.convertToFraction(120.0)
        val fractions8 = ConversionUtils.convertToFraction(0.35)
        val fractions9 = ConversionUtils.convertToFraction(12.8)
        val fractions10 = ConversionUtils.convertToFraction(12.1553)

        assertEquals("[12, 1/4]", fractions1.toString())
        assertEquals("[12, 1/2]", fractions2.toString())
        assertEquals("[12, 3/4]", fractions3.toString())
        assertEquals("[12, 1/3]", fractions4.toString())
        assertEquals("[12, 2/3]", fractions5.toString())
        assertEquals("[121]", fractions6.toString())
        assertEquals("[120]", fractions7.toString())
        assertEquals("[1/3]", fractions8.toString())
        assertEquals("[12.8]", fractions9.toString())
        assertEquals("[12.1553]", fractions10.toString())
    }

    @Test
    fun `calculate pev returns correct value`() {
        val actualPev = ConversionUtils.calculatePev(90.0, 0.7)
        assertEquals(3.11, actualPev, 0.001)
    }

    @Test
    fun `calculate pev returns zero with false values`() {
        val actualPev1 = ConversionUtils.calculatePev(90.0, null)
        val actualPev2 = ConversionUtils.calculatePev(90.0, 0.0)
        val actualPev3 = ConversionUtils.calculatePev(0.0, 1.4)

        assertEquals(0.0, actualPev1, 0.001)
        assertEquals(0.0, actualPev2, 0.001)
        assertEquals(0.0, actualPev3, 0.001)
    }

    @Test
    fun `convertEnergy returns correct value`() {
        val result1 = ConversionUtils.convertEnergy(120.5675, Enums.EnergyUnit.KCAL)
        val result2 = ConversionUtils.convertEnergy(100.0, Enums.EnergyUnit.KJ)
        val result3 = ConversionUtils.convertEnergy(418.4, Enums.EnergyUnit.KJ, toKcal = true)
        val result4 = ConversionUtils.convertEnergy(418.4, Enums.EnergyUnit.KCAL, toKcal = true)
        val result5 = ConversionUtils.convertEnergy(120.5675, Enums.EnergyUnit.KCAL, roundUp = true)

        assertEquals(120.5675, result1, 0.01)
        assertEquals(418.4, result2, 0.01)
        assertEquals(100.0, result3, 0.01)
        assertEquals(418.4, result4, 0.01)
        assertEquals(120.6, result5, 0.01)
    }

    @Test
    fun `convertWeight returns correct value`() {
        val result1 = ConversionUtils.convertWeight(120.5675, Enums.FoodWeightUnit.GRAMS)
        val result2 = ConversionUtils.convertWeight(100.0, Enums.FoodWeightUnit.OUNCES)
        val result3 = ConversionUtils.convertWeight(100.0, Enums.FoodWeightUnit.POUNDS)
        val result4 = ConversionUtils.convertWeight(0.220462, Enums.FoodWeightUnit.POUNDS, toGrams = true)
        val result5 = ConversionUtils.convertWeight(0.220462, Enums.FoodWeightUnit.GRAMS, toGrams = true)
        val result6 = ConversionUtils.convertWeight(120.5675, Enums.FoodWeightUnit.GRAMS, roundUp = true)

        assertEquals(120.5675, result1, 0.01)
        assertEquals(3.5274, result2, 0.01)
        assertEquals(0.220462, result3, 0.01)
        assertEquals(100.0, result4, 0.01)
        assertEquals(0.220462, result5, 0.01)
        assertEquals(120.6, result6, 0.01)
    }

    @Test
    fun `convertMacroWeight returns correct value`() {
        val result1 = ConversionUtils.convertMacroWeight(120.5675, Enums.MacroWeightUnit.GRAMS)
        val result2 = ConversionUtils.convertMacroWeight(100.0, Enums.MacroWeightUnit.OUNCES)
        val result3 = ConversionUtils.convertMacroWeight(3.5274, Enums.MacroWeightUnit.OUNCES, toGrams = true)
        val result4 = ConversionUtils.convertMacroWeight(0.220462, Enums.MacroWeightUnit.GRAMS, toGrams = true)
        val result5 = ConversionUtils.convertMacroWeight(120.5675, Enums.MacroWeightUnit.GRAMS, roundUp = true)

        assertEquals(120.5675, result1, 0.01)
        assertEquals(3.5274, result2, 0.01)
        assertEquals(100.0, result3, 0.01)
        assertEquals(0.220462, result4, 0.01)
        assertEquals(120.6, result5, 0.01)
    }

    @Test
    fun `dateStrToMillis returns correct value`() {
        val result1 = ConversionUtils.dateStrToMillis("1970-01-01", ZoneOffset.UTC)
        val result2 = ConversionUtils.dateStrToMillis("1970-01-02", ZoneOffset.UTC)

        assertEquals(0, result1)
        assertEquals(86400000, result2)
    }

    @Test
    fun `millisToLocalDate returns correct value`() {
        val result1 = ConversionUtils.millisToLocalDate(0, ZoneOffset.UTC)
        val result2 = ConversionUtils.millisToLocalDate(86400000, ZoneOffset.UTC)
        val result3 = ConversionUtils.millisToLocalDate(null)

        assertEquals(LocalDate.parse("1970-01-01"), result1)
        assertEquals(LocalDate.parse("1970-01-02"), result2)
        assertEquals(null, result3)
    }
}
