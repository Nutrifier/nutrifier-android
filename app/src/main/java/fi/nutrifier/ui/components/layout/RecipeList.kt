package fi.nutrifier.ui.components.layout

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fi.nutrifier.models.database.Recipe
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
    if (recipes.isEmpty()) onEmpty()
    else {
        recipes.reversed().map {
            RecipeButton(navController, it, viewModels.inspection)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
