package fi.nutrifier.ui.components.inputs

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fi.nutrifier.ui.components.misc.LabeledComponent
import fi.nutrifier.utils.FormattingUtils
import kotlinx.coroutines.delay

/**
 * A composable function that displays a number counter input.
 *
 * @param value The initial value of the counter.
 * @param onNumberChange The callback to be invoked when the number changes.
 * @param prefix The optional text to be displayed before the counter.
 * @param suffix The optional text to be displayed after the counter.
 * @param min The minimum value of the counter.
 * @param max The maximum value of the counter.
 * @param editable Whether the counter is editable or not.
 */
@Composable
fun NumberCounter(
    value: Double = 1.0,
    onNumberChange: (Double) -> Unit = {},
    label: String? = null,
    prefix: String = "",
    suffix: String = "",
    min: Double = 0.0,
    max: Double = 100.0,
    incrementAmount: Double = 1.0,
    editable: Boolean = false
) {
    val internalValue by rememberUpdatedState(value)
    var number by remember { mutableStateOf<Double?>(internalValue) }
    var text by remember(value) { mutableStateOf(FormattingUtils.removeTrailingZero(internalValue)) }
    var error by remember { mutableStateOf<String?>(null) }
    var incrementingInDirection by remember { mutableStateOf<Double?>(null) }

    val formattedMin = FormattingUtils.removeTrailingZero(min)
    val formattedMax = FormattingUtils.removeTrailingZero(max)

    val haptic = LocalHapticFeedback.current

    fun handleNumberChangeDirection(direction: Double): Boolean {
        val base: Double = number ?: min
        val newNumber = base + direction

        when {
            newNumber < min -> {
                number = min
                text = formattedMin
                error = "Minimum value is $formattedMin"
            }
            newNumber > max -> {
                number = max
                text = formattedMax
                error = "Maximum value is $formattedMax"
            }
            else -> {
                number = newNumber
                text = FormattingUtils.removeTrailingZero(newNumber)
                onNumberChange(newNumber)
                error = null
                return true
            }
        }
        return false
    }

    fun handleNumberChangeStr(numberStr: String) {
        text = numberStr

        if (numberStr.isBlank() || numberStr == "-" || numberStr.endsWith(".")) {
            number = numberStr.dropLast(1).toDoubleOrNull()
            return
        }

        val newNumber = numberStr.toDoubleOrNull() ?: return
        number = newNumber

        when {
            newNumber < min -> {
                error = "Minimum value is $formattedMin"
            }
            newNumber > max -> {
                error = "Maximum value is $formattedMax"
            }
            else -> {
                onNumberChange(newNumber)
                error = null
            }
        }
    }

    LaunchedEffect(incrementingInDirection) {
        val delays = listOf(300L, 200L, 100L, 50L)
        var index = 0

        while (incrementingInDirection != null) {
            val isChangeSuccessful = handleNumberChangeDirection(incrementingInDirection!!)
            if (isChangeSuccessful) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }
            delay(delays.getOrElse(index) { 50L })
            index++
        }
    }

    LabeledComponent(label = label, error = error) {
        NutrifierTextField(
            value = text,
            onValueChange = { handleNumberChangeStr(it) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            readOnly = !editable,
            modifier = Modifier.width(140.dp),
            centerText = true,
            leadingIcon = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(44.dp)
                        .height(44.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    incrementingInDirection = -incrementAmount
                                    tryAwaitRelease()
                                    incrementingInDirection = null
                                }
                            )
                        }
                ) {
                    Text(
                        text = "-",
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                    )
                }
            },
            trailingIcon = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(44.dp)
                        .height(44.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    incrementingInDirection = incrementAmount
                                    tryAwaitRelease()
                                    incrementingInDirection = null
                                }
                            )
                        }
                ) {
                    Text(
                        text = "+",
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        )
    }
}
