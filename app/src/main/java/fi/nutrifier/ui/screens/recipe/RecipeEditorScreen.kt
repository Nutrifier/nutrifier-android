package fi.nutrifier.ui.screens.recipe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fi.nutrifier.ui.components.buttons.BackButton
import fi.nutrifier.ui.components.inputs.PreviousNextOption
import fi.nutrifier.ui.components.misc.StepIndicator
import fi.nutrifier.ui.components.layout.TopBar
import fi.nutrifier.ui.components.misc.TitleSubtitle
import fi.nutrifier.ui.screens.BaseScreen
import fi.nutrifier.ui.screens.recipe.steps.IngredientsStep
import fi.nutrifier.ui.screens.recipe.steps.InstructionsStep
import fi.nutrifier.ui.screens.recipe.steps.PreviewStep
import fi.nutrifier.ui.screens.recipe.steps.TitleStep
import fi.nutrifier.utils.Enums
import fi.nutrifier.viewmodels.RecipeUnderInspectionViewModel
import fi.nutrifier.viewmodels.ViewModelWrapper

/**
 * Composable function for displaying the Recipe Editor screen.
 * @param navController The navigation controller for navigating between screens.
 * @param viewModels The ViewModelWrapper containing the necessary view models for the screen.
 */
@Composable
fun RecipeEditorScreen(
    navController: NavController,
    viewModels: ViewModelWrapper,
) {
    val viewModel: RecipeUnderInspectionViewModel = viewModels.inspection

    var currentFormStep by remember { mutableIntStateOf(0) }
    var allowNext by remember { mutableStateOf(false) }

    fun handleSave() {
        val recipe = viewModels.inspection.recipe.value
        viewModels.personal.isRecipeInDatabase(recipe) { isFound ->
            if (isFound) {
                if (recipe.id == -1) {
                    viewModels.personal.add(recipe)
                }
                viewModels.personal.edit(recipe)
            } else {
                viewModels.personal.add(recipe)
            }
            navController.navigate("cookbook")
        }
    }

    fun handleStepChange(direction: Int) {
        val newCurrentFormStep = currentFormStep + direction
        if (newCurrentFormStep in 0..4) {
            currentFormStep = newCurrentFormStep
            if (newCurrentFormStep == 4) {
                handleSave()
            }
        }
        allowNext = direction < 0
    }

    fun handleAllowNextChange(newValue: Boolean) {
        allowNext = newValue
    }

    BaseScreen(
        topBar = { TopBar(subtitle = { BackButton(navController) }) },
        bottomBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                PreviousNextOption(
                    showPrevious = currentFormStep > 0,
                    allowNext = allowNext,
                    nextButtonText = if (currentFormStep == 3) "Save" else "Next"
                ) {
                    handleStepChange(it)
                }
            }
        },
        screen = Enums.Screen.RECIPE_EDIT,
        viewModels,
        navController,
    ) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                when (currentFormStep) {
                    0 -> TitleSubtitle("General")
                    1 -> TitleSubtitle("Ingredients")
                    2 -> TitleSubtitle("Instructions")
                    3 -> TitleSubtitle("Preview")
                }
                Spacer(modifier = Modifier.height(16.dp))
                StepIndicator(0..3, currentFormStep)
                Spacer(modifier = Modifier.height(44.dp))
                when (currentFormStep) {
                    0 -> TitleStep(viewModel, ::handleAllowNextChange, ::handleStepChange)
                    1 -> IngredientsStep(viewModel, ::handleAllowNextChange)
                    2 -> InstructionsStep(viewModel, ::handleAllowNextChange)
                    3 -> PreviewStep(
                        viewModels,
                        ::handleAllowNextChange,
                        navController
                    )
                }
            }
        }
    }
}

/**
 * Composable function for the content of the Recipe Editor screen.
 * @param navController The navigation controller for navigating between screens.
 * @param paddingValues Padding values for the content.
 * @param viewModels The ViewModelWrapper containing the necessary view models for the screen.
 * @param currentFormStep The current step in the recipe editing process.
 * @param handleAllowNextChange Function to handle changes in the 'allowNext' state.
 * @param handleStepChange Function to handle changing the current form step.
 */
@Composable
private fun RecipeEditorContent(
    navController: NavController,
    paddingValues: PaddingValues,
    viewModels: ViewModelWrapper,
    currentFormStep: Int,
    handleAllowNextChange: (Boolean) -> Unit,
    handleStepChange: (Int) -> Unit,
) {


}
