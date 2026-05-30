package fi.nutrifier.ui.components.inputs

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun NutrifierTextField(
    value: String,
    onValueChange: (str: String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    label: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    readOnly: Boolean = false,
    centerText: Boolean = false,
    singleLine: Boolean = true,
    supportingText: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        suffix = suffix,
        label = label,
        modifier = modifier,
        isError = isError,
        readOnly = readOnly,
        supportingText = supportingText,
        visualTransformation = visualTransformation,
        shape = RoundedCornerShape(24.dp),
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        colors = OutlinedTextFieldDefaults.colors().copy(
            unfocusedLabelColor = MaterialTheme.colorScheme.outline
        ),
        textStyle = if (centerText) LocalTextStyle.current.copy(
            textAlign = TextAlign.Center
        ) else LocalTextStyle.current
    )
}