package fi.nutrifier.utils

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.round

object FormattingUtils {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    fun toLowerCaseCapitalizeFirst(str: String): String {
        return str[0].uppercase() + str.substring(1).lowercase()
    }

    fun formatLocalTimeToStringExcludeNanoSeconds(time: LocalTime): String {
        val split = time.toString().split(".") // Excluding nanoseconds
        return split[0]
    }

    fun localDateToString(date: LocalDate?): String? {
        return date?.format(formatter)
    }

    fun formatDateStr(dateStr: String?): String? {
        if (dateStr == null) return dateStr
        return LocalDate.parse(dateStr).format(formatter)
    }

    fun stringToDouble(string: String): Double {
        val convertedCommas = string.replace(",", ".")
        return convertedCommas.toDouble()
    }

    fun roundUp(value: Double?): Double {
        if (value == null) return 0.0
        return round(value * 10) / 10
    }

    fun formatNumberAndRound(value: Double?): Double {
        if (value == null) return 0.0

        return when {
            value < 0.1 -> round(value * 100) / 100     // Round to 1 decimal
            value < 10 -> round(value * 10) / 10        // 1 decimal Integer
            value <= 100 -> round(value)                // integer
            else -> round(value / 5) * 5                // nearest 5
        }
    }

    fun generateEnergyString(
        energy: Double,
        energyUnit: Enums.EnergyUnit?
    ): String {
        val convertedEnergy = ConversionUtils.convertEnergy(energy, energyUnit)
        return "${convertedEnergy.toInt()} ${energyUnit?.displayName ?: "kcal"}"
    }

    fun generateMacroString(fats: Double, carbs: Double, protein: Double): String {
        val convertedFats = ConversionUtils.convertMacroWeight(fats, Enums.MacroWeightUnit.GRAMS)
        val convertedCarbs = ConversionUtils.convertMacroWeight(carbs, Enums.MacroWeightUnit.GRAMS)
        val convertedProtein = ConversionUtils.convertMacroWeight(protein, Enums.MacroWeightUnit.GRAMS)

        // Show decimals for oz, otherwise most values would round to 0 or 1
        return "${convertedFats.toInt()}/${convertedCarbs.toInt()}/${convertedProtein.toInt()} g"
    }

    fun generateEnergyMacroString(
        energy: Double,
        fats: Double,
        carbs: Double,
        protein: Double,
        energyUnit: Enums.EnergyUnit?,
    ): String {
        return "${generateEnergyString(energy, energyUnit)}  ${generateMacroString(fats, carbs, protein)}"
    }

    fun formatDateLabel(
        date: LocalDate,
        showRelativeDates: Boolean = true,
        showWeekday: Boolean = true
    ): String {
        val today = LocalDate.now()
        return if (showRelativeDates && date == today) {
            "Today"
        } else if (showRelativeDates && date == today.minusDays(1)) {
            "Yesterday"
        } else if (showRelativeDates && date == today.plusDays(1)) {
            "Tomorrow"
        } else {
            val formatter = if (date.year == today.year) {
                DateTimeFormatter.ofPattern("dd.MM.")
            } else {
                DateTimeFormatter.ofPattern("dd.MM.yyyy")
            }
            "${if (showWeekday) "${Enums.Weekday.fromIndex(date.dayOfWeek.value)?.displayName}  " else ""}${date.format(formatter)}"
        }
    }

    fun removeTrailingZero(value: Double): String {
        return if (ValidatorUtils.hasFraction(value)) value.toString() else value.toInt().toString()
    }

    fun cardinalToOrdinal(number: Int): String {
        val numberStr = number.toString()
        val lastNumberChar = numberStr.last()

        return when (lastNumberChar) {
            '1' -> numberStr + "st"
            '2' -> numberStr + "nd"
            '3' -> numberStr + "rd"
            else -> numberStr + "th"
        }
    }
}