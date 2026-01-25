package fi.nutrifier.ui.screens.cookbook.tabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import fi.nutrifier.ui.components.layout.RecipeList
import fi.nutrifier.ui.components.misc.EmptyRecipeList
import fi.nutrifier.viewmodels.ViewModelWrapper

/**
 * Composable function for displaying personal recipes tab.
 * @param navController The navigation controller for navigating between screens.
 * @param viewModels The ViewModelWrapper containing the necessary view models for the screen.
 */
@Composable
internal fun PersonalRecipesTab(navController: NavController, viewModels: ViewModelWrapper) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
        // TODO: Implement search
        RecipeList(navController, viewModels.personal.recipes, viewModels,
            onEmpty = { EmptyRecipeList(navController, viewModels) }
        )
    }
}