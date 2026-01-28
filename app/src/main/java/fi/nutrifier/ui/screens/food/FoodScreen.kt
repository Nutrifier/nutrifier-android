package fi.nutrifier.ui.screens.food

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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
import fi.nutrifier.models.database.FoodEntry
import fi.nutrifier.models.database.MealType
import fi.nutrifier.ui.components.buttons.BackButton
import fi.nutrifier.ui.components.layout.TopBar
import fi.nutrifier.viewmodels.ViewModelWrapper
import fi.nutrifier.ui.components.inputs.CancelSaveOption
import fi.nutrifier.ui.components.inputs.NutrientTextField
import fi.nutrifier.ui.screens.BaseScreen
import fi.nutrifier.ui.screens.food.modes.CreateMode
import fi.nutrifier.ui.screens.food.modes.EditMode
import fi.nutrifier.ui.screens.food.modes.ViewMode
import fi.nutrifier.utils.Alert
import fi.nutrifier.utils.Constants
import fi.nutrifier.utils.Constants.IS_DEV
import fi.nutrifier.utils.ConversionUtils
import fi.nutrifier.utils.FormattingUtils
import java.time.LocalTime

/**
 * Composable function for displaying the Food Editor screen.
 * @param navController The navigation controller for navigating between screens.
 * @param viewModels The ViewModelWrapper containing the necessary view models for the screen.
 */
@Composable
fun FoodScreen(
    navController: NavController,
    viewModels: ViewModelWrapper,
    mode: String,
) {
    val isLoading by viewModels.foods.loading.collectAsState()
    var showDialog by remember { mutableStateOf<Alert?>(null) }
    var displayedCurrentAmount by remember { mutableStateOf("") }
    val foodMode = Constants.FoodMode.valueOf(mode)


    LaunchedEffect(viewModels.foods.selectedFood, foodMode) {
        when (foodMode) {
            Constants.FoodMode.CREATE_ENTRY -> {
                displayedCurrentAmount = when (viewModels.user.settings?.weightUnit) {
                    Constants.WeightUnit.LB -> "1"
                    Constants.WeightUnit.OZ -> "1"
                    else -> "100"
                }
            }
            Constants.FoodMode.EDIT_AMOUNT -> {
                displayedCurrentAmount = viewModels.foodEntry.currentAmount.toString()
            }
            else -> null
        }
    }

    fun onAmountChange(input: String) {
        displayedCurrentAmount = input

        val value = input.toDoubleOrNull() ?: return

        val grams = ConversionUtils.convertWeight(
            value = value,
            weightUnit = viewModels.user.settings?.weightUnit,
            toGrams = true,
        )

        viewModels.foodEntry.setCurrentAmount(grams)
    }

    fun onCreateFood() {
        viewModels.foods.savableFood?.let {
            viewModels.foods.saveFood(it)
            navController.navigateUp()
        }
    }

    fun onCreateEntry() {
        viewModels.foods.selectedFood?.let {
            val foodEntry = it.food?.id?.let { it1 ->
                FoodEntry(
                    date = viewModels.foodEntry.date.toString(),
                    // TODO: Make time changeable
                    time = FormattingUtils.formatLocalTimeToString(
                        LocalTime.now()
                    ),
                    meal = viewModels.foodEntry.selectedMeal ?: MealType.SNACKS,
                    userId = viewModels.foodEntry.getUserId(),
                    foodId = it1,
                    amount = viewModels.foodEntry.currentAmount, // Should never be anything than grams
                    fineliId = it.food.fineliId,
                )
            }
            if (foodEntry != null) {
                viewModels.foodEntry.saveFoodEntry(foodEntry)
                navController.navigateUp()
            }
        }
    }

    fun onEditAmount() {
        val updatedLog = viewModels.foodEntry.selectedFoodEntry?.copy(
            amount = viewModels.foodEntry.currentAmount,
        )
        if (updatedLog != null) {
            viewModels.foodEntry.updateFoodEntry(updatedLog)
            navController.popBackStack()
        }
    }

    BaseScreen(
        floatingActionButton = {
            if (foodMode == Constants.FoodMode.CREATE_ENTRY
                || foodMode == Constants.FoodMode.EDIT_AMOUNT
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (IS_DEV) {
                        Column {
                            Constants.WeightUnit.entries.filter { it != viewModels.user.settings?.weightUnit }.forEach {
                                Text(
                                    text = "${ConversionUtils.convertWeight(
                                        value = viewModels.foodEntry.currentAmount,
                                        weightUnit = it,
                                        roundUp = true,
                                    )} ${it.displayName}",
                                    color = MaterialTheme.colorScheme.outline,
                                )
                            }
                        }
                    }
                    NutrientTextField(
                        value = displayedCurrentAmount,
                        suffixText = viewModels.user.settings?.weightUnit?.displayName ?: "g",
                        width = 124.dp,
                        onConfirm = { if (foodMode == Constants.FoodMode.CREATE_ENTRY) {
                            onCreateEntry()
                        } else {
                            onEditAmount()
                        }}
                    ) { onAmountChange(it) }
                }
            } else null
        },
        topBar = { TopBar(subtitle = { BackButton(navController) }) },
        bottomBar = {
            when (foodMode) {
                Constants.FoodMode.CREATE -> {
                    CancelSaveOption(onClose = { navController.navigateUp() }) {
                        onCreateFood()
                    }
                }
                Constants.FoodMode.CREATE_ENTRY -> {
                    CancelSaveOption(onClose = { navController.navigateUp() }) {
                        onCreateEntry()
                    }
                }
                Constants.FoodMode.EDIT_AMOUNT -> {
                    CancelSaveOption(onClose = { navController.navigateUp() }) {
                        onEditAmount()
                    }
                }
                else -> null
            }
        },
        screen = Constants.Screen.FOOD_EDIT,
        viewModels = viewModels,
        navController = navController,
    ) {
        when (foodMode) {
            Constants.FoodMode.CREATE -> CreateMode(navController, viewModels)
            Constants.FoodMode.CREATE_ENTRY -> EditMode(viewModels, foodMode)
            Constants.FoodMode.EDIT_AMOUNT -> EditMode(viewModels, foodMode)
            else -> ViewMode(navController, viewModels)
        }
    }
}
