package fi.nutrifier.ui.components.misc

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fi.nutrifier.utils.Alert
import fi.nutrifier.utils.AlertType

@Composable
fun CustomSnackbar(
    snackbarHostState: SnackbarHostState,
    alert: Alert?,
) {
    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { data ->
            val containerColor = when (alert?.type) {
                AlertType.ERROR -> MaterialTheme.colorScheme.errorContainer
                AlertType.WARNING -> MaterialTheme.colorScheme.surfaceVariant
                else -> MaterialTheme.colorScheme.surface
            }

            val contentColor = when (alert?.type) {
                AlertType.ERROR -> MaterialTheme.colorScheme.onErrorContainer
                AlertType.WARNING -> MaterialTheme.colorScheme.onSurfaceVariant
                else -> MaterialTheme.colorScheme.onSurface
            }

            Snackbar(
                snackbarData = data,
                containerColor = containerColor,
                contentColor = contentColor,
                actionColor = MaterialTheme.colorScheme.primary,
                dismissActionContentColor = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.padding(bottom = 84.dp)
            )
        }
    )
}
