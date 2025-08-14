package fi.nutrifier.ui.components.buttons

import android.R
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.rounded.QrCodeScanner
import androidx.compose.ui.graphics.Color

@Composable
fun BarcodeButton(color: Color? = null, onClick: () -> Unit) {
    IconButton(onClick = { /*TODO*/ }) {
        if (color != null) {
            Icon(Icons.Rounded.QrCodeScanner, "qr-code-scanner", tint = color)
        } else {
            Icon(Icons.Rounded.QrCodeScanner, "qr-code-scanner")
        }
    }
}