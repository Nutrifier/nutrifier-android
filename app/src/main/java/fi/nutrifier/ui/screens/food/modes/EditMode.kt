package fi.nutrifier.ui.screens.food.modes

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode2
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
import fi.nutrifier.ui.components.layout.nutrient.NutrientColumn
import fi.nutrifier.ui.components.layout.nutrient.NutrientsPerUnit
import fi.nutrifier.ui.components.misc.IconTag
import fi.nutrifier.ui.components.misc.TextTag
import fi.nutrifier.utils.Constants
import fi.nutrifier.utils.Constants.IS_DEV
import fi.nutrifier.utils.ConversionUtils
import fi.nutrifier.utils.FormattingUtils
import fi.nutrifier.viewmodels.ViewModelWrapper

@Composable
internal fun EditMode(viewModels: ViewModelWrapper, foodMode: Constants.FoodMode) {
    val emptyNutrientSummary = NutrientSummary(
        energy = Constants.EnergyUnit.entries.associateWith { 0.0 },
        fats = Constants.MacroWeightUnit.entries.associateWith { 0.0 },
        carbs = Constants.MacroWeightUnit.entries.associateWith { 0.0 },
        protein = Constants.MacroWeightUnit.entries.associateWith { 0.0 },
    )
    var calculatedNutrientSummary by remember { mutableStateOf(emptyNutrientSummary) }
    val multiplier = (viewModels.foodEntry.currentAmount / 100)

    LaunchedEffect(viewModels.user.settings?.weightUnit) {
        if (foodMode == Constants.FoodMode.CREATE_ENTRY) {
            when (viewModels.user.settings?.weightUnit) {
                Constants.WeightUnit.OZ -> {
                    viewModels.foodEntry.setCurrentAmount(
                        ConversionUtils.convertWeight(1.0, Constants.WeightUnit.OZ, true)
                    )
                }
                Constants.WeightUnit.LB -> {
                    viewModels.foodEntry.setCurrentAmount(
                        ConversionUtils.convertWeight(1.0, Constants.WeightUnit.LB, true)
                    )
                }
                else -> {
                    viewModels.foodEntry.setCurrentAmount(100.0)
                }
            }
        }
    }

    LaunchedEffect(viewModels.foodEntry.currentAmount) {
        if (viewModels.foods.selectedFood != null) {
            val caloriesMapped = Constants.EnergyUnit.entries.associateWith { energyUnit ->
                ConversionUtils.convertEnergy(
                    value = viewModels.foods.selectedFood!!.food!!.calories * multiplier,
                    energyUnit = energyUnit,
                )
            }
            val fatsMapped = Constants.MacroWeightUnit.entries.associateWith { weightUnit ->
                ConversionUtils.convertMacroWeight(
                    value = viewModels.foods.selectedFood!!.food!!.fat * multiplier,
                    weightUnit = weightUnit,
                )
            }
            val carbsMapped = Constants.MacroWeightUnit.entries.associateWith { weightUnit ->
                ConversionUtils.convertMacroWeight(
                    value = viewModels.foods.selectedFood!!.food!!.carbs * multiplier,
                    weightUnit = weightUnit,
                )
            }
            val proteinMapped = Constants.MacroWeightUnit.entries.associateWith { weightUnit ->
                ConversionUtils.convertMacroWeight(
                    value = viewModels.foods.selectedFood!!.food!!.protein * multiplier,
                    weightUnit = weightUnit,
                )
            }

            calculatedNutrientSummary = NutrientSummary(caloriesMapped, fatsMapped, carbsMapped, proteinMapped)
        }
    }

    Column(modifier = Modifier
        .padding(horizontal = 16.dp)
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                "${viewModels.foods.selectedFood?.food?.name}",
                style = MaterialTheme.typography.headlineLarge
            )
            // TODO: Add functionality to request update
            // TODO: Add functionality to request delete
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            // TODO: Implement tags for foods an render them as a group
            if (viewModels.foods.selectedFood?.food?.fineliId != null) {
                TextTag("Fineli")
            }
            if (viewModels.foods.selectedFood?.food?.barcode != null
                && viewModels.foods.selectedFood?.food?.barcode?.isNotEmpty() == true
            ) {
                IconTag(imageVector = Icons.Default.QrCode2, contentDescription = "Barcode scanning")
            }
        }
        Spacer(modifier = Modifier.padding(vertical = 16.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                NutrientColumn(
                    nutrient = "Energy",
                    value = calculatedNutrientSummary.energy[viewModels.user.settings?.energyUnit ?: Constants.EnergyUnit.KCAL] ?: 0.0,
                    suffix = viewModels.user.settings?.energyUnit?.displayName ?: "kcal",
                )
                if (IS_DEV) {
                    calculatedNutrientSummary.energy.forEach { (energyUnit, value) ->
                        if (energyUnit != viewModels.user.settings?.energyUnit) {
                            Text(
                                text = "${FormattingUtils.roundUp(value)} ${energyUnit.displayName}",
                                color = MaterialTheme.colorScheme.outline,
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        Row {
            NutrientColumn(
                nutrient = "Fat",
                value = calculatedNutrientSummary.fats[viewModels.user.settings?.macroWeightUnit ?: Constants.MacroWeightUnit.G] ?: 0.0,
                style = MaterialTheme.typography.titleLarge,
                suffix = viewModels.user.settings?.macroWeightUnit?.displayName ?: "g",
                modifier = Modifier.weight(1f),
            )
            NutrientColumn(
                nutrient = "Carbs",
                value = calculatedNutrientSummary.carbs[viewModels.user.settings?.macroWeightUnit ?: Constants.MacroWeightUnit.G] ?: 0.0,
                style = MaterialTheme.typography.titleLarge,
                suffix = viewModels.user.settings?.macroWeightUnit?.displayName ?: "g",
                modifier = Modifier.weight(1f),
            )
            NutrientColumn(
                nutrient = "Protein",
                value = calculatedNutrientSummary.protein[viewModels.user.settings?.macroWeightUnit ?: Constants.MacroWeightUnit.G] ?: 0.0,
                style = MaterialTheme.typography.titleLarge,
                suffix = viewModels.user.settings?.macroWeightUnit?.displayName ?: "g",
                modifier = Modifier.weight(1f),
            )
        }
        if (IS_DEV) {
            val nutrients = listOf(
                calculatedNutrientSummary.fats,
                calculatedNutrientSummary.carbs,
                calculatedNutrientSummary.protein,
            )
            Row {
                nutrients.forEach { nutrient ->
                    Column(modifier = Modifier.weight(1f)) {
                        nutrient.forEach { (weightUnit, value) ->
                            if (weightUnit != viewModels.user.settings?.macroWeightUnit) {
                                Text(
                                    text = "${FormattingUtils.roundUp(value)} ${weightUnit.displayName}",
                                    color = MaterialTheme.colorScheme.outline,
                                )
                            }
                        }

                    }
                }
            }
        }
        Spacer(modifier = Modifier.padding(vertical = 24.dp))
        NutrientsPerUnit(viewModels)
        Spacer(modifier = Modifier.padding(vertical = 80.dp))
    }
}