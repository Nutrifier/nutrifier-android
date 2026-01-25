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
import fi.nutrifier.models.database.MealType
import fi.nutrifier.utils.Constants
import fi.nutrifier.utils.ConversionUtils
import fi.nutrifier.utils.FormattingUtils
import fi.nutrifier.utils.FormattingUtils.toLowerCaseCapitalizeFirst
import fi.nutrifier.viewmodels.ViewModelWrapper

@Composable
fun MealButton(
    mealType: MealType,
    viewModels: ViewModelWrapper,
    onClick: () -> Unit,
) {
    val nutrientString = viewModels.foodEntry.nutrients.entries.find { it.key == mealType }?.value?.let {
        FormattingUtils.generateEnergyMacroString(
            energy = it.energy[viewModels.user.settings?.energyUnit ?: Constants.EnergyUnit.KCAL] ?: 0.0,
            fats = it.fats[viewModels.user.settings?.macroWeightUnit ?: Constants.MacroWeightUnit.G] ?: 0.0,
            carbs = it.carbs[viewModels.user.settings?.macroWeightUnit ?: Constants.MacroWeightUnit.G] ?: 0.0,
            protein = it.protein[viewModels.user.settings?.macroWeightUnit ?: Constants.MacroWeightUnit.G] ?: 0.0,
            userViewModel = viewModels.user,
        )
    } ?: "Loading..."

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