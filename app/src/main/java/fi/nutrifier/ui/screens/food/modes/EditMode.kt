package fi.nutrifier.ui.screens.food.modes

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastCbrt
import androidx.navigation.NavController
import fi.nutrifier.models.database.NutrientSummary
import fi.nutrifier.ui.components.dialogs.NutrifierDialog
import fi.nutrifier.ui.components.inputs.ActionButtons
import fi.nutrifier.ui.components.inputs.BarcodeTextField
import fi.nutrifier.ui.components.inputs.NutrifierTextField
import fi.nutrifier.ui.components.inputs.PreviousNextOption
import fi.nutrifier.ui.components.layout.nutrient.NutrientColumn
import fi.nutrifier.ui.components.layout.nutrient.NutrientsPerUnit
import fi.nutrifier.ui.components.misc.IconTag
import fi.nutrifier.ui.components.misc.TextTag
import fi.nutrifier.utils.Constants.IS_DEV
import fi.nutrifier.utils.ConversionUtils
import fi.nutrifier.utils.Enums
import fi.nutrifier.utils.FormattingUtils
import fi.nutrifier.viewmodels.ViewModelWrapper

@Composable
internal fun EditMode(
    navController: NavController,
    viewModels: ViewModelWrapper,
    foodMode: Enums.FoodMode,
    barcode: String?
) {
    val emptyNutrientSummary = NutrientSummary(
        energy = Enums.EnergyUnit.entries.associateWith { 0.0 },
        fats = Enums.MacroWeightUnit.entries.associateWith { 0.0 },
        carbs = Enums.MacroWeightUnit.entries.associateWith { 0.0 },
        protein = Enums.MacroWeightUnit.entries.associateWith { 0.0 },
    )
    var newBarcode by remember { mutableStateOf(barcode) }
    var isBarcodeDialogOpen by remember { mutableStateOf(barcode != null && !barcode.isEmpty()) }
    var calculatedNutrientSummary by remember { mutableStateOf(emptyNutrientSummary) }
    var isEditable by remember { mutableStateOf(false) }
    val multiplier = (viewModels.foodEntry.currentAmount / 100)

    LaunchedEffect(viewModels.settings.settings?.weightUnit) {
        if (foodMode == Enums.FoodMode.CREATE_ENTRY) {
            when (viewModels.settings.settings?.weightUnit) {
                Enums.FoodWeightUnit.OUNCES -> {
                    viewModels.foodEntry.setCurrentAmount(
                        ConversionUtils.convertWeight(1.0, Enums.FoodWeightUnit.OUNCES, true)
                    )
                }
                Enums.FoodWeightUnit.POUNDS -> {
                    viewModels.foodEntry.setCurrentAmount(
                        ConversionUtils.convertWeight(1.0, Enums.FoodWeightUnit.POUNDS, true)
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
            val caloriesMapped = Enums.EnergyUnit.entries.associateWith { energyUnit ->
                ConversionUtils.convertEnergy(
                    energy = viewModels.foods.selectedFood!!.food!!.calories * multiplier,
                    energyUnit = energyUnit,
                )
            }
            val fatsMapped = Enums.MacroWeightUnit.entries.associateWith { weightUnit ->
                ConversionUtils.convertMacroWeight(
                    value = viewModels.foods.selectedFood!!.food!!.fat * multiplier,
                    weightUnit = weightUnit,
                )
            }
            val carbsMapped = Enums.MacroWeightUnit.entries.associateWith { weightUnit ->
                ConversionUtils.convertMacroWeight(
                    value = viewModels.foods.selectedFood!!.food!!.carbs * multiplier,
                    weightUnit = weightUnit,
                )
            }
            val proteinMapped = Enums.MacroWeightUnit.entries.associateWith { weightUnit ->
                ConversionUtils.convertMacroWeight(
                    value = viewModels.foods.selectedFood!!.food!!.protein * multiplier,
                    weightUnit = weightUnit,
                )
            }

            calculatedNutrientSummary = NutrientSummary(caloriesMapped, fatsMapped, carbsMapped, proteinMapped)
        }
    }

    fun handleBarcodeAddition() {
        if (newBarcode?.isNotEmpty() == true) {
            viewModels.foods.addBarcode(newBarcode!!)
            isBarcodeDialogOpen = false
        }
    }

    fun handleEditSave() {
        if (isEditable) {
            viewModels.foods.updateFoodFromSelectedFood()
        }
        isEditable = !isEditable
    }

    LaunchedEffect(isEditable) {
        Log.d("EditMode", "isEditable: $isEditable")
    }

    Column(modifier = Modifier
        .padding(horizontal = 16.dp)
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())
    ) {
        Column {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    "${viewModels.foods.selectedFood?.food?.name}",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.weight(1f)
                )
                if (viewModels.user.user?.role == Enums.Role.ADMIN) {
                    TextButton({ handleEditSave() }) {
                        Text(if (isEditable) "Save" else "Edit")
                    }
                }
                // TODO: Add functionality to request update
                // TODO: Add functionality to request delete
            }
            if (viewModels.foods.selectedFood?.food?.brand != null) {
                Text(
                    "${viewModels.foods.selectedFood?.food?.brand}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            // TODO: Implement tags for foods and render them as a group
            if (viewModels.foods.selectedFood?.food?.fineliId != null) {
                TextTag("Fineli")
            }
            if (viewModels.foods.selectedFood?.food?.barcode != null
                && viewModels.foods.selectedFood?.food?.barcode?.isNotEmpty() == true
            ) {
                IconTag(imageVector = Icons.Default.QrCode2, contentDescription = "Barcode scanning")
            }
        }
        if (viewModels.foods.selectedFood?.food?.barcode == null || viewModels.foods.selectedFood?.food?.barcode?.isEmpty() == true) {
            TextButton(
                onClick = { isBarcodeDialogOpen = true },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("Add barcode")
            }
        }
        NutrifierDialog(
            isVisible = isBarcodeDialogOpen,
            onDismiss = { isBarcodeDialogOpen = false },
            title = "Add barcode",
            actionButtons = {
                ActionButtons(
                    onSecondaryAction = { isBarcodeDialogOpen = false },
                    padding = PaddingValues(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 0.dp)
                ) {
                    handleBarcodeAddition()
                }
            }
        ) {
            BarcodeTextField(
                barcode = newBarcode ?: "",
                onBarcodeChange = { newBarcode = it },
                navController = navController,
                navigationDestination = "barcode/EDIT_FOOD"
            )
        }
        Spacer(modifier = Modifier.padding(vertical = 16.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                NutrientColumn(
                    nutrient = "Energy",
                    value = calculatedNutrientSummary.energy[viewModels.settings.settings?.energyUnit ?: Enums.EnergyUnit.KCAL] ?: 0.0,
                    suffix = viewModels.settings.settings?.energyUnit?.displayName ?: "kcal",
                )
                if (IS_DEV) {
                    calculatedNutrientSummary.energy.forEach { (energyUnit, value) ->
                        if (energyUnit != viewModels.settings.settings?.energyUnit) {
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
                value = calculatedNutrientSummary.fats[Enums.MacroWeightUnit.GRAMS] ?: 0.0,
                style = MaterialTheme.typography.titleLarge,
                suffix = "g",
                modifier = Modifier.weight(1f),
            )
            NutrientColumn(
                nutrient = "Carbs",
                value = calculatedNutrientSummary.carbs[Enums.MacroWeightUnit.GRAMS] ?: 0.0,
                style = MaterialTheme.typography.titleLarge,
                suffix = "g",
                modifier = Modifier.weight(1f),
            )
            NutrientColumn(
                nutrient = "Protein",
                value = calculatedNutrientSummary.protein[Enums.MacroWeightUnit.GRAMS] ?: 0.0,
                style = MaterialTheme.typography.titleLarge,
                suffix = "g",
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
                            if (weightUnit != Enums.MacroWeightUnit.GRAMS) {
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
        Spacer(modifier = Modifier.padding(vertical = 32.dp))
        NutrientsPerUnit(
            viewModels = viewModels,
            isEditable = isEditable
        )
        Spacer(modifier = Modifier.padding(vertical = 80.dp))
    }
}
