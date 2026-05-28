package fi.nutrifier.ui.components.dialogs

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import fi.nutrifier.ui.components.inputs.ActionButtons
import fi.nutrifier.viewmodels.ViewModelWrapper

/**
 * A composable function that displays a delete confirmation dialog.
 * When the delete action is confirmed, it deletes the recipe and navigates back to the cookbook screen.
 *
 * @param navController The NavController used to navigate between screens.
 * @param viewModels The ViewModelWrapper that contains the view models needed for the operation.
 * @param exitDialog A lambda function to handle the dismissal of the dialog.
 */
@Composable
fun DeleteDialog(
    navController: NavController,
    viewModels: ViewModelWrapper,
    isVisible: Boolean,
    exitDialog: () -> Unit
) {
    fun handleDelete() {
        exitDialog()
        viewModels.personal.delete(viewModels.inspection.recipe.value)
        navController.navigate("cookbook")
    }

    NutrifierDialog(
        isVisible = isVisible,
        onDismiss = { exitDialog() },
        title = "Are you sure you want to delete this recipe?",
        actionButtons = {
            ActionButtons(
                onSecondaryAction = { exitDialog() },
                onPrimaryAction = { handleDelete() },
                secondaryActionButtonText = "Cancel",
                primaryActionButtonText = "Delete",
            )
        }
    ) {
        Text("This action cannot be undone!")
    }
}
