package fi.nutrifier.ui.components.inputs

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fi.nutrifier.models.database.FoodRequest
import fi.nutrifier.ui.components.layout.TitledContainer
import fi.nutrifier.utils.ConversionUtils
import fi.nutrifier.utils.Enums
import fi.nutrifier.viewmodels.ViewModelWrapper

@Composable
fun FoodForm(navController: NavController, viewModels: ViewModelWrapper) {
    var name by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var barcode by remember { mutableStateOf("") }
    var servingSize by remember { mutableStateOf("100") }
    var calories by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }

    LaunchedEffect(viewModels.barcode.barScanResult) {
        barcode = viewModels.barcode.barScanResult ?: ""
    }

    LaunchedEffect(name, barcode, servingSize, calories, carbs, protein, fat) {
        viewModels.foods.setSavableFood(null)

        if (name.isNotEmpty() && servingSize.isNotEmpty() && calories.isNotEmpty()) {
            val food = FoodRequest(
                name = name,
                brand = brand,
                category = category,
                barcode = barcode,
                servingSize = servingSize.toInt(),
                calories = ConversionUtils.convertEnergy(
                    energy = calories.toDoubleOrNull() ?: 0.0,
                    energyUnit = viewModels.settings.settings?.energyUnit,
                    toKcal = true,
                ),
                fat = ConversionUtils.convertMacroWeight(
                    value = fat.toDoubleOrNull() ?: 0.0,
                    weightUnit = Enums.MacroWeightUnit.GRAMS,
                    toGrams = true,
                ),
                carbs = ConversionUtils.convertMacroWeight(
                    value = carbs.toDoubleOrNull() ?: 0.0,
                    weightUnit = Enums.MacroWeightUnit.GRAMS,
                    toGrams = true,
                ),
                protein = ConversionUtils.convertMacroWeight(
                    value = protein.toDoubleOrNull() ?: 0.0,
                    weightUnit = Enums.MacroWeightUnit.GRAMS,
                    toGrams = true,
                ),
            )
            viewModels.foods.setSavableFood(food)
        }
    }

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        NutrifierTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name *") },
            modifier = Modifier.fillMaxWidth()
        )
        NutrifierTextField(
            value = brand,
            onValueChange = { brand = it },
            label = { Text("Brand") },
            modifier = Modifier.fillMaxWidth()
        )
        NutrifierTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Category") },
            modifier = Modifier.fillMaxWidth()
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            NutrifierTextField(
                value = barcode,
                onValueChange = { barcode = it },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                label = { Text("Barcode") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            Log.d("FoodForm", "Navigating to barcode")
                            navController.navigate("barcode/CREATE-FOOD")
                        },
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(end = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCodeScanner,
                            contentDescription = "Barcode Scanner",
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
                },
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            NutrientInputRow(
                text = "Nutrients per",
                value = servingSize,
                suffixText = viewModels.settings.settings?.weightUnit?.displayName ?: "g",
                width = 124.dp,
                fillMaxWidth = false,
                onChange = { servingSize = it },
                editable = false // TODO: Make editable
            )
        }
        TitledContainer(
            title = "Nutrients",
            titleSize = MaterialTheme.typography.titleLarge,
            backgroundColor = MaterialTheme.colorScheme.background,
        ) {
            Column(modifier = Modifier.padding(start = 8.dp)) {
                NutrientInputRow(
                    text = "Energy *",
                    value = calories,
                    width = 120.dp,
                    suffixText = viewModels.settings.settings?.energyUnit?.displayName ?: "kcal",
                ) {
                    calories = it
                }
                NutrientInputRow(
                    text = "Carbohydrates *",
                    value = carbs,
                    suffixText = "g",
                ) {
                    carbs = it
                }
                NutrientInputRow(
                    text = "Protein *",
                    value = protein,
                    suffixText = "g",
                ) {
                    protein = it
                }
                NutrientInputRow(
                    text = "Fat *",
                    value = fat,
                    suffixText = "g",
                ) {
                    fat = it
                }
            }
        }
    }
}