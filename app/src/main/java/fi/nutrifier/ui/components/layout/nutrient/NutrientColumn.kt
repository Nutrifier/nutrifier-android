package fi.nutrifier.ui.components.layout.nutrient

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import fi.nutrifier.utils.FormattingUtils

@Composable
fun NutrientColumn(
    nutrient: String,
    value: Double,
    suffix: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.headlineMedium,
) {
    Column(modifier = modifier) {
        Text(
            text = nutrient,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline,
        )
        Text(text = "${FormattingUtils.formatNumber(value)} $suffix", style = style)
    }
}
