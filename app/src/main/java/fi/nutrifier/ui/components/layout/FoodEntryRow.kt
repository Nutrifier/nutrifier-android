package fi.nutrifier.ui.components.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import fi.nutrifier.models.database.Food
import fi.nutrifier.models.database.FoodEntry
import fi.nutrifier.ui.components.layout.nutrient.NutrientRow
import fi.nutrifier.viewmodels.FoodEntryViewModel

/**
 * A composable function that displays a row representing a log.
 */
@Composable
fun FoodEntryRow(foodEntry: FoodEntry, viewModel: FoodEntryViewModel) {
    var food: Food? by remember { mutableStateOf(null) }

    /*
    LaunchedEffect(foodEntry) {
        food = viewModel.fetchFoodById(foodEntry.foodId)
    }
    */

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(food?.name ?: "Loading...", style = MaterialTheme.typography.titleLarge,)
            //NutrientRow(food = food)
        }
        Row {
            Text(text = "${foodEntry.amount}")
            IconButton(onClick = { foodEntry.id?.let { viewModel.deleteFoodEntry(it) } }) {
                Icon(imageVector = Icons.Filled.Close, contentDescription = "Close")
            }
        }
    }
}
