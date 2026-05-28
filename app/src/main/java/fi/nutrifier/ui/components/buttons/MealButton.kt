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
import androidx.compose.ui.unit.dp
import fi.nutrifier.utils.Enums
import fi.nutrifier.utils.FormattingUtils
import fi.nutrifier.utils.FormattingUtils.toLowerCaseCapitalizeFirst
import fi.nutrifier.viewmodels.ViewModelWrapper

@Composable
fun MealButton(
    mealType: Enums.MealType,
    viewModels: ViewModelWrapper,
    onClick: () -> Unit,
) {
    // TODO: Add "Loading..." text
    val nutrientString = if (viewModels.foodEntry.summary == null) {
        "0 ${viewModels.settings.settings?.energyUnit?.displayName ?: "kcal"} 0/0/0 g"
    } else {
        viewModels.foodEntry.summary!!.mealSummaries[mealType]?.let {
            FormattingUtils.generateEnergyMacroString(
                energy = it.caloriesConsumed, // TODO: Support unit change
                fats = it.fatConsumed, // TODO: Support unit change
                carbs = it.carbsConsumed, // TODO: Support unit change
                protein = it.proteinConsumed, // TODO: Support unit change
                energyUnit = viewModels.settings.settings?.energyUnit
            )
        } ?: "-"
    }

    TextButton(
        onClick = { onClick() },
        modifier = Modifier.padding(horizontal = 8.dp),
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
                    color = if (viewModels.foodEntry.summary == null) {
                        MaterialTheme.colorScheme.outline
                    } else {
                        MaterialTheme.colorScheme.outlineVariant
                    },
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowForwardIos,
                contentDescription = "Open"
            )
        }
    }
}