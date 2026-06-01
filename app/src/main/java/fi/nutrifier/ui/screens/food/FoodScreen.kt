package fi.nutrifier.ui.screens.food

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fi.nutrifier.models.database.FoodEntryRequest
import fi.nutrifier.ui.components.buttons.BackButton
import fi.nutrifier.ui.components.inputs.ActionButtons
import fi.nutrifier.ui.components.layout.TopBar
import fi.nutrifier.viewmodels.ViewModelWrapper
import fi.nutrifier.ui.components.inputs.NutrientTextField
import fi.nutrifier.ui.screens.BaseScreen
import fi.nutrifier.ui.screens.food.modes.CreateMode
import fi.nutrifier.ui.screens.food.modes.EditMode
import fi.nutrifier.ui.screens.food.modes.ViewMode
import fi.nutrifier.utils.Alert
import fi.nutrifier.utils.Constants.IS_DEV
import fi.nutrifier.utils.ConversionUtils
import fi.nutrifier.utils.Enums
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
    barcode: String?
) {
    val isLoading by viewModels.foods.loading.collectAsState()
    var showDialog by remember { mutableStateOf<Alert?>(null) }
    var displayedCurrentAmount by remember { mutableStateOf("") }
    val foodMode = Enums.FoodMode.valueOf(mode)
    val selectedDate by viewModels.foodEntry.selectedDate

    LaunchedEffect(viewModels.foods.selectedFood, foodMode) {
        when (foodMode) {
            Enums.FoodMode.CREATE_ENTRY -> {
                displayedCurrentAmount = when (viewModels.settings.settings?.weightUnit) {
                    Enums.FoodWeightUnit.POUNDS -> "1"
                    Enums.FoodWeightUnit.OUNCES -> "1"
                    else -> "100"
                }
            }
            Enums.FoodMode.EDIT_AMOUNT -> {
                displayedCurrentAmount = viewModels.foodEntry.currentAmount.toString()
            }
            else -> null
        }
    }

    fun onAmountChange(input: String) {
        displayedCurrentAmount = input

        val value = input.toDoubleOrNull() ?: return

        val grams = ConversionUtils.convertWeight(
            weight = value,
            foodWeightUnit = viewModels.settings.settings?.weightUnit,
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
        val food = viewModels.foods.selectedFood?.food ?: return

        val foodEntryRequest = FoodEntryRequest(
            amount = viewModels.foodEntry.currentAmount, // Should never be anything than grams
            date = selectedDate.toString(),
            // TODO: Make time changeable
            time = FormattingUtils.formatLocalTimeToStringExcludeNanoSeconds(LocalTime.now()),
            mealType = viewModels.foodEntry.selectedMeal ?: Enums.MealType.SNACKS,
            unit = viewModels.settings.settings?.weightUnit ?: Enums.FoodWeightUnit.GRAMS,
            fineliId = food.fineliId,
            foodId =  if (food.fineliId != null) null else food.id
        )

        viewModels.foodEntry.saveFoodEntry(foodEntryRequest)
        navController.navigateUp()
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
            if (foodMode == Enums.FoodMode.CREATE_ENTRY
                || foodMode == Enums.FoodMode.EDIT_AMOUNT
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (IS_DEV) {
                        Column {
                            Enums.FoodWeightUnit.entries.filter { it != viewModels.settings.settings?.weightUnit }.forEach {
                                Text(
                                    text = "${ConversionUtils.convertWeight(
                                        weight = viewModels.foodEntry.currentAmount,
                                        foodWeightUnit = it,
                                        roundUp = true,
                                    )} ${it.displayName}",
                                    color = MaterialTheme.colorScheme.outline,
                                )
                            }
                        }
                    }
                    NutrientTextField(
                        value = displayedCurrentAmount,
                        suffixText = viewModels.settings.settings?.weightUnit?.displayName ?: "g",
                        width = 124.dp,
                        onConfirm = { if (foodMode == Enums.FoodMode.CREATE_ENTRY) {
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
                Enums.FoodMode.CREATE -> {
                    ActionButtons(
                        onSecondaryAction = { navController.navigateUp() },
                        onPrimaryAction = { onCreateFood() },
                    )
                }
                Enums.FoodMode.CREATE_ENTRY -> {
                    ActionButtons(
                        onSecondaryAction = { navController.navigateUp() },
                        onPrimaryAction = { onCreateEntry() },
                    )
                }
                Enums.FoodMode.EDIT_AMOUNT -> {
                    ActionButtons(
                        onSecondaryAction = { navController.navigateUp() },
                        onPrimaryAction = { onEditAmount() },
                    )
                }
                else -> null
            }
        },
        screen = Enums.Screen.FOOD_EDIT,
        viewModels = viewModels,
        navController = navController,
    ) {
        when (foodMode) {
            Enums.FoodMode.CREATE -> CreateMode(navController, viewModels)
            Enums.FoodMode.CREATE_ENTRY -> EditMode(navController, viewModels, foodMode, barcode)
            Enums.FoodMode.EDIT_AMOUNT -> EditMode(navController, viewModels, foodMode, barcode)
            else -> ViewMode(navController, viewModels)
        }
    }
}
