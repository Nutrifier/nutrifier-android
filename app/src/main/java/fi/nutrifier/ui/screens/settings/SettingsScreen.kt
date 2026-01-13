package fi.nutrifier.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fi.nutrifier.ui.components.navigation.NavBar
import fi.nutrifier.ui.components.layout.TopBar
import fi.nutrifier.ui.screens.Screen
import fi.nutrifier.ui.screens.settings.sections.GoalsSection
import fi.nutrifier.ui.screens.settings.sections.NotificationSection
import fi.nutrifier.ui.screens.settings.sections.OtherSection
import fi.nutrifier.ui.screens.settings.sections.ProfileSection
import fi.nutrifier.ui.screens.settings.sections.TrackingSection
import fi.nutrifier.utils.Constants
import fi.nutrifier.viewmodels.ViewModelWrapper

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModels: ViewModelWrapper,
    snackbarHostState: SnackbarHostState,
) {
    Screen(
        topBar = { TopBar("Settings", navController = navController) },
        bottomBar = { NavBar(navController, "settings") },
        screen = Constants.Screen.SETTINGS,
        viewModels,
        navController,
        snackbarHostState,
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
            ProfileSection(viewModels)
            GoalsSection()
            TrackingSection()
            NotificationSection()
            OtherSection(navController, viewModels)
        }
    }
}
