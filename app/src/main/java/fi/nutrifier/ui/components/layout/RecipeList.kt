package fi.nutrifier.ui.components.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fi.nutrifier.models.room.Recipe
import fi.nutrifier.ui.components.buttons.RecipeButton
import fi.nutrifier.viewmodels.ViewModelWrapper

/**
 * Composable function that displays a list of recipes.
 *
 * @param navController The [NavController] used for navigation.
 * @param recipes The list of recipes to be displayed.
 * @param viewModels The [ViewModelWrapper] containing view models needed for handling recipes.
 * @param onEmpty The composable to be displayed when the recipe list is empty.
 */
@Composable
fun RecipeList(
    navController: NavController,
    recipes: List<Recipe>,
    viewModels: ViewModelWrapper,
    onEmpty: @Composable () -> Unit = {},
) {
    if (recipes.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize()) {
            onEmpty()
        }
    }
    else {
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
        ) {
            recipes.reversed().map {
                RecipeButton(navController, it, viewModels.inspection)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
