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
import fi.nutrifier.utils.ConversionUtils
import fi.nutrifier.utils.FormattingUtils
import fi.nutrifier.viewmodels.UserViewModel

@Composable
fun FoodEntryButton(userViewModel: UserViewModel, foodEntryFood: FoodEntryFood, onClick: () -> Unit, onDelete: () -> Unit) {
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
                        text = "${ConversionUtils.convertWeight(
                            value = foodEntryFood.foodEntry.amount,
                            weightUnit = userViewModel.settings?.weightUnit,
                        ).toInt()} ${userViewModel.settings?.weightUnit?.displayName ?: "g"}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.outline,
                    )
                }
                Column(modifier = Modifier.weight(0.3f), horizontalAlignment = Alignment.End) {
                    Text(
                        text = FormattingUtils.generateEnergyString(
                            energy = foodEntryFood.food.calories * (foodEntryFood.foodEntry.amount / 100),
                            userViewModel = userViewModel,
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        overflow = TextOverflow.Clip,
                        maxLines = 1,
                    )
                    Text(
                        text = FormattingUtils.generateMacroString(
                            fats = foodEntryFood.food.fat * (foodEntryFood.foodEntry.amount / 100),
                            carbs = foodEntryFood.food.carbs * (foodEntryFood.foodEntry.amount / 100),
                            protein = foodEntryFood.food.protein * (foodEntryFood.foodEntry.amount / 100),
                            userViewModel = userViewModel,
                        ),
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