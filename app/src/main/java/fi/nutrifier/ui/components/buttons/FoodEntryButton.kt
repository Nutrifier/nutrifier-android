package fi.nutrifier.ui.components.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fi.nutrifier.models.database.FoodEntryFood

@Composable
fun FoodEntryButton(foodEntryFood: FoodEntryFood, onClick: () -> Unit, onDelete: () -> Unit) {
    val calories = (foodEntryFood.food.calories * (foodEntryFood.foodEntry.amount / 100)).toInt()
    val carbs = (foodEntryFood.food.carbs * (foodEntryFood.foodEntry.amount / 100)).toInt()
    val protein = (foodEntryFood.food.protein * (foodEntryFood.foodEntry.amount / 100)).toInt()
    val fat = (foodEntryFood.food.fat * (foodEntryFood.foodEntry.amount / 100)).toInt()

    val calorieString = "$calories kcal"
    val nutrientString = "${carbs}/${protein}/${fat}"

    Row {
        TextButton(
            onClick = { onClick() },
            modifier = Modifier.fillMaxWidth().weight(0.9f),
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground,
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(0.7f)) {
                    Text(
                        text = foodEntryFood.food.name.ifEmpty { "[Deleted Food]" },
                        style = MaterialTheme.typography.titleMedium,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )
                    Text(
                        text = "${foodEntryFood.food.barcode}, ${foodEntryFood.foodEntry.amount.toInt()} g",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.outline,
                    )
                }
                Column(modifier = Modifier.weight(0.3f), horizontalAlignment = Alignment.End) {
                    Text(
                        text = calorieString,
                        style = MaterialTheme.typography.titleMedium,
                        overflow = TextOverflow.Clip,
                        maxLines = 1,
                    )
                    Text(
                        text = nutrientString,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.outline,
                    )
                }
            }
        }
        IconButton(modifier = Modifier.weight(.05f), onClick = { onDelete() }) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "Clear",
                tint = MaterialTheme.colorScheme.outlineVariant,
            )
        }
    }
}