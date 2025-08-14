package fi.nutrifier.ui.components.inputs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CancelSaveOption(onClose: () -> Unit, onSave: () -> Unit) {
    Row {
        Button(
            modifier = Modifier.weight(1f).height(44.dp),
            onClick = { onClose() },
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.surface),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
        ) {
            Text("Cancel")
        }
        Spacer(modifier = Modifier.width(16.dp))
        Button(
            modifier = Modifier.weight(1f).height(44.dp),
            onClick = { onSave() },
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.surface),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
        ) {
            Text("Save")
        }
    }
}