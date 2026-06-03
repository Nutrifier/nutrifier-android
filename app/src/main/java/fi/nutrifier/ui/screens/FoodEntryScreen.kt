package fi.nutrifier.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fi.nutrifier.utils.Enums
import fi.nutrifier.ui.components.buttons.AddButton
import fi.nutrifier.ui.components.buttons.MealButton
import fi.nutrifier.ui.components.buttons.ProfileButton
import fi.nutrifier.ui.components.inputs.DateNavigator
import fi.nutrifier.ui.components.layout.NutrientProgressSection
import fi.nutrifier.ui.components.navigation.NavBar
import fi.nutrifier.ui.components.layout.TopBar
import fi.nutrifier.utils.ValidatorUtils
import fi.nutrifier.viewmodels.ViewModelWrapper
import java.time.LocalDate

/**
 * Composable function for displaying the Logs screen.
 * @param navController The navigation controller for navigating between screens.
 * @param viewModels The ViewModelWrapper containing the necessary view models for the screen.
 */
@Composable
fun FoodEntryScreen(
    navController: NavController,
    viewModels: ViewModelWrapper,
    selectedDateStr: String?
) {
    val isLineMode = viewModels.settings.settings?.nutrientDisplayMode == Enums.NutrientDisplayMode.LINE
    val isLoading by viewModels.foodEntry.loading.collectAsState()
    val selectedDate by viewModels.foodEntry.selectedDate

    LaunchedEffect(Unit) {
        viewModels.foodEntry.loadDailySummary()
    }

    LaunchedEffect(selectedDateStr) {
        if (selectedDateStr != null) {
            val newSelectedDate = LocalDate.parse(selectedDateStr)
            viewModels.foodEntry.setSelectedDate(newSelectedDate)
        }
    }

    BaseScreen(
        topBar = { TopBar(
            title = "Food Entries",
            bottomPadding = 12.dp,
            actionButton = { ProfileButton(navController) },
        )},
        bottomBar = { NavBar(navController, "logs") },
        screen = Enums.Screen.FOOD_ENTRIES,
        viewModels,
        navController,
        floatingActionButton = { AddButton { navController.navigate("add_food") } }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            DateNavigator(selectedDate) { viewModels.foodEntry.setSelectedDate(it) }
            Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                Spacer(modifier = Modifier.padding(vertical = if (isLineMode) 8.dp else 4.dp))
                NutrientProgressSection(viewModels)
                // TODO: Add functionality to change the status of the dailySummary
                Column(modifier = Modifier.fillMaxSize()) {
                    if (viewModels.foodEntry.summary != null
                        && ValidatorUtils.isDateInPast(selectedDate)
                        && viewModels.foodEntry.summary!!.caloriesConsumed <= viewModels.foodEntry.summary!!.calorieTarget
                        && !viewModels.foodEntry.summary!!.confirmed
                    ) {
                        Spacer(modifier = Modifier.height(24.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(horizontal = 8.dp),
                        ) {
                            Text(
                                text = "Did you remember to log everything? If you did, and want to count this towards the analytics, click Confirm.",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.outline,
                                modifier = Modifier.weight(0.7f)
                            )
                            Button({ viewModels.foodEntry.confirmDay() }) { Text("Confirm") }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    } else {
                        Spacer(modifier = Modifier.height(if (isLineMode) 32.dp else 24.dp))
                    }
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        MealButton(Enums.MealType.BREAKFAST, viewModels) {
                            viewModels.foodEntry.setSelectedMeal(Enums.MealType.BREAKFAST)
                            navController.navigate("meal")
                        }
                        MealButton(Enums.MealType.LUNCH, viewModels) {
                            viewModels.foodEntry.setSelectedMeal(Enums.MealType.LUNCH)
                            navController.navigate("meal")
                        }
                        MealButton(Enums.MealType.DINNER, viewModels) {
                            viewModels.foodEntry.setSelectedMeal(Enums.MealType.DINNER)
                            navController.navigate("meal")
                        }
                        MealButton(Enums.MealType.SNACKS, viewModels) {
                            viewModels.foodEntry.setSelectedMeal(Enums.MealType.SNACKS)
                            navController.navigate("meal")
                        }
                    }
                }
            }
        }
    }
}