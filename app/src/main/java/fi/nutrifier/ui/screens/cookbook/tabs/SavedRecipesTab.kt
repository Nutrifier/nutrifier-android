package fi.nutrifier.ui.screens.cookbook.tabs

import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import fi.nutrifier.ui.components.layout.RecipeList
import fi.nutrifier.ui.components.misc.EmptyRecipeList
import fi.nutrifier.viewmodels.ViewModelWrapper

@Composable
internal fun SavedRecipesTab(navController: NavController, viewModels: ViewModelWrapper) {
    val isLoading by viewModels.personal.loading.collectAsState()

    if (isLoading) LinearProgressIndicator()
    else {
        // TODO: Implement search
        RecipeList(navController, viewModels.favourite.recipes, viewModels,
            onEmpty = { EmptyRecipeList(navController, viewModels, true) })
    }
}