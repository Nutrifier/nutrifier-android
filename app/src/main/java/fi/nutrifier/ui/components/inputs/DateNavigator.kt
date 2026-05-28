package fi.nutrifier.ui.components.inputs

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fi.nutrifier.utils.FormattingUtils
import java.time.LocalDate
import java.time.ZoneId
import kotlin.time.Instant
import kotlin.time.toJavaInstant

/**
 * A composable function that displays the selected date.
 */
@Composable
fun DateNavigator(
    selectedDate: LocalDate,
    onDateChange: (LocalDate) -> Unit,
) {
    var isDatePickerOpen by remember { mutableStateOf(false) }
    val today = LocalDate.now() // NOTE: Can be exploited by changing device's time
    val state = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli(),
        initialDisplayMode = DisplayMode.Picker,
        yearRange = today.minusYears(3).year..today.plusYears(1).year
    )

    LaunchedEffect(selectedDate) {
        Log.d("DateNavigator", "DateNavigator incoming selected date: $selectedDate")
    }

    fun changeDate() {
        val localDate: LocalDate? = state.selectedDateMillis?.let { millis ->
            Instant.fromEpochMilliseconds(millis)
                .toJavaInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        }
        if (localDate != null) onDateChange(localDate)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        IconButton(
            onClick = { onDateChange(selectedDate.minusDays(1)) },
            modifier = Modifier.size(32.dp),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                contentDescription = "Previous",
                modifier = Modifier.size(16.dp)
            )
        }
        TextButton(
            onClick = { isDatePickerOpen = !isDatePickerOpen },
            colors = ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colorScheme.onBackground,
                containerColor = MaterialTheme.colorScheme.background,
            )
        ) {
            Text(
                text = FormattingUtils.formatDateLabel(selectedDate),
                style = MaterialTheme.typography.headlineSmall,
            )
        }
        IconButton(
            onClick = { onDateChange(selectedDate.plusDays(1)) },
            modifier = Modifier.size(32.dp),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "Next",
                modifier = Modifier.size(16.dp)
            )
        }
    }
    if (isDatePickerOpen) {
        DatePickerDialog(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            onDismissRequest = { isDatePickerOpen = false },
            confirmButton = {
                TextButton(onClick = {
                    isDatePickerOpen = false
                    changeDate()
                }) {
                    Text("Select")
                }
            },
            dismissButton = {
                TextButton(onClick = { isDatePickerOpen = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(
                state = state,
                showModeToggle = true,
                title = {
                    Text("Select a date", modifier = Modifier.padding(16.dp))
                },
            )
        }
    }
}
