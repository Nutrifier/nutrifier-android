package fi.nutrifier.ui.components.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.rounded.QrCodeScanner

@Composable
fun BarcodeButton(onClick: () -> Unit) {
    IconButton(onClick = { /*TODO*/ }) {
        Icon(Icons.Rounded.QrCodeScanner, contentDescription = "qr-code-scanner")
    }
}