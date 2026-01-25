package fi.nutrifier.ui.screens.settings.sections

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import fi.nutrifier.ui.components.inputs.Dropdown
import fi.nutrifier.ui.components.misc.LabeledComponent
import fi.nutrifier.utils.Constants
import fi.nutrifier.viewmodels.ViewModelWrapper
import java.util.Locale
import java.util.Locale.getDefault

@Composable
internal fun ProfileSection(viewModels: ViewModelWrapper) {
    Section("Profile", "Edit your profile information.") {
        LabeledComponent("First name:") { Text("-") } // TODO: Implement name and change
        LabeledComponent("Last name:") { Text("-") } // TODO: Implement name and change
        LabeledComponent("Email:") { Text("${viewModels.user.user?.email}") } // TODO: Implement email change
        LabeledComponent("Password:") { Text("************") } // TODO: Implement password change
        LabeledComponent("Account id:") {
            Text(
                text = viewModels.user.user?.id ?: "-",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        LabeledComponent("Account type:") {
            Text(viewModels.user.user?.role?.displayName ?: "-")
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            LabeledComponent("Energy unit:") {
                Dropdown(
                    value = viewModels.user.settings?.energyUnit,
                    items = Constants.EnergyUnit.entries.toList(),
                    modifier = Modifier.width(80.dp),
                    labelMapper = { it.displayName }
                ) {
                    Log.d("ProfileSection", "settings ${viewModels.user.settings.toString()}")
                    val updatedSettings = viewModels.user.settings?.copy(
                        energyUnit = Constants.EnergyUnit.valueOf(it.name),
                    )
                    if (updatedSettings != null) viewModels.user.updateSettings(updatedSettings)
                }
            }
            LabeledComponent("Weight unit:") {
                Dropdown(
                    value = viewModels.user.settings?.weightUnit,
                    items = Constants.WeightUnit.entries.toList(),
                    modifier = Modifier.width(80.dp),
                    labelMapper = { it.displayName }
                ) {
                    val updatedSettings = viewModels.user.settings?.copy(
                        weightUnit = Constants.WeightUnit.valueOf(it.name),
                    )
                    if (updatedSettings != null) viewModels.user.updateSettings(updatedSettings)
                }
            }
            LabeledComponent("Macro weight unit:") {
                Dropdown(
                    value = viewModels.user.settings?.macroWeightUnit,
                    items = Constants.MacroWeightUnit.entries.toList(),
                    modifier = Modifier.width(80.dp),
                    labelMapper = { it.displayName }
                ) {
                    val updatedSettings = viewModels.user.settings?.copy(
                        macroWeightUnit = Constants.MacroWeightUnit.valueOf(it.name),
                    )
                    if (updatedSettings != null) viewModels.user.updateSettings(updatedSettings)
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            LabeledComponent("Nutrient display mode:") {
                Dropdown(
                    value = viewModels.user.settings?.nutrientDisplayMode,
                    items = Constants.NutrientDisplayMode.entries.toList().filter { it.name != "LEGACY_CIRCLE" },
                    modifier = Modifier.width(120.dp),
                    labelMapper = { it.displayName }
                ) {
                    val updatedSettings = viewModels.user.settings?.copy(
                        nutrientDisplayMode = Constants.NutrientDisplayMode.valueOf(it.name),
                    )
                    if (updatedSettings != null) viewModels.user.updateSettings(updatedSettings)
                }
            }
            LabeledComponent("Language:") {
                Dropdown(
                    value = viewModels.user.settings?.language,
                    items = Constants.Language.entries.toList(),
                    modifier = Modifier.width(120.dp),
                    labelMapper = { it.displayName }
                ) {
                    val updatedSettings = viewModels.user.settings?.copy(language =
                        Constants.Language.valueOf(it.name))
                    if (updatedSettings != null) viewModels.user.updateSettings(updatedSettings)
                }
            }
        }
    }
}
