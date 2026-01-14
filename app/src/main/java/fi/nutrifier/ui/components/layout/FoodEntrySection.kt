package fi.nutrifier.ui.components.layout

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import fi.nutrifier.models.database.FoodEntry
import fi.nutrifier.ui.components.misc.ItemDivider
import fi.nutrifier.viewmodels.ViewModelWrapper

@Composable
fun FoodEntrySection(
    title: String,
    foodEntries: List<FoodEntry>,
    viewModels: ViewModelWrapper,
) {
    TitledContainer(title) {
        if (foodEntries.isEmpty()) {
            Text(
                text = "No foods logged.",
                style = TextStyle(color = MaterialTheme.colorScheme.outline)
            )
        } else {
            foodEntries.forEachIndexed { index, log ->
                FoodEntryRow(foodEntry = log, viewModel = viewModels.foodEntry)
                if (index < foodEntries.size - 1) ItemDivider()
            }
        }
    }
}