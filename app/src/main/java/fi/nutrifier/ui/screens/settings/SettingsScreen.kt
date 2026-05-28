package fi.nutrifier.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fi.nutrifier.ui.components.buttons.ProfileButton
import fi.nutrifier.ui.components.navigation.NavBar
import fi.nutrifier.ui.components.layout.TopBar
import fi.nutrifier.ui.screens.BaseScreen
import fi.nutrifier.ui.screens.settings.sections.GoalsSection
import fi.nutrifier.ui.screens.settings.sections.NotificationSection
import fi.nutrifier.ui.screens.settings.sections.OtherSection
import fi.nutrifier.ui.screens.settings.sections.ProfileSection
import fi.nutrifier.ui.screens.settings.sections.TrackingSection
import fi.nutrifier.utils.Enums
import fi.nutrifier.viewmodels.ViewModelWrapper

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModels: ViewModelWrapper,
) {
    BaseScreen(
        topBar = { TopBar("Settings", actionButton = { ProfileButton(navController, true) }) },
        bottomBar = { NavBar(navController, "settings") },
        screen = Enums.Screen.SETTINGS,
        viewModels,
        navController,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 4.dp, vertical = 8.dp)
        ) {
            ProfileSection(viewModels)
            GoalsSection(viewModels)
            TrackingSection(viewModels)
            NotificationSection(viewModels)
            OtherSection(navController, viewModels)
        }
    }
}
