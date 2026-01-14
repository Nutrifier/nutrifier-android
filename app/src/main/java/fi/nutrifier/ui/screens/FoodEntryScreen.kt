package fi.nutrifier.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fi.nutrifier.models.database.MealType
import fi.nutrifier.ui.components.buttons.AddButton
import fi.nutrifier.ui.components.buttons.MealButton
import fi.nutrifier.ui.components.inputs.DateNavigator
import fi.nutrifier.ui.components.layout.MacroWheels
import fi.nutrifier.ui.components.navigation.NavBar
import fi.nutrifier.ui.components.layout.TopBar
import fi.nutrifier.utils.Constants
import fi.nutrifier.viewmodels.ViewModelWrapper

/**
 * Composable function for displaying the Logs screen.
 * @param navController The navigation controller for navigating between screens.
 * @param viewModels The ViewModelWrapper containing the necessary view models for the screen.
 */
@Composable
fun FoodEntryScreen(
    navController: NavController,
    viewModels: ViewModelWrapper,
    snackbarHostState: SnackbarHostState,
) {
    LaunchedEffect(Unit) {
        viewModels.foodEntry.loadFoodEntries()
    }

    LaunchedEffect(viewModels.foodEntry.alert) {
        viewModels.foodEntry.alert?.let {
            snackbarHostState.showSnackbar(it.message)
            viewModels.foodEntry.clearAlert()
        }
    }

    Screen(
        topBar = { TopBar("Food Entries") },
        bottomBar = { NavBar(navController, "logs") },
        screen = Constants.Screen.FOOD_ENTRIES,
        viewModels,
        navController,
        snackbarHostState,
        floatingActionButton = { AddButton { navController.navigate("add_food") } }
    ) {
        LazyColumn {
            item {
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    Column(
                        Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        DateNavigator(viewModels.foodEntry.date) {
                            viewModels.foodEntry.setDate(it)
                        }
                        MacroWheels(viewModels.foodEntry.overallNutrients)
                        Spacer(modifier = Modifier.padding(vertical = 16.dp))
                    }
                }
            }
            item {
                MealButton(MealType.BREAKFAST, viewModels.foodEntry) {
                    viewModels.foodEntry.setSelectedMeal(MealType.BREAKFAST)
                    navController.navigate("meal")
                }
            }
            item {
                MealButton(MealType.LUNCH, viewModels.foodEntry) {
                    viewModels.foodEntry.setSelectedMeal(MealType.LUNCH)
                    navController.navigate("meal")
                }
            }
            item {
                MealButton(MealType.DINNER, viewModels.foodEntry) {
                    viewModels.foodEntry.setSelectedMeal(MealType.DINNER)
                    navController.navigate("meal")
                }
            }
            item {
                MealButton(MealType.SNACKS, viewModels.foodEntry) {
                    viewModels.foodEntry.setSelectedMeal(MealType.SNACKS)
                    navController.navigate("meal")
                }
            }
        }
    }
}