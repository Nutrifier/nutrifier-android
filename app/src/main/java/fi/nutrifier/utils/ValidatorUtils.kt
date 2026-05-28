package fi.nutrifier.utils

import android.util.Log
import fi.nutrifier.models.database.DailySummary
import java.time.LocalDate

object ValidatorUtils {
    fun recipeTitle(title: String): Boolean {
        return title.length > 2
    }
    fun recipeServings(servings: Double): Boolean {
        return servings in 1.0..40.0
    }
    fun ingredientName(name: String): Boolean {
        return name.length > 2
    }
    fun ingredientAmount(amount: Double): Boolean {
        return amount in 1.0..999.9
    }

    fun hasFraction(value: Double): Boolean {
        return value % 1.0 != 0.0
    }

    fun endDateIsAfterStartDate(startDate: LocalDate, endDate: LocalDate): Boolean {
        return endDate.isAfter(startDate)
    }

    fun startDateIsBeforeEndDate(startDate: LocalDate, endDate: LocalDate): Boolean {
        return startDate.isBefore(endDate)
    }

    fun isDateInPast(localDate: LocalDate?): Boolean {
        if (localDate == null) return true
        return localDate.isBefore(LocalDate.now())
    }
}