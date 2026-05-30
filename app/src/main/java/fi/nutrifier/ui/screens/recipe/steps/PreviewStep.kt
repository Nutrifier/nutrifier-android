package fi.nutrifier.ui.screens.recipe.steps

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fi.nutrifier.ui.components.misc.LabeledComponent
import fi.nutrifier.ui.screens.recipe.RecipeScreen
import fi.nutrifier.viewmodels.ViewModelWrapper


/**
 * Composable function for rendering the preview step of the recipe editor.
 *
 * @param viewModels The [ViewModelWrapper] containing the view models that are used.
 * @param handleAllowNextChange Callback function to handle changes in allowing navigation to the next step.
 * @param navController The [NavController] used for navigation.
 */
@Composable
internal fun PreviewStep(
    viewModels: ViewModelWrapper,
    handleAllowNextChange: (Boolean) -> Unit,
    navController: NavController,
) {
    var showPreview by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        handleAllowNextChange(true)
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        LabeledComponent(label = "Title", gap = 4.dp) {
            Text(viewModels.inspection.recipe.value.title)
        }
        LabeledComponent(label = "Image", gap = 4.dp) {
            // TODO: Render the actual image
            Text(viewModels.inspection.recipe.value.image)
        }
        LabeledComponent(label = "Servings", gap = 4.dp) {
            Text("${viewModels.inspection.recipe.value.servings}")
        }
        LabeledComponent(label = "Ingredients", gap = 4.dp) {
            viewModels.inspection.recipe.value.ingredients.forEach {
                Text("${it.amount} ${it.unit.displayName}  ${it.name}")
            }
        }
        LabeledComponent(label = "Instructions", gap = 4.dp) {
            viewModels.inspection.recipe.value.instructions.forEach { instruction ->
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("${instruction.number}.")
                    Text(instruction.text)
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
    TextButton(
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(vertical = 0.dp, horizontal = 8.dp),
        onClick = { showPreview = !showPreview }
    ) {
        Text("Toggle Show Preview")
    }
    Spacer(modifier = Modifier.height(16.dp))
    if (showPreview) {
        Column(
            Modifier
                .fillMaxSize()
                .shadow(4.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
        ) {
            RecipeScreen(navController, viewModels, isPreview = true)
        }
    }
}