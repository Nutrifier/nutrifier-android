package fi.nutrifier.ui.screens.cookbook.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fi.nutrifier.ui.components.inputs.CustomSearchBar
import fi.nutrifier.ui.components.layout.CategoryShelf
import fi.nutrifier.ui.components.layout.RecipeList
import fi.nutrifier.ui.components.layout.RecipeShelf
import fi.nutrifier.ui.components.layout.SearchPanel
import fi.nutrifier.ui.components.misc.EmptyRecipeList
import fi.nutrifier.viewmodels.ViewModelWrapper


/**
 * Composable function for displaying recipes.
 * @param navController The navigation controller for navigating between screens.
 * @param viewModels The ViewModelWrapper containing the necessary view models for the screen.
 */
@Composable
internal fun DiscoverRecipesTab(navController: NavController, viewModels: ViewModelWrapper) {
    var showSearchPanel by remember { mutableStateOf(false) }
    val isLoading by viewModels.specials.loading.collectAsState()

    if (isLoading)  LinearProgressIndicator()
    else {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Column {
                Spacer(modifier = Modifier.height(4.dp))
                CustomSearchBar(
                    placeholder = "Search any recipe",
                    showResult = showSearchPanel,
                    handleShowResult = { showSearchPanel = it },
                ) {
                    viewModels.search.search(it)
                }
                if (showSearchPanel) SearchPanel(navController, viewModels)
            }
            if (viewModels.user.user?.hasPremium() == true) {
                Column {
                    Text("Today's Specials", style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    RecipeShelf(viewModels, navController)
                }
            }
            Column {
                Text("Categories", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))
                CategoryShelf(viewModels) { showSearchPanel = it }
            }
        }
    }
}