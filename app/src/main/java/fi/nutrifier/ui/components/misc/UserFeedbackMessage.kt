package fi.nutrifier.ui.components.misc

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Composable function that displays a user feedback message based on the provided type.
 *
 * @param message The message to display.
 * @param type The type of feedback message. Defaults to "default".
 */
@Composable
fun UserFeedbackMessage(message: String, type: String = "default") {
    when (type) {
        "error" -> Error(message)
        "warning" -> Warning(message)
        "default" -> Default(message)
    }
}

/**
 * Composable function that displays a default user feedback message.
 *
 * @param message The message to display.
 */
@Composable
private fun Default(message: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
    ) {
        Spacer(modifier = Modifier.width(8.dp))
        Text(message, style = MaterialTheme.typography.labelLarge)
    }
}

/**
 * Composable function that displays an error user feedback message.
 *
 * @param message The message to display.
 */
@Composable
private fun Error(message: String) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = MaterialTheme.colorScheme.error,
        contentColor = MaterialTheme.colorScheme.errorContainer,
        shadowElevation = 8.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
        ) {
            Icon(
                imageVector = Icons.Rounded.ErrorOutline,
                contentDescription = "error",
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(message, style = MaterialTheme.typography.labelLarge)
        }
    }
}

/**
 * Composable function that displays a warning user feedback message.
 *
 * @param message The message to display.
 */
@Composable
private fun Warning(message: String) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        shadowElevation = 8.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
        ) {
            Icon(
                imageVector = Icons.Rounded.WarningAmber,
                contentDescription = "warning",
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(message, style = MaterialTheme.typography.labelLarge)
        }
    }
}
