package fi.nutrifier.ui.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun MockButton(onClick: () -> Unit) {
    TextButton(
        onClick = { onClick() },
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        modifier = Modifier
            .clip(RoundedCornerShape(32.dp))
            .background(color = MaterialTheme.colorScheme.surface)
            .height(44.dp)
    ) {
        Text(text = "DEV MENU")
    }
}