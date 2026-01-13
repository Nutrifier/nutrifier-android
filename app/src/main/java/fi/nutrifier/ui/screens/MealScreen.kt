package fi.nutrifier.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fi.nutrifier.models.database.FoodLog
import fi.nutrifier.models.database.MealType
import fi.nutrifier.ui.components.buttons.AddButton
import fi.nutrifier.ui.components.buttons.BackButton
import fi.nutrifier.ui.components.buttons.MealLogButton
import fi.nutrifier.ui.components.layout.MealNutrients
import fi.nutrifier.ui.components.layout.NutrientColumn
import fi.nutrifier.ui.components.layout.TopBar
import fi.nutrifier.utils.Constants
import fi.nutrifier.utils.FormattingUtils.toLowerCaseCapitalizeFirst
import fi.nutrifier.viewmodels.ViewModelWrapper

@Composable
fun MealScreen(
    navController: NavController,
    viewModels: ViewModelWrapper,
    snackbarHostState: SnackbarHostState,
) {
    var logs: List<FoodLog> by remember { mutableStateOf(emptyList()) }

    LaunchedEffect(
        viewModels.logsScreen.selectedMeal,
        viewModels.logsScreen.breakfastLogs,
        viewModels.logsScreen.lunchLogs,
        viewModels.logsScreen.dinnerLogs,
        viewModels.logsScreen.snacksLogs,
    ) {
        logs = when (viewModels.logsScreen.selectedMeal) {
            MealType.BREAKFAST -> viewModels.logsScreen.breakfastLogs
            MealType.LUNCH -> viewModels.logsScreen.lunchLogs
            MealType.DINNER -> viewModels.logsScreen.dinnerLogs
            else -> viewModels.logsScreen.snacksLogs
        }
    }

    LaunchedEffect(viewModels.logsScreen.alert) {
        viewModels.logsScreen.alert?.let {
            snackbarHostState.showSnackbar(it.message)
            viewModels.logsScreen.clearAlert()
        }
    }

    Screen(
        topBar = { TopBar(subtitle = { BackButton(navController) }) },
        bottomBar = {},
        screen = Constants.Screen.MEAL,
        viewModels,
        navController,
        snackbarHostState,
        floatingActionButton = { AddButton { navController.navigate("add_food") }
    }) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
            Column(modifier = Modifier.padding(horizontal = 16.dp).fillMaxSize()) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        toLowerCaseCapitalizeFirst(viewModels.logsScreen.selectedMeal.toString()),
                        style = MaterialTheme.typography.headlineLarge,
                    )
                    viewModels.logsScreen.nutrients[viewModels.logsScreen.selectedMeal]?.let {
                        NutrientColumn("Calories", it.calories, suffix = "kcal")
                    }
                }
                Spacer(modifier = Modifier.padding(vertical = 8.dp))
                viewModels.logsScreen.nutrients[viewModels.logsScreen.selectedMeal]?.let {
                    MealNutrients(it.carbs, it.protein, it.fats)
                }
                Spacer(modifier = Modifier.padding(vertical = 16.dp))

                Box {
                    LazyColumn {
                        items(logs) {
                            MealLogButton(it,
                                onClick = {
                                    viewModels.logsScreen.setSelectedFood(it.food)
                                    viewModels.logsScreen.setSelectedLog(it.log)
                                    viewModels.logsScreen.setCurrentAmount(it.log.amount.toString())
                                    navController.navigate("food_editor/UPDATE")
                                },
                                onDelete = {
                                    it.log.id?.let { logId -> viewModels.logsScreen.deleteLog(logId) }
                                }
                            )
                        }
                    }
                    if (viewModels.logsScreen.loading) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)),
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}