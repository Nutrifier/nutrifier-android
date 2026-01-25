package fi.nutrifier.ui.components.layout.nutrient

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fi.nutrifier.ui.components.inputs.NutrientInputRow
import fi.nutrifier.ui.components.layout.TitledContainer
import fi.nutrifier.utils.ConversionUtils
import fi.nutrifier.utils.FormattingUtils
import fi.nutrifier.viewmodels.ViewModelWrapper

@Composable
fun NutrientsPerUnit(viewModels: ViewModelWrapper) {
    val energy = FormattingUtils.roundUp(
        ConversionUtils.convertEnergy(
            value = viewModels.foods.selectedFood?.food?.calories ?: 0.0,
            energyUnit = viewModels.user.settings?.energyUnit,
        )
    )
    val fats = FormattingUtils.roundUp(
        ConversionUtils.convertMacroWeight(
            value = viewModels.foods.selectedFood?.food?.fat ?: 0.0,
            weightUnit = viewModels.user.settings?.macroWeightUnit,
        )
    )
    val carbs = FormattingUtils.roundUp(
        ConversionUtils.convertMacroWeight(
            value = viewModels.foods.selectedFood?.food?.carbs ?: 0.0,
            weightUnit = viewModels.user.settings?.macroWeightUnit,
        )
    )
    val protein = FormattingUtils.roundUp(
        ConversionUtils.convertMacroWeight(
            value = viewModels.foods.selectedFood?.food?.protein ?: 0.0,
            weightUnit = viewModels.user.settings?.macroWeightUnit,
        )
    )
    val pev = FormattingUtils.roundUp(viewModels.foods.selectedFood?.pev)

    TitledContainer(
        title = "Nutrients per 100 g",
        titleSize = MaterialTheme.typography.headlineSmall,
        backgroundColor = MaterialTheme.colorScheme.background,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(start = 8.dp),
        ) {
            NutrientInputRow(
                text = "Energy",
                value = energy.toString(),
                suffixText = viewModels.user.settings?.energyUnit?.displayName ?: "kcal",
                width = 120.dp,
                editable = false
            )
            NutrientInputRow(
                text = "Fat",
                value = fats.toString(),
                suffixText = viewModels.user.settings?.macroWeightUnit?.displayName ?: "g",
                editable = false
            )
            NutrientInputRow(
                text = "Carbohydrates",
                value = carbs.toString(),
                suffixText = viewModels.user.settings?.macroWeightUnit?.displayName ?: "g",
                editable = false
            )
            NutrientInputRow(
                text = "Protein",
                value = protein.toString(),
                suffixText = viewModels.user.settings?.macroWeightUnit?.displayName ?: "g",
                editable = false
            )
            if (viewModels.user.settings?.proteinEfficiencyEnabled == true) {
                HorizontalDivider(
                    Modifier,
                    DividerDefaults.Thickness,
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
                NutrientInputRow(
                    text = "PEV",
                    value = pev.toString(),
                    suffixText = "",
                    editable = false
                )
            }
        }
    }
}