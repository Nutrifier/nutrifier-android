package fi.nutrifier.ui.components.buttons

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

/**
 * A composable function that displays a back button.
 * When clicked, it navigates back to the previous screen using the NavController.
 *
 * @param navController The NavController used to navigate between screens.
 */
@Composable
fun BackButton(navController: NavController, onNavigation: () -> Unit = { navController.navigateUp() }) {
    TextButton(
        onClick = { onNavigation() }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "back",
                modifier = Modifier.height(16.dp).width(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Back")
        }
    }
}
