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
    carbs: Double,
    protein: Double,
    fat: Double,
) {
    FlowRow(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        NutrientColumn(
            "Fat",
            fat,
            "g"
        )
        NutrientColumn(
            "Carbs",
            carbs,
            "g"
        )
        NutrientColumn(
            "Protein",
            protein,
            "g"
        )
    }
}