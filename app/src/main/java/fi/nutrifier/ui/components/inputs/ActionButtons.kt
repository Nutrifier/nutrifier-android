package fi.nutrifier.ui.components.inputs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ActionButtons(
    padding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 32.dp),
    showSecondaryButton: Boolean = true,
    showPrimaryButton: Boolean = true,
    secondaryActionButtonText: String = "Cancel",
    primaryActionButtonText: String = "Save",
    onSecondaryAction: (() -> Unit)? = null,
    onPrimaryAction: (() -> Unit)? = null,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(padding),
    ) {
        if (showSecondaryButton && onSecondaryAction != null) {
            Button(
                modifier = Modifier.weight(1f).height(44.dp),
                onClick = { onSecondaryAction() },
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.primary
                ),
            ) {
                Text(secondaryActionButtonText)
            }
        }
        if (showPrimaryButton && onPrimaryAction != null) {
            Button(
                modifier = Modifier.weight(1f).height(44.dp),
                onClick = { onPrimaryAction() },
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
            ) {
                Text(primaryActionButtonText)
            }
        }
    }
}