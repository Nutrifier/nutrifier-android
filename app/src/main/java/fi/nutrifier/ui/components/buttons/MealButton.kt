package fi.nutrifier.ui.components.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import fi.nutrifier.models.database.MealType
import fi.nutrifier.utils.FormattingUtils.toLowerCaseCapitalizeFirst
import fi.nutrifier.viewmodels.LogsScreenViewModel

@Composable
fun MealButton(
    mealType: MealType,
    viewModel: LogsScreenViewModel,
    onClick: () -> Unit,
) {
    val nutrientString = viewModel.nutrients.entries.find { it.key == mealType }?.value?.let {
        "${it.calories.toInt()} kcal (${it.fats.toInt()}/${it.carbs.toInt()}/${it.protein.toInt()})"
    } ?: "Loading..."

    TextButton(
        onClick = { onClick() },
        modifier = Modifier.padding(8.dp, 4.dp),
        shape = RoundedCornerShape(4.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(
                    text = toLowerCaseCapitalizeFirst(mealType.toString()),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    text = nutrientString,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline,
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowForwardIos,
                contentDescription = "Open"
            )
        }
    }
}