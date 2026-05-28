package fi.nutrifier.ui.components.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import fi.nutrifier.ui.components.layout.nutrient.NutrientColumn
import fi.nutrifier.utils.Constants.IS_DEV
import fi.nutrifier.utils.Enums
import fi.nutrifier.utils.FormattingUtils
import fi.nutrifier.viewmodels.SettingsViewModel

@Composable
fun MealNutrients(
    settingsViewModel: SettingsViewModel,
    carbs: Map<Enums.MacroWeightUnit, Double>,
    protein: Map<Enums.MacroWeightUnit, Double>,
    fat: Map<Enums.MacroWeightUnit, Double>,
) {

    // TODO: Remove MacroWeightUnits from everywhere
    val convertedCarbs = carbs[Enums.MacroWeightUnit.GRAMS] ?: 0.0
    val convertedProtein = protein[Enums.MacroWeightUnit.GRAMS] ?: 0.0
    val convertedFat = fat[Enums.MacroWeightUnit.GRAMS] ?: 0.0

    FlowRow(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        NutrientColumn(
            "Fat",
            convertedFat,
            "g"
        )
        NutrientColumn(
            "Carbs",
            convertedCarbs,
            "g"
        )
        NutrientColumn(
            "Protein",
            convertedProtein,
            "g"
        )
    }
}