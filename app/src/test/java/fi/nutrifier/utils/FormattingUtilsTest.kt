package fi.nutrifier.utils

import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class FormattingUtilsTest {

    @Test
    fun `toLowerCaseCapitalizeFirst returns correct values`() {
        val text1 = FormattingUtils.toLowerCaseCapitalizeFirst("FULL ON CAPS")
        val text2 = FormattingUtils.toLowerCaseCapitalizeFirst("all in lower case")
        val text3 = FormattingUtils.toLowerCaseCapitalizeFirst("mIXEd caSInG")

        assertEquals("Full on caps", text1)
        assertEquals("All in lower case", text2)
        assertEquals("Mixed casing", text3)
    }

    @Test
    fun `formatLocalTimeToStringExcludeNanoSeconds returns correct values`() {
        val time1 = FormattingUtils.formatLocalTimeToStringExcludeNanoSeconds(LocalTime.of(12, 0, 0))
        val time2 = FormattingUtils.formatLocalTimeToStringExcludeNanoSeconds(LocalTime.of(23, 54, 30, 20))

        assertEquals("12:00", time1)
        assertEquals("23:54:30", time2)
    }

    @Test
    fun `localDateToString returns correct values`() {
        val date1 = FormattingUtils.localDateToString(LocalDate.of(2024, 1, 2))
        val date2 = FormattingUtils.localDateToString(LocalDate.of(2138, 7, 23))

        assertEquals("02.01.2024", date1)
        assertEquals("23.07.2138", date2)
    }

    @Test
    fun `formatDateStr returns correct values`() {
        val date1 = FormattingUtils.formatDateStr("2024-01-02")
        val date2 = FormattingUtils.formatDateStr("2138-07-23")

        assertEquals("02.01.2024", date1)
        assertEquals("23.07.2138", date2)
    }

    @Test
    fun `stringToDouble returns correct values`() {
        val double1 = FormattingUtils.stringToDouble("20.5")
        val double2 = FormattingUtils.stringToDouble("20,5")

        assertEquals(20.5, double1)
        assertEquals(20.5, double2)
    }

    @Test
    fun `roundUp returns correct values`() {
        val double1 = FormattingUtils.roundUp(20.52)
        val double2 = FormattingUtils.roundUp(20.54)
        val double3 = FormattingUtils.roundUp(20.55)
        val double4 = FormattingUtils.roundUp(20.546)
        val double5 = FormattingUtils.roundUp(null)

        assertEquals(20.5, double1)
        assertEquals(20.5, double2)
        assertEquals(20.6, double3)
        assertEquals(20.5, double4)
        assertEquals(0.0, double5)
    }

    @Test
    fun `formatNumberAndRound returns correct values`() {
        val number1 = FormattingUtils.formatNumberAndRound(0.038)
        val number2 = FormattingUtils.formatNumberAndRound(0.75)
        val number3 = FormattingUtils.formatNumberAndRound(1.55)
        val number4 = FormattingUtils.formatNumberAndRound(20.546)
        val number5 = FormattingUtils.formatNumberAndRound(457.89)
        val number6 = FormattingUtils.formatNumberAndRound(457.39)
        val number7 = FormattingUtils.formatNumberAndRound(null)

        assertEquals(0.04, number1)
        assertEquals(0.8, number2)
        assertEquals(1.6, number3)
        assertEquals(21.0, number4)
        assertEquals(460.0, number5)
        assertEquals(455.0, number6)
        assertEquals(0.0, number7)
    }

    @Test
    fun `generateEnergyString returns correct values`() {
        val energy1 = FormattingUtils.generateEnergyString(100.55, Enums.EnergyUnit.KCAL)
        val energy2 = FormattingUtils.generateEnergyString(100.0, Enums.EnergyUnit.KJ)
        val energy3 = FormattingUtils.generateEnergyString(100.0, null)

        assertEquals("100 kcal", energy1)
        assertEquals("418 kJ", energy2) // 100 kcal = 418,4 kj
        assertEquals("100 kcal", energy3)
    }

    @Test
    fun `generateMacroString returns correct values`() {
        val str1 = FormattingUtils.generateMacroString(10.0, 20.0, 30.0, Enums.MacroWeightUnit.GRAMS)
        val str2 = FormattingUtils.generateMacroString(10.0, 10.0, 10.0, Enums.MacroWeightUnit.OUNCES)
        val str3 = FormattingUtils.generateMacroString(10.0, 10.0, 10.0, null)

        assertEquals("10/20/30 g", str1)
        assertEquals("0.4/0.4/0.4 oz", str2) // 10 g = 0,35274 oz
        assertEquals("10/10/10 g", str3)
    }

    @Test
    fun `generateEnergyMacroString returns correct values`() {
        val str1 = FormattingUtils.generateEnergyMacroString(100.0, 20.0, 30.0, 40.0, Enums.EnergyUnit.KCAL, Enums.MacroWeightUnit.GRAMS)
        val str2 = FormattingUtils.generateEnergyMacroString(100.0, 10.0, 10.0, 10.0, Enums.EnergyUnit.KJ, Enums.MacroWeightUnit.OUNCES)
        val str3 = FormattingUtils.generateEnergyMacroString(100.0, 10.0, 10.0, 10.0, Enums.EnergyUnit.KJ, Enums.MacroWeightUnit.GRAMS)
        val str4 = FormattingUtils.generateEnergyMacroString(100.0, 10.0, 10.0, 10.0, Enums.EnergyUnit.KCAL, Enums.MacroWeightUnit.OUNCES)
        val str5 = FormattingUtils.generateEnergyMacroString(100.0, 10.0, 10.0, 10.0, null, null)

        // 100 kcal = 418.4 kj, 10 g = 0.35274 oz
        assertEquals("100 kcal  20/30/40 g", str1)
        assertEquals("418 kJ  0.4/0.4/0.4 oz", str2)
        assertEquals("418 kJ  10/10/10 g", str3)
        assertEquals("100 kcal  0.4/0.4/0.4 oz", str4)
        assertEquals("100 kcal  10/10/10 g", str5)
    }

    @Test
    fun `formatRelativeDateLabel returns correct values`() {
        val today = LocalDate.now()

        val todayStr = FormattingUtils.formatDateLabel(today)
        val yesterdayStr = FormattingUtils.formatDateLabel(today.minusDays(1))
        val tomorrowStr = FormattingUtils.formatDateLabel(today.plusDays(1))
        val thisYearStr = FormattingUtils.formatDateLabel(LocalDate.of(today.year, 1, 2))
        val pastYearStr = FormattingUtils.formatDateLabel(LocalDate.of(2024, 1, 2))

        assertEquals("Today", todayStr)
        assertEquals("Yesterday", yesterdayStr)
        assertEquals("Tomorrow", tomorrowStr)
        assert(thisYearStr.endsWith("02.01."))
        assertEquals("Tue  02.01.2024", pastYearStr) // 2.1.2024 = Tuesday
    }

    @Test
    fun `removeTrailingZero returns correct values`() {
        val str1 = FormattingUtils.removeTrailingZero(12.344678)
        val str2 = FormattingUtils.removeTrailingZero(24.0)

        assertEquals("12.344678", str1)
        assertEquals("24", str2)
    }

    @Test
    fun `cardinalToOrdinal returns correct values`() {
        val ordinal1 = FormattingUtils.cardinalToOrdinal(1)
        val ordinal2 = FormattingUtils.cardinalToOrdinal(32)
        val ordinal3 = FormattingUtils.cardinalToOrdinal(763)
        val ordinal4 = FormattingUtils.cardinalToOrdinal(1064)

        assertEquals("1st", ordinal1)
        assertEquals("32nd", ordinal2)
        assertEquals("763rd", ordinal3)
        assertEquals("1064th", ordinal4)
    }
}