package fi.nutrifier.ui.components.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fi.nutrifier.ui.components.layout.nutrient.NutrientColumn
import fi.nutrifier.utils.Constants
import fi.nutrifier.utils.Constants.IS_DEV
import fi.nutrifier.utils.ConversionUtils
import fi.nutrifier.utils.FormattingUtils
import fi.nutrifier.viewmodels.UserViewModel

@Composable
fun MealNutrients(
    userViewModel: UserViewModel,
    carbs: Map<Constants.MacroWeightUnit, Double>,
    protein: Map<Constants.MacroWeightUnit, Double>,
    fat: Map<Constants.MacroWeightUnit, Double>,
) {

    val convertedCarbs = carbs[userViewModel.settings?.macroWeightUnit ?: Constants.MacroWeightUnit.G] ?: 0.0
    val convertedProtein = protein[userViewModel.settings?.macroWeightUnit ?: Constants.MacroWeightUnit.G] ?: 0.0
    val convertedFat = fat[userViewModel.settings?.macroWeightUnit ?: Constants.MacroWeightUnit.G] ?: 0.0

    FlowRow(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Column {
            NutrientColumn(
                "Fat",
                convertedFat,
                userViewModel.settings?.macroWeightUnit?.displayName ?: "g"
            )
            if (IS_DEV) {
                Constants.MacroWeightUnit.entries.filter{ it != userViewModel.settings?.macroWeightUnit }.forEach { unit ->
                    Text(
                        text = "${FormattingUtils.roundUp(fat[unit])} ${unit.displayName}",
                        color = MaterialTheme.colorScheme.outline,
                    )
                }
            }
        }
        Column {
            NutrientColumn(
                "Carbs",
                convertedCarbs,
                userViewModel.settings?.macroWeightUnit?.displayName ?: "g"
            )
            if (IS_DEV) {
                Constants.MacroWeightUnit.entries.filter{ it != userViewModel.settings?.macroWeightUnit }.forEach { unit ->
                    Text(
                        text = "${FormattingUtils.roundUp(carbs[unit])} ${unit.displayName}",
                        color = MaterialTheme.colorScheme.outline,
                    )
                }
            }
        }
        Column {
            NutrientColumn(
                "Protein",
                convertedProtein,
                userViewModel.settings?.macroWeightUnit?.displayName ?: "g"
            )
            if (IS_DEV) {
                Constants.MacroWeightUnit.entries.filter{ it != userViewModel.settings?.macroWeightUnit }.forEach { unit ->
                    Text(
                        text = "${FormattingUtils.roundUp(protein[unit])} ${unit.displayName}",
                        color = MaterialTheme.colorScheme.outline,
                    )
                }
            }
        }
    }
}