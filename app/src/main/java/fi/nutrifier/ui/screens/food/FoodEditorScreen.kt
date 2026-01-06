package fi.nutrifier.ui.screens.food

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fi.nutrifier.models.database.Log
import fi.nutrifier.ui.components.buttons.BackButton
import fi.nutrifier.ui.components.layout.TopBar
import fi.nutrifier.viewmodels.ViewModelWrapper
import fi.nutrifier.ui.components.inputs.CancelSaveOption
import fi.nutrifier.ui.screens.Screen
import fi.nutrifier.utils.Constants
import fi.nutrifier.utils.FormattingUtils
import java.time.LocalTime

/**
 * Composable function for displaying the Food Editor screen.
 * @param navController The navigation controller for navigating between screens.
 * @param viewModels The ViewModelWrapper containing the necessary view models for the screen.
 */
@Composable
fun FoodEditorScreen(
    navController: NavController,
    viewModels: ViewModelWrapper,
    snackbarHostState: SnackbarHostState,
    mode: String,
) {
    LaunchedEffect(viewModels.logsScreen.alert) {
        viewModels.logsScreen.alert?.let {
            snackbarHostState.showSnackbar(it.message)
            viewModels.logsScreen.clearAlert()
        }
    }

    Screen(
        topBar = { TopBar(subtitle = { BackButton(navController) }) },
        bottomBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                when (mode) {
                    "ADD" ->
                        CancelSaveOption(onClose = { navController.navigateUp() }) {
                            viewModels.foods.savableFood?.let {
                                viewModels.foods.saveFood(it)
                                navController.navigateUp()
                            }
                        }
                    "EDIT" ->
                        CancelSaveOption(onClose = { navController.navigateUp() }) {
                            viewModels.foods.selectedFood?.let {
                                val log = it.food?.id?.let { it1 ->
                                    Log(
                                        date = viewModels.logsScreen.date.toString(),
                                        // TODO: Make time changeable
                                        time = FormattingUtils.formatLocalTimeToString(
                                            LocalTime.now()
                                        ),
                                        meal = viewModels.logsScreen.selectedMeal.toString(),
                                        userId = viewModels.logsScreen.getUserId(),
                                        foodId = it1,
                                        amount = viewModels.logsScreen.currentAmount.toDouble(),
                                    )
                                }
                                if (log != null) {
                                    viewModels.logsScreen.saveLog(log)
                                    repeat(2) { navController.popBackStack() }
                                }
                            }
                        }
                    "UPDATE" ->
                        CancelSaveOption(onClose = { navController.navigateUp() }) {
                            val newAmount = viewModels.logsScreen.currentAmount.toDoubleOrNull()
                            if (newAmount != null) {
                                val updatedLog = viewModels.logsScreen.selectedLog?.copy(amount = newAmount)
                                if (updatedLog != null) {
                                    viewModels.logsScreen.updateLog(updatedLog)
                                    navController.popBackStack()
                                }
                            }
                        }
                    else -> null
                }
            }
        },
        screen = Constants.Screen.FOOD_EDIT,
        viewModels,
        navController,
        snackbarHostState,
    ) {
        when (mode) {
            "ADD" -> AddMode(navController, viewModels)
            "EDIT" -> EditMode(viewModels)
            "UPDATE" -> EditMode(viewModels)
            else -> ViewMode(navController, viewModels)
        }
    }
}
