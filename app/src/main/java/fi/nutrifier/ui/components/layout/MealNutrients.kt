package fi.nutrifier.ui.components.layout

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MealNutrients(carbs: Double, protein: Double, fat: Double) {
    Row {
        NutrientColumn("Carbs", carbs)
        Spacer(modifier = Modifier.padding(horizontal = 16.dp))
        NutrientColumn("Protein", protein)
        Spacer(modifier = Modifier.padding(horizontal = 16.dp))
        NutrientColumn("Fat", fat)
    }
}