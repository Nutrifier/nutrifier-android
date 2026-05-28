package fi.nutrifier.ui.screens.settings.sections

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fi.nutrifier.ui.components.inputs.Dropdown
import fi.nutrifier.ui.components.inputs.NumberCounter
import fi.nutrifier.ui.components.misc.LabeledComponent
import fi.nutrifier.utils.Enums
import fi.nutrifier.viewmodels.ViewModelWrapper

@Composable
internal fun TrackingSection(viewModels: ViewModelWrapper) {
    Section("Tracking", "Set specific tracking tools and helpers.") {
        LabeledComponent(label = "Diet:") {
            Dropdown(
                value = viewModels.settings.settings?.diet,
                items = Enums.Diet.entries.toList(),
                modifier = Modifier.width(180.dp),
                labelMapper = { it.name }
            ) {
                val updatedSettings = viewModels.settings.settings?.copy(diet =
                    Enums.Diet.valueOf(it.name))
                if (updatedSettings != null) viewModels.settings.updateSettings(updatedSettings)
            }
        }
        LabeledComponent(label = "Time between meals") {
            NumberCounter(
                value = viewModels.settings.settings?.timeBetweenMeals?.toDouble() ?: 3.0,
                onNumberChange = {
                    val updatedSettings = viewModels.settings.settings?.copy(timeBetweenMeals = it.toInt())
                    if (updatedSettings != null) viewModels.settings.updateSettings(updatedSettings)
                },
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Switch(viewModels.settings.settings?.proteinEfficiencyEnabled ?: true, onCheckedChange = {
                val updatedSettings = viewModels.settings.settings?.copy(proteinEfficiencyEnabled = it)
                if (updatedSettings != null) viewModels.settings.updateSettings(updatedSettings)
            })
            Spacer(modifier = Modifier.width(8.dp))
            Text("Show protein efficiency values")
        }
    }
}
