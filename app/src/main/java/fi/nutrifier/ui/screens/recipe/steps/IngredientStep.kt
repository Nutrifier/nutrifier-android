package fi.nutrifier.ui.screens.recipe.steps

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fi.nutrifier.models.room.RecipeIngredient
import fi.nutrifier.ui.components.inputs.IngredientForm
import fi.nutrifier.ui.components.layout.IngredientRow
import fi.nutrifier.ui.components.misc.LabeledComponent
import fi.nutrifier.viewmodels.RecipeUnderInspectionViewModel

/**
 * Composable function for rendering the ingredients step of the recipe editor.
 *
 * @param viewModel The [RecipeUnderInspectionViewModel] containing the recipe being edited.
 * @param handleAllowNextChange Callback function to handle changes in allowing navigation to the next step.
 */
@Composable
internal fun IngredientsStep(
    viewModel: RecipeUnderInspectionViewModel,
    handleAllowNextChange: (Boolean) -> Unit
) {
    LaunchedEffect(viewModel.recipe.value) {
        if (viewModel.recipe.value.ingredients.isEmpty()) {
            handleAllowNextChange(false)
        } else {
            handleAllowNextChange(true)
        }
    }

    fun handleIngredientAdd(ingredient: RecipeIngredient) {
        // TODO: Clear data on save
        viewModel.addIngredient(ingredient)
    }

    fun handleIngredientDelete(index: Int) {
        viewModel.deleteIngredient(index)
    }

    IngredientForm { handleIngredientAdd(it) }
    Spacer(modifier = Modifier.height(32.dp))
    if (viewModel.recipe.value.ingredients.isNotEmpty()) {
        LabeledComponent(label = "Added ingredients") {
            viewModel.recipe.value.ingredients.mapIndexed { index, ingredient ->
                IngredientRow(index, ingredient, handleDelete = { handleIngredientDelete(index) })
            }
        }
    }
}
