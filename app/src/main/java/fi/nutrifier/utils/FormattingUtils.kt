package fi.nutrifier.utils

import android.util.Log
import fi.nutrifier.viewmodels.UserViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.FormatStyle
import kotlin.math.round

object FormattingUtils {
    fun toLowerCaseCapitalizeFirst(str: String): String {
        return str[0] + str.substring(1).lowercase()
    }

    fun formatLocalTimeToString(time: LocalTime): String {
        val split = time.toString().split(".")
        return split[0]
    }

    fun stringToDouble(string: String): Double {
        val convertedCommas = string.replace(",", ".")
        return convertedCommas.toDouble()
    }

    fun roundUp(value: Double?): Double {
        if (value == null) return 0.0
        return round(value * 10) / 10
    }

    fun formatNumber(value: Double?): Double {
        if (value == null) return 0.0

        return when {
            value < 0.1 -> round(value * 100) / 100
            value < 1 -> round(value * 10) / 10
            else -> round(value)
        }
    }

    fun generateEnergyString(energy: Double, userViewModel: UserViewModel): String {
        val convertedEnergy = ConversionUtils.convertEnergy(energy, userViewModel.settings?.energyUnit)

        return "${convertedEnergy.toInt()} ${userViewModel.settings?.energyUnit?.displayName ?: "kcal"}"
    }

    fun generateMacroString(fats: Double, carbs: Double, protein: Double, userViewModel: UserViewModel): String {
        val convertedFats = ConversionUtils.convertMacroWeight(fats, userViewModel.settings?.macroWeightUnit)
        val convertedCarbs = ConversionUtils.convertMacroWeight(carbs, userViewModel.settings?.macroWeightUnit)
        val convertedProtein = ConversionUtils.convertMacroWeight(protein, userViewModel.settings?.macroWeightUnit)

        return "${convertedFats.toInt()}/${convertedCarbs.toInt()}/${convertedProtein.toInt()} ${userViewModel.settings?.macroWeightUnit?.displayName ?: "g"}"
    }

    fun generateEnergyMacroString(
        energy: Double,
        fats: Double,
        carbs: Double,
        protein: Double,
        userViewModel: UserViewModel,
    ): String {
        return "${generateEnergyString(energy, userViewModel)}  ${generateMacroString(fats, carbs, protein, userViewModel)}"
    }

    fun generateDateString(date: LocalDate): String {
        val today = LocalDate.now()
        return if (date == today) {
            "Today"
        } else if (date == today.minusDays(1)) {
            "Yesterday"
        } else if (date == today.plusDays(1)) {
            "Tomorrow"
        } else {
            val formatter = if (date.year == today.year) {
                DateTimeFormatter.ofPattern("dd.MM.")
            } else {
                DateTimeFormatter.ofPattern("dd.MM.yyyy")
            }
            "${Constants.Weekday.fromIndex(date.dayOfWeek.value)?.displayName}  ${date.format(formatter)}"
        }
    }
}