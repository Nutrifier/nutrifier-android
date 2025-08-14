package fi.nutrifier.utils

import android.util.Log
import java.time.LocalDate
import java.time.LocalTime

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
}