package fi.nutrifier.ui.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun ProfileButton(navController: NavController, close: Boolean = false) {
    IconButton(
        onClick = { if (close) {
            navController.navigateUp()
        } else {
            navController.navigate("settings")
        }},
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(
                if (close) {
                    MaterialTheme.colorScheme.surface
                } else {
                    MaterialTheme.colorScheme.primary
                }
            )
            .shadow(2.dp)
    ) {
        Icon(
            imageVector = if (close) Icons.Rounded.Close else Icons.Rounded.AccountCircle,
            contentDescription = if (close) "Close" else "Settings",
            tint = if (close) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MaterialTheme.colorScheme.onPrimary
            },
        )
    }
}

@Preview
@Composable
fun ProfileButtonPreview() {
    ProfileButton(rememberNavController())
}
