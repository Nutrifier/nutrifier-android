package fi.nutrifier.ui.screens.cookbook

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import fi.nutrifier.ui.components.layout.RecipeList
import fi.nutrifier.ui.components.misc.EmptyRecipeList
import fi.nutrifier.ui.components.misc.UserFeedbackMessage
import fi.nutrifier.utils.Alert
import fi.nutrifier.utils.AlertType
import fi.nutrifier.viewmodels.ViewModelWrapper
import kotlinx.coroutines.flow.collectLatest


/**
 * Composable function for displaying favourite recipes tab.
 * @param navController The navigation controller for navigating between screens.
 * @param viewModels The ViewModelWrapper containing the necessary view models for the screen.
 */
@Composable
internal fun FavouriteRecipesTab(navController: NavController, viewModels: ViewModelWrapper) {
    val isLoading by viewModels.personal.loading.collectAsState()

    if (isLoading) {
        LinearProgressIndicator()
    } else {
        RecipeList(navController, viewModels.favourite.recipes, viewModels,
            onEmpty = { EmptyRecipeList(navController, viewModels, true) })
    }
}