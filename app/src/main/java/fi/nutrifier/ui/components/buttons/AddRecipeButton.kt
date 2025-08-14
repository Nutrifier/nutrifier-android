package fi.nutrifier.ui.components.buttons

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import fi.nutrifier.viewmodels.RecipeUnderInspectionViewModel

/**
 * A composable function that displays a circular button for adding a new recipe.
 * When clicked, it navigates to the recipe editor screen and sets the current recipe in the ViewModel to null.
 *
 * @param navController The NavController used to navigate between screens.
 * @param viewModel The ViewModel that manages the state of the recipe under inspection.
 */
@Composable
fun AddRecipeButton(navController: NavController, viewModel: RecipeUnderInspectionViewModel) {
    fun handleAddRecipeClick() {
        viewModel.setRecipe(null)
        navController.navigate("recipe_editor")

    }
    AddButton(::handleAddRecipeClick)
}