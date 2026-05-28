package fi.nutrifier.ui.components.inputs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fi.nutrifier.utils.Enums
import fi.nutrifier.utils.FormattingUtils
import java.time.LocalDate
import java.time.temporal.IsoFields

@Composable
fun TimePeriodNavigator(
    selectedDate: LocalDate,
    startDateStr: String?,
    endDateStr: String?,
    timePeriod: Enums.AnalyticsTimePeriod,
    onDateChange: (LocalDate) -> Unit,
) {
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val startDate = if (startDateStr != null) LocalDate.parse(startDateStr) else null
    val endDate = if (endDateStr != null) LocalDate.parse(endDateStr) else null

    val timePeriodStr = when (timePeriod) {
        Enums.AnalyticsTimePeriod.WEEK -> "Week ${selectedDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)}"
        Enums.AnalyticsTimePeriod.MONTH -> "${FormattingUtils.toLowerCaseCapitalizeFirst(selectedDate.month.name)} ${selectedDate.year}"
        Enums.AnalyticsTimePeriod.YEAR -> "${selectedDate.year}"
    }

    val timePeriodDatesStr =  if (startDate != null && endDate != null) {
        "${FormattingUtils.formatDateLabel(
            date = startDate,
            showRelativeDates = false,
            showWeekday = false
        )} - ${FormattingUtils.formatDateLabel(
            date = endDate,
            showRelativeDates = false,
            showWeekday = false
        )}"
    } else ""

    fun handlePeriodChange(goBack: Boolean = false) {
        val nextDate = when (timePeriod) {
            Enums.AnalyticsTimePeriod.WEEK -> {
                if (goBack) {
                    selectedDate.minusWeeks(1)
                } else {
                    selectedDate.plusWeeks(1)
                }
            }
            Enums.AnalyticsTimePeriod.MONTH -> {
                if (goBack) {
                    selectedDate.minusMonths(1)
                } else {
                    selectedDate.plusMonths(1)
                }
            }
            Enums.AnalyticsTimePeriod.YEAR -> {
                if (goBack) {
                    selectedDate.minusYears(1)
                } else {
                    selectedDate.plusYears(1)
                }
            }
        }
        if (nextDate.isAfter(LocalDate.now().plusYears(1))) {
            errorMessage = "Traveling to the future is unfortunately not supported yet"
        } else if (nextDate.isBefore(LocalDate.now().minusYears(5))) {
            errorMessage = "Fetching history data past 5 years is restricted. Your data is still saved."
        } else {
            errorMessage = null
            onDateChange(nextDate)
        }
    }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            IconButton(
                onClick = { handlePeriodChange(true) },
                modifier = Modifier.size(32.dp),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                    contentDescription = "Previous",
                    modifier = Modifier.size(16.dp)
                )
            }
            TextButton(
                onClick = { },
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    containerColor = MaterialTheme.colorScheme.background,
                )
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = timePeriodStr,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    Text(
                        text = timePeriodDatesStr,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
            IconButton(
                onClick = { handlePeriodChange() },
                modifier = Modifier.size(32.dp),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = "Next",
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        if (errorMessage != null) Text(
            text = errorMessage!!,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.error
        )
    }
}
