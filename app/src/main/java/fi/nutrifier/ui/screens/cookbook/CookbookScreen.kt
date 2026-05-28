package fi.nutrifier.ui.screens.cookbook

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ManageSearch
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import fi.nutrifier.ui.components.buttons.AddRecipeButton
import fi.nutrifier.ui.components.buttons.ProfileButton
import fi.nutrifier.ui.components.navigation.NavBar
import fi.nutrifier.ui.components.layout.TopBar
import fi.nutrifier.ui.screens.BaseScreen
import fi.nutrifier.ui.screens.cookbook.tabs.DiscoverRecipesTab
import fi.nutrifier.ui.screens.cookbook.tabs.PersonalRecipesTab
import fi.nutrifier.ui.screens.cookbook.tabs.SavedRecipesTab
import fi.nutrifier.utils.Enums
import fi.nutrifier.viewmodels.ViewModelWrapper

/**
 * Composable function for displaying the Cookbook screen.
 * @param navController The navigation controller for navigating between screens.
 * @param viewModels The ViewModelWrapper containing the necessary view models for the screen.
 */
@Composable
fun CookbookScreen(
    navController: NavController,
    viewModels: ViewModelWrapper,
) {
    var selectedTab: Int by remember { mutableIntStateOf(0) }

    BaseScreen(
        topBar = {
            TopBar(
                title = "Cookbook",
                tabs = {
                    SecondaryTabRow(selectedTabIndex = selectedTab, modifier = Modifier.fillMaxWidth()) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            text = {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.AutoMirrored.Filled.ManageSearch, "Search")
                                    Text("Discover")
                                }
                            }
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.Star, "Star")
                                    Text("Saved")
                                }
                            }
                        )
                        Tab(
                            selected = selectedTab == 2,
                            onClick = { selectedTab = 2 },
                            text = {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.Person, "Person")
                                    Text("Created")
                                }
                            }
                        )
                    }
                },
                actionButton = { ProfileButton(navController) }
            )
        },
        bottomBar = { NavBar(navController, "cookbook") },
        screen = Enums.Screen.COOKBOOK,
        viewModels,
        navController,
        floatingActionButton = {
            AddRecipeButton(navController, viewModels.inspection)
        }
    ) {
        when (selectedTab) {
            0 -> DiscoverRecipesTab(navController, viewModels)
            1 -> SavedRecipesTab(navController, viewModels)
            2 -> PersonalRecipesTab(navController, viewModels)
        }
    }
}