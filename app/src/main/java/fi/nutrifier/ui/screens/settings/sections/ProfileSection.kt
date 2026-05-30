package fi.nutrifier.ui.screens.settings.sections

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fi.nutrifier.ui.components.inputs.NutrifierDropdown
import fi.nutrifier.ui.components.misc.LabeledComponent
import fi.nutrifier.utils.Enums
import fi.nutrifier.viewmodels.ViewModelWrapper

data class ProfileItem(
    val label: String,
    val text: String,
)

@Composable
internal fun ProfileSection(viewModels: ViewModelWrapper) {
    val profileItems = listOf(
        ProfileItem("First name", "-"),
        ProfileItem("Last name", "-"),
        ProfileItem("Email", "${viewModels.user.user?.email}"),
        ProfileItem("Password", "************"),
        ProfileItem("Height (cm)", "${viewModels.profile.profile?.height}"),
        ProfileItem("Age", "${viewModels.profile.profile?.age}")
    )

    Section("Profile", "Edit your profile information.") {
        FlowRow(
            maxItemsInEachRow = 2,
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // TODO: Make email and password changeable
            profileItems.forEach {
                LabeledComponent(label = it.label) {
                    Text(it.text)
                }
            }
        }
        LabeledComponent(label = "Account type") {
            Text(viewModels.user.user?.role?.displayName ?: "-")
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            LabeledComponent(label = "Energy unit") {
                NutrifierDropdown(
                    value = viewModels.settings.settings?.energyUnit,
                    items = Enums.EnergyUnit.entries.toList(),
                    modifier = Modifier.width(100.dp),
                    labelMapper = { it.displayName }
                ) {
                    Log.d("ProfileSection", "settings ${viewModels.settings.settings.toString()}")
                    val updatedSettings = viewModels.settings.settings?.copy(
                        energyUnit = Enums.EnergyUnit.valueOf(it.name),
                    )
                    if (updatedSettings != null) viewModels.settings.updateSettings(updatedSettings)
                }
            }
            LabeledComponent(label = "Weight unit") {
                NutrifierDropdown(
                    value = viewModels.settings.settings?.weightUnit,
                    items = Enums.FoodWeightUnit.entries.toList(),
                    modifier = Modifier.width(100.dp),
                    labelMapper = { it.displayName }
                ) {
                    val updatedSettings = viewModels.settings.settings?.copy(
                        weightUnit = Enums.FoodWeightUnit.valueOf(it.name),
                    )
                    if (updatedSettings != null) viewModels.settings.updateSettings(updatedSettings)
                }
            }
            /* REDUNDANT
            LabeledComponent(label = "Macro weight unit") {
                Dropdown(
                    value = viewModels.settings.settings?.macroWeightUnit,
                    items = Enums.MacroWeightUnit.entries.toList(),
                    modifier = Modifier.width(80.dp),
                    labelMapper = { it.displayName }
                ) {
                    try {
                        val updatedSettings = viewModels.settings.settings?.copy(
                            macroWeightUnit = Enums.MacroWeightUnit.valueOf(it.name),
                        )
                        if (updatedSettings != null) viewModels.settings.updateSettings(updatedSettings)
                    } catch (e: Exception) {
                        Log.d("ProfileSection", "Tried to run valueOf for ${it.name} to Enums.MacroWeightUnit, error: $e")
                    }
                }
            }
             */
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            LabeledComponent(label = "Nutrient display mode") {
                NutrifierDropdown(
                    value = viewModels.settings.settings?.nutrientDisplayMode,
                    items = Enums.NutrientDisplayMode.entries.toList().filter { it.name != "LEGACY_CIRCLE" },
                    modifier = Modifier.width(140.dp),
                    labelMapper = { it.displayName }
                ) {
                    val updatedSettings = viewModels.settings.settings?.copy(
                        nutrientDisplayMode = Enums.NutrientDisplayMode.valueOf(it.name),
                    )
                    if (updatedSettings != null) viewModels.settings.updateSettings(updatedSettings)
                }
            }
            LabeledComponent(label = "Language") {
                NutrifierDropdown(
                    value = viewModels.settings.settings?.language,
                    items = Enums.Language.entries.toList(),
                    modifier = Modifier.width(140.dp),
                    labelMapper = { it.displayName }
                ) {
                    val updatedSettings = viewModels.settings.settings?.copy(language =
                        Enums.Language.valueOf(it.name))
                    if (updatedSettings != null) viewModels.settings.updateSettings(updatedSettings)
                }
            }
        }
    }
}
