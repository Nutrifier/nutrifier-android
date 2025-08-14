package fi.nutrifier.ui.components.inputs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun NutrientInputRow(
    text: String,
    value: String,
    width: Dp = 96.dp,
    suffixText: String?,
    keyboardType: KeyboardType = KeyboardType.Number,
    fillMaxWidth: Boolean = true,
    editable: Boolean = true,
    onChange: ((String) -> Unit)? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = if (fillMaxWidth) {
            Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth()
        } else {
            Modifier.padding(vertical = 0.dp)
        }
    ) {
        Text(text, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.padding(horizontal = 8.dp))
        NutrientTextField(value, width, suffixText, keyboardType, editable) {
            if (onChange != null) onChange(it)
        }
    }
}