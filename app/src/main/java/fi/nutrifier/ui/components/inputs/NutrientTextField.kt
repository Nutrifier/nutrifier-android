package fi.nutrifier.ui.components.inputs

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun NutrientTextField(
    value: String,
    suffixText: String,
    width: Dp = 96.dp,
    keyboardType: KeyboardType = KeyboardType.Number,
    editable: Boolean = true,
    onChange: ((String) -> Unit)? = null,
) {
    if (editable) {
        TextField(
            value = value,
            onValueChange = { if (onChange != null) { onChange(it) } },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = androidx.compose.ui.Modifier
                .width(width)
                .border(2.dp, MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(32.dp))
                .clip(RoundedCornerShape(32.dp)),
            suffix = {
                Text(text = suffixText, color = MaterialTheme.colorScheme.outlineVariant)
            },
        )
    } else {
        Text(text = "$value $suffixText", style = MaterialTheme.typography.headlineSmall)
    }
}