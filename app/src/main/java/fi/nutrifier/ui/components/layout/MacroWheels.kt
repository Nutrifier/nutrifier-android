package fi.nutrifier.ui.components.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fi.nutrifier.models.database.NutrientSummary
import fi.nutrifier.ui.components.misc.MacroWheel

@Composable
fun MacroWheels(nutrientSummary: NutrientSummary) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Box(modifier = Modifier
            .align(Alignment.CenterStart)
            .padding(start = 30.dp, top = 92.dp)
        ) {
            MacroWheel(
                value = nutrientSummary.fats,
                max = 50,
                title = "Fats",
                color = MaterialTheme.colorScheme.errorContainer,
                size = "small"
            )
        }
        Box(modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(top = 195.dp)
        ) {
            MacroWheel(
                value = nutrientSummary.carbs,
                max = 180,
                title = "Carbohydrates",
                color = MaterialTheme.colorScheme.primary,
                size = "small",
            )
        }
        Box(modifier = Modifier
            .align(Alignment.CenterEnd)
            .padding(end =30.dp, top = 92.dp)
        ) {
            MacroWheel(
                value = nutrientSummary.protein,
                max = 150,
                title = "Protein",
                color = MaterialTheme.colorScheme.tertiary,
                size = "small"
            )
        }
        Box(modifier = Modifier.align(Alignment.TopCenter)) {
            MacroWheel(
                value = nutrientSummary.calories,
                max = 1500,
                title = "Calories",
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}
