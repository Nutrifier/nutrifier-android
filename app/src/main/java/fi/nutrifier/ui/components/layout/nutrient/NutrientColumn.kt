package fi.nutrifier.ui.components.layout.nutrient

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import fi.nutrifier.utils.FormattingUtils

@Composable
fun NutrientColumn(
    nutrient: String,
    value: Double,
    suffix: String,
    modifier: Modifier = Modifier,
    prefix: String? = null,
    style: TextStyle = MaterialTheme.typography.headlineMedium,
    color: Color? = Color.Unspecified
) {
    Column(modifier = modifier) {
        Text(
            text = nutrient,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline
        )
        Row {
            if (prefix != null) Text(text = prefix, style = style)
            Text(
                text = "${FormattingUtils.formatNumberAndRound(value)} $suffix",
                style = style,
                color = color ?: Color.Unspecified,
            )
        }
    }
}
