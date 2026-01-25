package fi.nutrifier.ui.components.layout

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fi.nutrifier.ui.components.layout.nutrient.NutrientColumn
import fi.nutrifier.utils.Constants
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

    Row {
        NutrientColumn(
            "Fat",
            convertedFat,
            userViewModel.settings?.macroWeightUnit?.displayName ?: "g"
        )
        Spacer(modifier = Modifier.padding(horizontal = 16.dp))
        NutrientColumn(
            "Carbs",
            convertedCarbs,
            userViewModel.settings?.macroWeightUnit?.displayName ?: "g"
        )
        Spacer(modifier = Modifier.padding(horizontal = 16.dp))
        NutrientColumn(
            "Protein",
            convertedProtein,
            userViewModel.settings?.macroWeightUnit?.displayName ?: "g"
        )
    }
}