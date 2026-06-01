package fi.nutrifier.ui.components.inputs

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun BarcodeTextField(
    barcode: String,
    onBarcodeChange: (String) -> Unit,
    navController: NavController,
    navigationDestination: String = "barcode/CREATE-FOOD"
) {
    NutrifierTextField(
        value = barcode,
        onValueChange = onBarcodeChange,
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        label = { Text("Barcode") },
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            IconButton(
                onClick = { navController.navigate(navigationDestination) },
                modifier = Modifier.height(44.dp).padding(end = 4.dp)
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