package fi.nutrifier.ui.components.misc

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

/**
 * A composable function that displays a check icon inside a circular background.
 * This can be used to indicate a selected or completed state.
 */
@Composable
fun CheckCircle(checked: Boolean = true, useBorder: Boolean = true) {
    val modifier = if (useBorder) {
        Modifier
            .clip(CircleShape)
            .height(20.dp)
            .width(20.dp)
            .background(
                if (checked) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outline
            )
            .border(
                width = 1.dp,
                color =
                    if (checked) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surface,
                shape = CircleShape
            )
    } else {
        Modifier
            .clip(CircleShape)
            .height(22.dp)
            .width(22.dp)
            .background(
                if (checked) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outline
            )
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Rounded.Check,
                contentDescription = "check",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(2.dp)
            )
        }
    }
}
