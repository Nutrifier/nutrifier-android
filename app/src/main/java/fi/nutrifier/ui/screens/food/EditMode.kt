package fi.nutrifier.ui.screens.food

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fi.nutrifier.models.database.NutrientSummary
import fi.nutrifier.ui.components.inputs.NutrientInputRow
import fi.nutrifier.ui.components.inputs.NutrientTextField
import fi.nutrifier.ui.components.layout.NutrientColumn
import fi.nutrifier.ui.components.layout.TitledContainer
import fi.nutrifier.viewmodels.LogsScreenViewModel

@Composable
internal fun EditMode(viewModel: LogsScreenViewModel) {
    var calculatedNutrientSummary by remember { mutableStateOf(NutrientSummary(0.0, 0.0, 0.0, 0.0)) }

    LaunchedEffect(viewModel.currentAmount) {
        if (viewModel.selectedFood != null && viewModel.currentAmount.isNotEmpty()) {
            val multiplier = (viewModel.currentAmount.toDouble() / 100)

            val calories = viewModel.selectedFood!!.food!!.calories * multiplier
            val fats = viewModel.selectedFood!!.food!!.fat * multiplier
            val carbs = viewModel.selectedFood!!.food!!.carbs * multiplier
            val protein = viewModel.selectedFood!!.food!!.protein * multiplier

            calculatedNutrientSummary = NutrientSummary(calories, fats, carbs, protein)
        }
    }

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                "${viewModel.selectedFood?.food?.name}",
                style = MaterialTheme.typography.headlineLarge
            )
            // TODO: Add functionality to request update
            // TODO: Add functionality to request delete
        }
        Text(
            text = "${viewModel.selectedFood?.food?.barcode}",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline,
        )
        Spacer(modifier = Modifier.padding(vertical = 24.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            NutrientColumn(
                nutrient = "Calories",
                value = calculatedNutrientSummary.calories,
                suffix = "kcal",
            )
            NutrientTextField(value = viewModel.currentAmount) {
                viewModel.setCurrentAmount(it)
            }
        }
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        Row {
            NutrientColumn(
                nutrient = "Carbs",
                value = calculatedNutrientSummary.carbs,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            NutrientColumn(
                nutrient = "Protein",
                value = calculatedNutrientSummary.protein,
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            NutrientColumn(
                nutrient = "Fat",
                value = calculatedNutrientSummary.fats,
                style = MaterialTheme.typography.titleLarge,
            )
        }
        Spacer(modifier = Modifier.padding(vertical = 24.dp))
        TitledContainer(
            title = "Nutrients per 100 g",
            titleSize = MaterialTheme.typography.headlineSmall,
            backgroundColor = MaterialTheme.colorScheme.background,
        ) {
            Column(modifier = Modifier.padding(start = 8.dp)) {
                NutrientInputRow(
                    "Calories *",
                    viewModel.selectedFood?.food?.calories.toString(),
                    120.dp,
                    "kcal",
                    editable = false
                )
                Spacer(Modifier.padding(vertical = 4.dp))
                NutrientInputRow(
                    "Carbohydrates",
                    viewModel.selectedFood?.food?.carbs.toString(),
                    suffixText = "g",
                    editable = false
                )
                Spacer(Modifier.padding(vertical = 4.dp))
                NutrientInputRow(
                    "Protein",
                    viewModel.selectedFood?.food?.protein.toString(),
                    suffixText = "g",
                    editable = false
                )
                Spacer(Modifier.padding(vertical = 4.dp))
                NutrientInputRow(
                    "Fat",
                    viewModel.selectedFood?.food?.fat.toString(),
                    suffixText = "g",
                    editable = false
                )
                Spacer(Modifier.padding(vertical = 8.dp))
                Divider(color = MaterialTheme.colorScheme.surfaceVariant)
                Spacer(Modifier.padding(vertical = 8.dp))
                NutrientInputRow(
                    "PEV",
                    viewModel.selectedFood?.pev.toString(),
                    suffixText = "",
                    editable = false
                )
            }
        }
    }
}