package fi.nutrifier.ui.screens.food

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fi.nutrifier.models.database.FoodEntry
import fi.nutrifier.ui.components.buttons.BackButton
import fi.nutrifier.ui.components.layout.TopBar
import fi.nutrifier.viewmodels.ViewModelWrapper
import fi.nutrifier.ui.components.inputs.CancelSaveOption
import fi.nutrifier.ui.screens.BaseScreen
import fi.nutrifier.utils.Alert
import fi.nutrifier.utils.Constants
import fi.nutrifier.utils.FormattingUtils
import kotlinx.coroutines.flow.collectLatest
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
    val isLoading by viewModels.specials.loading.collectAsState()
    var showDialog by remember { mutableStateOf<Alert?>(null) }

    LaunchedEffect(key1 = true) {
        viewModels.specials.alert.collectLatest {
            snackbarHostState.showSnackbar(
                message = it.message,
                withDismissAction = true,
            )
        }
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
                                val foodEntry = it.food?.id?.let { it1 ->
                                    FoodEntry(
                                        date = viewModels.foodEntry.date.toString(),
                                        // TODO: Make time changeable
                                        time = FormattingUtils.formatLocalTimeToString(
                                            LocalTime.now()
                                        ),
                                        meal = viewModels.foodEntry.selectedMeal.toString(),
                                        userId = viewModels.foodEntry.getUserId(),
                                        foodId = it1,
                                        amount = viewModels.foodEntry.currentAmount.toDouble(),
                                    )
                                }
                                if (foodEntry != null) {
                                    viewModels.foodEntry.saveFoodEntry(foodEntry)
                                    repeat(2) { navController.popBackStack() }
                                }
                            }
                        }
                    "UPDATE" ->
                        CancelSaveOption(onClose = { navController.navigateUp() }) {
                            val newAmount = viewModels.foodEntry.currentAmount.toDoubleOrNull()
                            if (newAmount != null) {
                                val updatedLog = viewModels.foodEntry.selectedFoodEntry?.copy(amount = newAmount)
                                if (updatedLog != null) {
                                    viewModels.foodEntry.updateFoodEntry(updatedLog)
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
