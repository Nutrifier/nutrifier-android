package fi.nutrifier.ui.components.inputs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fi.nutrifier.utils.ConversionUtils
import fi.nutrifier.utils.FormattingUtils
import java.time.LocalDate
import java.time.ZoneOffset

@Composable
fun DatePickerButton(
    initialDateStr: String?,
    onChange: (String) -> Unit
) {
    var isDatePickerOpen by remember { mutableStateOf(false) }

    val today = LocalDate.now()
    val pickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDateStr?.let {
            ConversionUtils.dateStrToMillis(it, ZoneOffset.UTC)
        },
        initialDisplayMode = DisplayMode.Picker,
        yearRange = today.year..today.plusYears(10).year,
    )

    val dateCalculated by remember {
        derivedStateOf { ConversionUtils.millisToLocalDate(pickerState.selectedDateMillis, ZoneOffset.UTC) }
    }

    LaunchedEffect(initialDateStr) {
        val millis = initialDateStr?.let { ConversionUtils.dateStrToMillis(it, ZoneOffset.UTC) }
        if (millis != null && millis != pickerState.selectedDateMillis) {
            pickerState.selectedDateMillis = millis
        }
    }

    fun handleDateChange() {
        if (dateCalculated != null) {
            onChange(dateCalculated.toString())
        }
    }

    TextButton(
        onClick = { isDatePickerOpen = !isDatePickerOpen },
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(horizontal = 22.dp, vertical = 12.dp),
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Text(
            text = FormattingUtils.formatDateStr(dateCalculated?.toString())
                ?: "Select a date",
            color = MaterialTheme.colorScheme.onSurface
        )
    }
    if (isDatePickerOpen) {
        DatePickerDialog(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            onDismissRequest = { isDatePickerOpen = false },
            confirmButton = {
                TextButton(onClick = {
                    isDatePickerOpen = false
                    handleDateChange()
                }) {
                    Text("Select")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    isDatePickerOpen = false
                }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(
                state = pickerState,
                showModeToggle = true,
                title = { Text("Select a date", modifier = Modifier.padding(16.dp)) },
                headline = { Text(dateCalculated?.toString() ?: "No date selected", modifier = Modifier.padding(16.dp) )},
            )
        }
    }
}
