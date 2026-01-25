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
import fi.nutrifier.utils.Constants
import fi.nutrifier.viewmodels.ViewModelWrapper

@Composable
internal fun TrackingSection(viewModels: ViewModelWrapper) {
    Section("Tracking", "Set specific tracking tools and helpers.") {
        LabeledComponent("Diet:") {
            Dropdown(
                value = viewModels.user.settings?.diet,
                items = Constants.Diet.entries.toList(),
                modifier = Modifier.width(180.dp),
                labelMapper = { it.displayName }
            ) {
                val updatedSettings = viewModels.user.settings?.copy(diet =
                    Constants.Diet.valueOf(it.name))
                if (updatedSettings != null) viewModels.user.updateSettings(updatedSettings)
            }
        }
        LabeledComponent("Time between meals") {
            NumberCounter(
                value = viewModels.user.settings?.timeBetweenMeals ?: 3,
                onNumberChange = {
                    val updatedSettings = viewModels.user.settings?.copy(timeBetweenMeals = it.toInt())
                    if (updatedSettings != null) viewModels.user.updateSettings(updatedSettings)
                },
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Switch(viewModels.user.settings?.proteinEfficiencyEnabled ?: true, onCheckedChange = {
                val updatedSettings = viewModels.user.settings?.copy(proteinEfficiencyEnabled = it)
                if (updatedSettings != null) viewModels.user.updateSettings(updatedSettings)
            })
            Spacer(modifier = Modifier.width(8.dp))
            Text("Show protein efficiency values")
        }
    }
}