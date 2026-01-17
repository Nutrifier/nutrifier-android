package fi.nutrifier.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fi.nutrifier.ui.components.navigation.NavBar
import fi.nutrifier.ui.components.layout.TopBar
import fi.nutrifier.ui.screens.BaseScreen
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
    BaseScreen(
        topBar = { TopBar("Settings", navController = navController) },
        bottomBar = { NavBar(navController, "settings") },
        screen = Constants.Screen.SETTINGS,
        viewModels,
        navController,
        snackbarHostState,
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            ProfileSection(viewModels)
            GoalsSection()
            TrackingSection()
            NotificationSection(viewModels)
            OtherSection(navController, viewModels)
        }
    }
}
