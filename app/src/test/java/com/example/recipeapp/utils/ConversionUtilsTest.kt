package fi.nutrifier.utils

import junit.framework.TestCase.assertEquals
import org.junit.Test

class ConversionUtilsTest {
    @Test
    fun `calculatePev returns correct value`() {
        val calories = 90.0
        val proteins = 0.7
        val expectedPev = 3.11

        val actualPev = ConversionUtils.calculatePev(calories, proteins)

        assertEquals(expectedPev, actualPev, 0.001)
    }

    @Test
    fun `calculatePev returns zero with false values`() {
        val actualPev1 = ConversionUtils.calculatePev(90.0, null)
        val actualPev2 = ConversionUtils.calculatePev(90.0, 0.0)
        val actualPev3 = ConversionUtils.calculatePev(0.0, 1.4)

        assertEquals(0.0, actualPev1, 0.001)
        assertEquals(0.0, actualPev2, 0.001)
        assertEquals(0.0, actualPev3, 0.001)
    }
}