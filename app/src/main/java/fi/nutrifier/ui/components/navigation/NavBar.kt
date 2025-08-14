package fi.nutrifier.ui.components.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.rounded.Checklist
import fi.nutrifier.ui.components.buttons.NavButton

/**
 * A composable function that displays a bottom navigation bar with buttons for navigation.
 *
 * @param navController The NavController to handle navigation.
 * @param selected The currently selected navigation item.
 */
@Composable
fun NavBar(navController: NavController, selected: String) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.fillMaxWidth().height(80.dp)
    ) {
        BottomAppBar(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.fillMaxWidth().shadow(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                NavButton(
                    selected = selected == "discover",
                    onClick = { navController.navigate("discover") },
                    iconVector = Icons.Filled.Dashboard,
                    text = "Discover"
                )
                NavButton(
                    selected = selected == "logs",
                    onClick = { navController.navigate("logs") },
                    iconVector = Icons.Filled.Restaurant,
                    text = "Logs"
                )
                NavButton(
                    selected = selected == "cookbook",
                    onClick = { navController.navigate("cookbook") },
                    iconVector = Icons.AutoMirrored.Rounded.MenuBook,
                    text = "Cookbook"
                )
                NavButton(
                    selected = selected == "shopping_list",
                    onClick = { navController.navigate("shopping_list") },
                    iconVector = Icons.Rounded.Checklist,
                    text = "Grocery"
                )
            }
        }
    }
}
