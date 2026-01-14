package fi.nutrifier.ui.components.inputs

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fi.nutrifier.models.database.Food
import fi.nutrifier.ui.components.layout.TitledContainer
import fi.nutrifier.utils.FormattingUtils
import fi.nutrifier.viewmodels.ViewModelWrapper

@Composable
fun FoodForm(navController: NavController, viewModels: ViewModelWrapper) {
    var name by remember { mutableStateOf("") }
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
            val food = Food(
                name = name,
                barcode = barcode,
                servingSize = servingSize.toInt(),
                calories = calories.toDouble(),
                carbs = if (carbs.isNotEmpty()) {
                    FormattingUtils.stringToDouble(carbs)
                } else 0.0,
                protein = if (protein.isNotEmpty()) {
                    FormattingUtils.stringToDouble(protein)
                } else 0.0,
                fat = if (fat.isNotEmpty()) {
                    FormattingUtils.stringToDouble(fat)
                } else 0.0,
                createdBy = viewModels.foodEntry.getUserId(),
                editedBy = viewModels.foodEntry.getUserId(),
            )
            viewModels.foods.setSavableFood(food)
        }
    }

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name *") },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
        )
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            TextField(
                value = barcode,
                onValueChange = { barcode = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                label = { Text("Barcode") },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp, 0.dp, 0.dp, 6.dp))
                    .fillMaxWidth(0.85f)
            )
            Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            IconButton(
                onClick = {
                    Log.d("FoodForm", "Navigating to barcode")
                    navController.navigate("barcode")
                },
                modifier = Modifier
                    .clip(RoundedCornerShape(topEnd = 6.dp, bottomEnd = 6.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .height(56.dp)
                    .width(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.QrCodeScanner,
                    contentDescription = "Barcode Scanner",
                )
            }
        }
        Spacer(modifier = Modifier.padding(vertical = 24.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            NutrientInputRow("Nutrients per", servingSize,
                suffixText = "g",
                fillMaxWidth = false,
                onChange = { servingSize = it }
            )
        }
        Spacer(modifier = Modifier.padding(horizontal = 8.dp))
        TitledContainer(
            title = "Nutrients",
            titleSize = MaterialTheme.typography.titleLarge,
            backgroundColor = MaterialTheme.colorScheme.background,
        ) {
            Column(modifier = Modifier.padding(start = 8.dp)) {
                NutrientInputRow("Calories *", calories, 120.dp, "kcal") {
                    calories = it
                }
                NutrientInputRow("Carbohydrates", carbs, suffixText = "g") {
                    carbs = it
                }
                NutrientInputRow("Protein", protein, suffixText = "g") {
                    protein = it
                }
                NutrientInputRow("Fat", fat, suffixText = "g") {
                    fat = it
                }
            }
        }
    }
}