package fi.nutrifier.ui.components.inputs

import android.util.Log
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
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
    onConfirm: (() -> Unit)? = null,
    onChange: ((String) -> Unit)? = null,
) {
    if (editable) {
        OutlinedTextField(
            value = value,
            onValueChange = { if (onChange != null) { onChange(it) } },
            singleLine = true,
            colors = TextFieldDefaults.colors().copy(
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                focusedContainerColor = MaterialTheme.colorScheme.background
            ),
            shape = RoundedCornerShape(32.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = keyboardType,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = { if (onConfirm != null) onConfirm() }
            ),
            modifier = Modifier.width(width).height(56.dp).padding(0.dp),
            suffix = {
                Text(text = suffixText, color = MaterialTheme.colorScheme.outlineVariant)
            },
        )
    } else {
        Text(text = "$value $suffixText", style = MaterialTheme.typography.headlineSmall)
    }
}