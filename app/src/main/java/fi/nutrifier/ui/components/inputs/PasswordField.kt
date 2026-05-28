package fi.nutrifier.ui.components.inputs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun PasswordField(value: String, errorMessage: String? = null, onValueChange: (String) -> Unit) {
    var isVisible by remember { mutableStateOf(false) }

    TextField(
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = "Password") },
        value = value,
        onValueChange = { onValueChange(it) },
        singleLine = true,
        isError = errorMessage != null,
        supportingText = { if (errorMessage != null) Text(errorMessage) },
        visualTransformation =
            if (isVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            IconButton(onClick = { isVisible = !isVisible }) {
                Icon(
                    imageVector =
                        if (isVisible) Icons.Filled.VisibilityOff
                        else Icons.Filled.Visibility
                    ,
                    contentDescription =
                        if (isVisible) "Hide password"
                        else "Show password"
                )
            }
        }
    )
}