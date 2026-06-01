package fi.nutrifier.ui.components.layout.nutrient

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fi.nutrifier.ui.components.inputs.NutrientInputRow
import fi.nutrifier.utils.ConversionUtils
import fi.nutrifier.utils.Enums
import fi.nutrifier.utils.FormattingUtils
import fi.nutrifier.viewmodels.ViewModelWrapper

@Composable
fun NutrientsPerUnit(viewModels: ViewModelWrapper, isEditable: Boolean) {
    val energy = ConversionUtils.convertEnergy(
        energy = viewModels.foods.selectedFood?.food?.calories ?: 0.0,
        energyUnit = viewModels.settings.settings?.energyUnit,
        roundUp = true,
    )
    val fats = ConversionUtils.convertMacroWeight(
        value = viewModels.foods.selectedFood?.food?.fat ?: 0.0,
        weightUnit = Enums.MacroWeightUnit.GRAMS,
        roundUp = true,
    )
    val carbs = ConversionUtils.convertMacroWeight(
        value = viewModels.foods.selectedFood?.food?.carbs ?: 0.0,
        weightUnit = Enums.MacroWeightUnit.GRAMS,
        roundUp = true,
    )
    val protein = ConversionUtils.convertMacroWeight(
        value = viewModels.foods.selectedFood?.food?.protein ?: 0.0,
        weightUnit = Enums.MacroWeightUnit.GRAMS,
        roundUp = true,
    )
    val pev = FormattingUtils.roundUp(viewModels.foods.selectedFood?.pev)

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(start = 8.dp).fillMaxWidth(),
    ) {
        Text(
            text = "Nutrients per 100g",
            style = MaterialTheme.typography.headlineSmall,
        )
        Spacer(modifier = Modifier.height(8.dp))
        NutrientInputRow(
            text = "Energy",
            value = energy.toString(),
            suffixText = viewModels.settings.settings?.energyUnit?.displayName ?: "kcal",
            width = 120.dp,
            editable = isEditable,
            onChange = { viewModels.foods.setSelectedFood(
                viewModels.foods.selectedFood?.food?.copy(calories = it.toDouble())
            ) }
        )
        NutrientInputRow(
            text = "Fat",
            value = fats.toString(),
            suffixText = "g",
            editable = isEditable,
            onChange = { viewModels.foods.setSelectedFood(
                viewModels.foods.selectedFood?.food?.copy(fat = it.toDouble())
            ) }
        )
        NutrientInputRow(
            text = "Carbohydrates",
            value = carbs.toString(),
            suffixText = "g",
            editable = isEditable,
            onChange = { viewModels.foods.setSelectedFood(
                viewModels.foods.selectedFood?.food?.copy(carbs = it.toDouble())
            ) }
        )
        NutrientInputRow(
            text = "Protein",
            value = protein.toString(),
            suffixText = "g",
            editable = isEditable,
            onChange = { viewModels.foods.setSelectedFood(
                viewModels.foods.selectedFood?.food?.copy(protein = it.toDouble())
            ) }
        )
        if (viewModels.settings.settings?.proteinEfficiencyEnabled == true) {
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(
                Modifier,
                DividerDefaults.Thickness,
                color = MaterialTheme.colorScheme.surfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            NutrientInputRow(
                text = "PEV",
                value = pev.toString(),
                suffixText = "",
                editable = false
            )
        }
    }
}
