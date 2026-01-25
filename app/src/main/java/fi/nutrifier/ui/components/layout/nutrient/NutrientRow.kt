package fi.nutrifier.ui.components.layout.nutrient

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun NutrientRow(name: String, value: Double) {
    Row(horizontalArrangement = Arrangement.SpaceBetween) {
        Text(name)
        Text(value.toString())
    }
}