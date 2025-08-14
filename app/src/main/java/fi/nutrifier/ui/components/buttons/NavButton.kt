package fi.nutrifier.ui.components.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * A composable function that displays a navigation button.
 *
 * @param selected Whether the button is currently selected.
 * @param onClick The callback to be invoked when the button is clicked.
 * @param iconVector The icon vector to be displayed on the button.
 * @param text The optional text to be displayed below the icon.
 */
@Composable
fun NavButton(
    selected: Boolean,
    onClick: () -> Unit,
    iconVector: ImageVector,
    text: String? = null
) {
    Button(
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor =
                if (selected) MaterialTheme.colorScheme.primary
                else Color.Gray.copy(alpha = 0.5f),
        ),
        modifier = Modifier.width(64.dp).height(64.dp),
        onClick = { if (!selected) onClick() }
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = iconVector, contentDescription = "$text icon")
            if (text != null) {
                Text(
                    text = text,
                    maxLines = 1,
                    style = TextStyle(fontSize = 12.sp),
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}