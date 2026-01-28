package fi.nutrifier.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fi.nutrifier.models.database.FoodEntryFood
import fi.nutrifier.models.database.MealType
import fi.nutrifier.ui.components.buttons.AddButton
import fi.nutrifier.ui.components.buttons.BackButton
import fi.nutrifier.ui.components.buttons.FoodEntryButton
import fi.nutrifier.ui.components.layout.MealNutrients
import fi.nutrifier.ui.components.layout.nutrient.NutrientColumn
import fi.nutrifier.ui.components.layout.TopBar
import fi.nutrifier.ui.components.misc.EmptyFoodEntryList
import fi.nutrifier.utils.Constants
import fi.nutrifier.utils.Constants.IS_DEV
import fi.nutrifier.utils.FormattingUtils
import fi.nutrifier.utils.FormattingUtils.toLowerCaseCapitalizeFirst
import fi.nutrifier.viewmodels.ViewModelWrapper
import kotlinx.coroutines.flow.collectLatest
import kotlin.collections.component1
import kotlin.collections.component2

@Composable
fun MealScreen(
    navController: NavController,
    viewModels: ViewModelWrapper,
) {
    var foodEntries: List<FoodEntryFood> by remember { mutableStateOf(emptyList()) }
    val isLoading by viewModels.foodEntry.loading.collectAsState()

    LaunchedEffect(
        viewModels.foodEntry.selectedMeal,
        viewModels.foodEntry.breakfastEntries,
        viewModels.foodEntry.lunchEntries,
        viewModels.foodEntry.dinnerEntries,
        viewModels.foodEntry.snacksEntries,
    ) {
        foodEntries = when (viewModels.foodEntry.selectedMeal) {
            MealType.BREAKFAST -> viewModels.foodEntry.breakfastEntries
            MealType.LUNCH -> viewModels.foodEntry.lunchEntries
            MealType.DINNER -> viewModels.foodEntry.dinnerEntries
            else -> viewModels.foodEntry.snacksEntries
        }
        //Log.d("MealScreen", foodEntries.toString())
    }

    BaseScreen(
        topBar = { TopBar(subtitle = { BackButton(navController) }) },
        bottomBar = {},
        screen = Constants.Screen.MEAL,
        viewModels,
        navController,
        floatingActionButton = { AddButton { navController.navigate("add_food") }
    }) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
            Column(modifier = Modifier.padding(horizontal = 16.dp).fillMaxSize()) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        toLowerCaseCapitalizeFirst(viewModels.foodEntry.selectedMeal.toString()),
                        style = MaterialTheme.typography.headlineLarge,
                    )
                    Column {
                        viewModels.foodEntry.nutrients[viewModels.foodEntry.selectedMeal]?.let {
                            NutrientColumn(
                                nutrient = "Energy",
                                value = it.energy[viewModels.user.settings?.energyUnit ?: Constants.EnergyUnit.KCAL] ?: 0.0,
                                suffix = viewModels.user.settings?.energyUnit?.displayName ?: "kcal",
                            )
                            if (IS_DEV) {
                                Constants.EnergyUnit.entries.filter{ it2 -> it2 != viewModels.user.settings?.energyUnit }.forEach { energyUnit ->
                                    Text(
                                        text = "${FormattingUtils.roundUp(it.energy[energyUnit])} ${energyUnit.displayName}",
                                        color = MaterialTheme.colorScheme.outline,
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(vertical = 8.dp))
                viewModels.foodEntry.nutrients[viewModels.foodEntry.selectedMeal]?.let {
                    MealNutrients(viewModels.user, it.carbs, it.protein, it.fats)
                }
                Spacer(modifier = Modifier.padding(vertical = 16.dp))

                Box {
                    if (isLoading) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)),
                        ) {
                            CircularProgressIndicator()
                        }
                    } else if (foodEntries.isEmpty()) {
                        EmptyFoodEntryList()
                    } else {
                        LazyColumn {
                            items(foodEntries) {
                                FoodEntryButton(
                                    userViewModel = viewModels.user,
                                    foodEntryFood = it,
                                    onClick = {
                                        viewModels.foods.setSelectedFood(it.food)
                                        viewModels.foodEntry.setSelectedFoodEntry(it.foodEntry)
                                        viewModels.foodEntry.setCurrentAmount(it.foodEntry.amount)
                                        navController.navigate("food_editor/${Constants.FoodMode.EDIT_AMOUNT}")
                                    },
                                    onDelete = {
                                        it.foodEntry.id?.let { logId -> viewModels.foodEntry.deleteFoodEntry(logId) }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}