package fi.nutrifier.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fi.nutrifier.models.database.MealType
import fi.nutrifier.ui.components.buttons.AddButton
import fi.nutrifier.ui.components.buttons.MealButton
import fi.nutrifier.ui.components.buttons.ProfileButton
import fi.nutrifier.ui.components.inputs.DateNavigator
import fi.nutrifier.ui.components.layout.NutrientProgressSection
import fi.nutrifier.ui.components.navigation.NavBar
import fi.nutrifier.ui.components.layout.TopBar
import fi.nutrifier.ui.components.misc.UserFeedbackMessage
import fi.nutrifier.ui.theme.LocalExtraColors
import fi.nutrifier.utils.AlertType
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
) {
    val isLineMode = viewModels.user.settings?.nutrientDisplayMode == Constants.NutrientDisplayMode.LINE
    val isLoading by viewModels.foodEntry.loading.collectAsState()

    LaunchedEffect(Unit) {
        viewModels.foodEntry.loadFoodEntries()
    }

    BaseScreen(
        topBar = { TopBar(
            title = "Food Entries",
            bottomPadding = 12.dp,
            actionButton = { ProfileButton(navController) },
        )},
        bottomBar = { NavBar(navController, "logs") },
        screen = Constants.Screen.FOOD_ENTRIES,
        viewModels,
        navController,
        floatingActionButton = { AddButton { navController.navigate("add_food") } }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(bottom = 24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column {
                DateNavigator(viewModels.foodEntry.date) {
                    viewModels.foodEntry.setDate(it)
                }
                Spacer(modifier = Modifier.padding(vertical = if (isLineMode) 8.dp else 4.dp))
                NutrientProgressSection(
                    userViewModel = viewModels.user,
                    nutrientSummary = viewModels.foodEntry.overallNutrients,
                )
            }
            Spacer(modifier = Modifier.height(if (isLineMode) 16.dp else 8.dp))
            MealButton(MealType.BREAKFAST, viewModels) {
                viewModels.foodEntry.setSelectedMeal(MealType.BREAKFAST)
                navController.navigate("meal")
            }
            MealButton(MealType.LUNCH, viewModels) {
                viewModels.foodEntry.setSelectedMeal(MealType.LUNCH)
                navController.navigate("meal")
            }
            MealButton(MealType.DINNER, viewModels) {
                viewModels.foodEntry.setSelectedMeal(MealType.DINNER)
                navController.navigate("meal")
            }
            MealButton(MealType.SNACKS, viewModels) {
                viewModels.foodEntry.setSelectedMeal(MealType.SNACKS)
                navController.navigate("meal")
            }
        }
    }
}