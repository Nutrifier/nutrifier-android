package fi.nutrifier.ui.screens.dashboard

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import fi.nutrifier.ui.components.buttons.AddRecipeButton
import fi.nutrifier.ui.components.buttons.ProfileButton
import fi.nutrifier.ui.components.navigation.NavBar
import fi.nutrifier.ui.components.layout.TopBar
import fi.nutrifier.ui.screens.BaseScreen
import fi.nutrifier.utils.Constants
import fi.nutrifier.viewmodels.ViewModelWrapper

/**
 * Composable function for displaying the Dashboard screen.
 * @param navController The navigation controller for navigating between screens.
 * @param viewModels The ViewModelWrapper containing the necessary view models for the screen.
 */
@Composable
fun DashboardScreen(
    navController: NavController,
    viewModels: ViewModelWrapper,
) {

    BaseScreen(
        topBar = { TopBar(title = "Dashboard", actionButton = { ProfileButton(navController) }) },
        bottomBar = { NavBar(navController, "dashboard") },
        screen = Constants.Screen.DASHBOARD,
        viewModels,
        navController,
        floatingActionButton = {
            AddRecipeButton(navController, viewModels.inspection)
        }
    ) {
    }
}
