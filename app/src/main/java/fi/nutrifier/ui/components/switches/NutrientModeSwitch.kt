package fi.nutrifier.ui.components.switches

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.PieChartOutline
import androidx.compose.material.icons.filled.Stream
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import fi.nutrifier.ui.components.misc.LabeledComponent
import fi.nutrifier.utils.Constants
import fi.nutrifier.utils.Enums
import fi.nutrifier.viewmodels.SettingsViewModel
import fi.nutrifier.viewmodels.UserSessionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun NutrientModeSwitch(settingsViewModel: SettingsViewModel) {
    val isLegacyMode = settingsViewModel.settings?.nutrientDisplayMode == Enums.NutrientDisplayMode.LEGACY_CIRCLE
    var clickCount by remember { mutableIntStateOf(0) }
    var clickTimeoutJob: Job? = null

    fun onSwitchClick() {
        clickCount++
        clickTimeoutJob?.cancel()

        clickTimeoutJob = CoroutineScope(Dispatchers.Main).launch {
            delay(200L)

            settingsViewModel.settings?.let { latestSettings ->
                val finalSettings = if (clickCount > 5) {
                    latestSettings.copy(
                        nutrientDisplayMode = Enums.NutrientDisplayMode.LEGACY_CIRCLE,
                    )
                } else {
                    latestSettings.copy(
                        nutrientDisplayMode = if (latestSettings.nutrientDisplayMode == Enums.NutrientDisplayMode.FULL_CIRCLE) {
                            Enums.NutrientDisplayMode.LINE
                        } else Enums.NutrientDisplayMode.FULL_CIRCLE
                    )
                }

                Log.d("NutrientModeSwitch", "Final mode ${finalSettings.nutrientDisplayMode}")
                settingsViewModel.updateSettings(finalSettings)
            }

            clickCount = 0
        }
    }

    val legacyColors = SwitchDefaults.colors(
        checkedTrackColor = MaterialTheme.colorScheme.primary,
        checkedThumbColor = MaterialTheme.colorScheme.primary,
        checkedIconColor = MaterialTheme.colorScheme.surface,
        checkedBorderColor = MaterialTheme.colorScheme.surface,
        uncheckedTrackColor = MaterialTheme.colorScheme.primary,
        uncheckedThumbColor = MaterialTheme.colorScheme.surface,
        uncheckedBorderColor = MaterialTheme.colorScheme.surface,
        uncheckedIconColor = MaterialTheme.colorScheme.primary,
    )

    LabeledComponent(label = "Mode:", gap = 0.dp) {
        Switch(
            modifier = Modifier.scale(0.65f).padding(0.dp),
            checked = settingsViewModel.settings?.nutrientDisplayMode == Enums.NutrientDisplayMode.FULL_CIRCLE,
            colors = if (isLegacyMode) legacyColors else {
                SwitchDefaults.colors(
                    checkedTrackColor = MaterialTheme.colorScheme.surface,
                    checkedThumbColor = MaterialTheme.colorScheme.outline,
                    checkedIconColor = MaterialTheme.colorScheme.surface,
                    checkedBorderColor = MaterialTheme.colorScheme.outline,
                    uncheckedTrackColor = MaterialTheme.colorScheme.surface,
                )
            },
            thumbContent = {
                Box(modifier = Modifier.padding(4.dp)) {
                    if (isLegacyMode) {
                        Icon(Icons.Default.Stream, "Stream")
                    }
                    else if (settingsViewModel.settings?.nutrientDisplayMode == Enums.NutrientDisplayMode.FULL_CIRCLE) {
                        Icon(Icons.Default.PieChartOutline, "Pie chart")
                    } else {
                        Icon(Icons.Default.BarChart, "Bar chart")
                    }
                }
            },
            onCheckedChange = { onSwitchClick() }
        )
    }
}
