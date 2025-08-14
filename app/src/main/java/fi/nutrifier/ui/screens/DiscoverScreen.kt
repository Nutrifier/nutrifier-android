package fi.nutrifier.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fi.nutrifier.ui.components.buttons.AddRecipeButton
import fi.nutrifier.ui.components.layout.CategoryShelf
import fi.nutrifier.ui.components.inputs.CustomSearchBar
import fi.nutrifier.ui.components.navigation.NavBar
import fi.nutrifier.ui.components.layout.RecipeShelf
import fi.nutrifier.ui.components.layout.SearchPanel
import fi.nutrifier.ui.components.layout.TopBar
import fi.nutrifier.ui.components.misc.UserFeedbackMessage
import fi.nutrifier.utils.Constants
import fi.nutrifier.viewmodels.ViewModelWrapper

/**
 * Composable function for displaying the Discover screen.
 * @param navController The navigation controller for navigating between screens.
 * @param viewModels The ViewModelWrapper containing the necessary view models for the screen.
 */
@Composable
fun DiscoverScreen(
    navController: NavController,
    viewModels: ViewModelWrapper,
    snackbarHostState: SnackbarHostState,
) {
    var showSearchPanel by remember { mutableStateOf(false) }

    Screen(
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                TopBar("Discover")
                TextButton(onClick = {
                    viewModels.authViewModel.logout()
                    navController.navigate("login")
                }) {
                    Text(text = "Logout")
                }
            }
        },
        bottomBar = { NavBar(navController, "discover") },
        screen = Constants.Screen.DISCOVER,
        viewModels,
        navController,
        snackbarHostState,
        floatingActionButton = {
            AddRecipeButton(navController, viewModels.inspection)
        }
    ) {
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())) {
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    /* === SEARCH SECTION === */
                    CustomSearchBar(
                        placeholder = "Search any recipe",
                        showSearchPanel, { showSearchPanel = it }) {
                        viewModels.search.search(it)
                    }
                    if (showSearchPanel) SearchPanel(navController, viewModels)
                    Spacer(modifier = Modifier.height(16.dp))

                    /* === TODAY'S SPECIALS SECTION === */
                    Text("Today's Specials", style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    if (viewModels.specials.alert != null) {
                        UserFeedbackMessage(viewModels.specials.alert!!.message, "error")
                    } else if (viewModels.specials.loading) {
                        LinearProgressIndicator()
                    } else {
                        RecipeShelf(
                            navController,
                            viewModels.specials.recipes,
                            viewModels,
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    /* === CATEGORY SEARCH SECTION === */
                    Text("Categories", style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    CategoryShelf(viewModels) { showSearchPanel = it }
                }
            }
        }
    }
}