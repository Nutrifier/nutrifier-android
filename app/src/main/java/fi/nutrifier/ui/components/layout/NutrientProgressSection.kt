package fi.nutrifier.ui.components.layout

import android.widget.ToggleButton
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fi.nutrifier.models.database.NutrientSummary
import fi.nutrifier.ui.components.layout.nutrient.progress.indicator.NutrientProgressIndicator
import fi.nutrifier.ui.components.switches.NutrientModeSwitch
import fi.nutrifier.utils.Constants
import fi.nutrifier.viewmodels.UserViewModel

@Composable
fun NutrientProgressSection(userViewModel: UserViewModel, nutrientSummary: NutrientSummary) {
    var showMacros by remember { mutableStateOf(false) }
    val isLineMode = userViewModel.settings?.nutrientDisplayMode == Constants.NutrientDisplayMode.LINE

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopEnd,
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Box(modifier = Modifier.align(Alignment.CenterStart).padding(start = 12.dp,
                top = if (isLineMode) 132.dp else 80.dp)) {
                NutrientProgressIndicator(
                    userViewModel = userViewModel,
                    value = nutrientSummary.fats[userViewModel.settings?.macroWeightUnit ?: Constants.MacroWeightUnit.G] ?: 0.0,
                    max = 50,
                    title = "Fats",
                    color = MaterialTheme.colorScheme.errorContainer,
                    size = "small",
                    suffix = userViewModel.settings?.macroWeightUnit?.displayName ?: "g",
                )
            }
            Box(modifier = Modifier.align(Alignment.BottomCenter).padding(
                top = if (isLineMode) 132.dp else 156.dp
            )) {
                NutrientProgressIndicator(
                    userViewModel = userViewModel,
                    value = nutrientSummary.carbs[userViewModel.settings?.macroWeightUnit ?: Constants.MacroWeightUnit.G] ?: 0.0,
                    max = 180,
                    title = "Carbohydrates",
                    color = MaterialTheme.colorScheme.primary,
                    size = "small",
                    suffix = userViewModel.settings?.macroWeightUnit?.displayName ?: "g",
                )
            }
            Box(modifier = Modifier.align(Alignment.CenterEnd).padding(end = 12.dp,
                top = if (isLineMode) 132.dp else 80.dp
            )) {
                NutrientProgressIndicator(
                    userViewModel = userViewModel,
                    value = nutrientSummary.protein[userViewModel.settings?.macroWeightUnit ?: Constants.MacroWeightUnit.G] ?: 0.0,
                    max = 150,
                    title = "Protein",
                    color = MaterialTheme.colorScheme.tertiary,
                    size = "small",
                    suffix = userViewModel.settings?.macroWeightUnit?.displayName ?: "g",
                )
            }
            if (isLineMode) {
                Spacer(modifier = Modifier.size(24.dp))
            }
            Box(modifier = Modifier.align(Alignment.TopCenter)) {
                NutrientProgressIndicator(
                    userViewModel = userViewModel,
                    value = nutrientSummary.energy[userViewModel.settings?.energyUnit ?: Constants.EnergyUnit.KCAL] ?: 0.0,
                    max = 1500,
                    title = "Energy",
                    color = MaterialTheme.colorScheme.secondary,
                    suffix = userViewModel.settings?.energyUnit?.displayName ?: "kcal",
                    onClick = { showMacros = !showMacros }
                )
            }
        }
        NutrientModeSwitch(userViewModel)
    }
}
