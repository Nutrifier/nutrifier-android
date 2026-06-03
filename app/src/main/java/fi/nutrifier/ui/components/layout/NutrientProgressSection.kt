package fi.nutrifier.ui.components.layout

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fi.nutrifier.ui.components.layout.nutrient.progress.indicator.NutrientProgressIndicator
import fi.nutrifier.ui.components.switches.NutrientModeSwitch
import fi.nutrifier.utils.ConversionUtils
import fi.nutrifier.utils.Enums
import fi.nutrifier.viewmodels.ViewModelWrapper
import java.time.LocalDate

@Composable
fun NutrientProgressSection(viewModels: ViewModelWrapper) {
    var showMacros by remember { mutableStateOf(false) }
    val isLineMode = viewModels.settings.settings?.nutrientDisplayMode == Enums.NutrientDisplayMode.LINE

    fun determineValueToShow(nutrition: Enums.Nutrition): Double {
        return when (nutrition) {
            Enums.Nutrition.CALORIES ->
                if (viewModels.foodEntry.summary?.caloriesConsumed != null) {
                    ConversionUtils.convertEnergy(
                        energy = viewModels.foodEntry.summary!!.caloriesConsumed,
                        energyUnit = viewModels.settings.settings?.energyUnit,
                    )
                } else 0.0
            Enums.Nutrition.FAT ->
                viewModels.foodEntry.summary?.fatConsumed ?: 0.0
            Enums.Nutrition.CARBS ->
                viewModels.foodEntry.summary?.carbsConsumed ?: 0.0
            else ->
                viewModels.foodEntry.summary?.proteinConsumed ?: 0.0
        }
    }

    // If max value has a value for overall nutrient, we use that - else current goal period
    fun determineMaxValueToUse(nutrition: Enums.Nutrition): Double {
        if (viewModels.foodEntry.summary == null && viewModels.goals.goals == null) return 0.0

        val isInPast = LocalDate.now().isAfter(viewModels.foodEntry.selectedDate.value)

        val useDailySummaryValue = when (nutrition) {
            Enums.Nutrition.CALORIES -> if (viewModels.foodEntry.summary?.calorieTarget != null && isInPast) {
                Log.d("NutrientProgressSection", "Using summary: ${viewModels.foodEntry.summary?.calorieTarget}")
                viewModels.foodEntry.summary!!.calorieTarget
            } else {
                Log.d("NutrientProgressSection", "Using goal: ${viewModels.goals.goals?.dailyCalorieTarget ?: 1.0}")
                viewModels.goals.goals?.dailyCalorieTarget ?: 1.0
            }
            Enums.Nutrition.FAT -> if (viewModels.foodEntry.summary?.fatTarget != null && isInPast) {
                viewModels.foodEntry.summary!!.fatTarget
            } else viewModels.goals.goals?.dailyFatTarget ?: 1.0
            Enums.Nutrition.CARBS -> if (viewModels.foodEntry.summary?.carbTarget != null && isInPast) {
                viewModels.foodEntry.summary!!.carbTarget
            } else viewModels.goals.goals?.dailyCalorieTarget ?: 1.0
            Enums.Nutrition.PROTEIN -> if (viewModels.foodEntry.summary?.proteinTarget != null && isInPast) {
                viewModels.foodEntry.summary!!.proteinTarget
            } else viewModels.goals.goals?.dailyProteinTarget ?: 1.0
        }

        // TODO: First use dailySummary target values, then goal targets
        return when (nutrition) {
            Enums.Nutrition.CALORIES ->
                ConversionUtils.convertEnergy(
                    energy = useDailySummaryValue,
                    energyUnit = viewModels.settings.settings?.energyUnit
                )
            Enums.Nutrition.FAT -> viewModels.goals.goals?.dailyFatTarget ?: 1.0
            Enums.Nutrition.CARBS -> viewModels.goals.goals?.dailyCarbTarget ?: 1.0
            else -> viewModels.goals.goals?.dailyProteinTarget ?: 1.0
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp),
    ) {
        val radius = maxWidth * 0.12f
        val centerYOffset = maxHeight * 0.16f

        Box(modifier = Modifier.align(Alignment.TopCenter)) {
            NutrientProgressIndicator(
                settingsViewModel = viewModels.settings,
                value = determineValueToShow(Enums.Nutrition.CALORIES),
                max = determineMaxValueToUse(Enums.Nutrition.CALORIES),
                title = "Energy",
                color = MaterialTheme.colorScheme.secondary,
                valueColor = if (viewModels.foodEntry.summary?.caloriesConsumed == null) {
                    MaterialTheme.colorScheme.outline
                } else {
                    MaterialTheme.colorScheme.onBackground
                },
                maxColor = if (viewModels.foodEntry.summary == null && viewModels.goals.currentGoalPeriod == null) {
                    MaterialTheme.colorScheme.outline
                } else {
                    MaterialTheme.colorScheme.onBackground
                },
                suffix = viewModels.settings.settings?.energyUnit?.displayName ?: "kcal",
                showMax = false,
                showLeftValue = true,
                onClick = { showMacros = !showMacros }
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = radius, top = radius + centerYOffset)
        ) {
            NutrientProgressIndicator(
                settingsViewModel = viewModels.settings,
                value = determineValueToShow(Enums.Nutrition.FAT),
                max = determineMaxValueToUse(Enums.Nutrition.FAT),
                title = "Fats",
                color = MaterialTheme.colorScheme.errorContainer,
                valueColor = if (viewModels.foodEntry.summary?.caloriesConsumed == null) {
                    MaterialTheme.colorScheme.outline
                } else {
                    MaterialTheme.colorScheme.onBackground
                },
                maxColor = if (viewModels.foodEntry.summary == null && viewModels.goals.currentGoalPeriod == null) {
                    MaterialTheme.colorScheme.outline
                } else {
                    MaterialTheme.colorScheme.onBackground
                },
                size = Enums.IndicatorSize.SMALL,
                suffix = "g",
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(top = radius + centerYOffset)
        ) {
            NutrientProgressIndicator(
                settingsViewModel = viewModels.settings,
                value = determineValueToShow(Enums.Nutrition.CARBS),
                max = determineMaxValueToUse(Enums.Nutrition.CARBS),
                title = "Carbohydrates",
                color = MaterialTheme.colorScheme.primary,
                valueColor = if (viewModels.foodEntry.summary?.caloriesConsumed == null) {
                    MaterialTheme.colorScheme.outline
                } else {
                    MaterialTheme.colorScheme.onBackground
                },
                maxColor = if (viewModels.foodEntry.summary == null && viewModels.goals.currentGoalPeriod == null) {
                    MaterialTheme.colorScheme.outline
                } else {
                    MaterialTheme.colorScheme.onBackground
                },
                size = Enums.IndicatorSize.SMALL,
                suffix = "g",
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = radius, top = radius + centerYOffset)
        ) {
            NutrientProgressIndicator(
                settingsViewModel = viewModels.settings,
                value = determineValueToShow(Enums.Nutrition.PROTEIN),
                max = determineMaxValueToUse(Enums.Nutrition.PROTEIN),
                title = "Protein",
                color = MaterialTheme.colorScheme.tertiary,
                valueColor = if (viewModels.foodEntry.summary?.caloriesConsumed == null) {
                    MaterialTheme.colorScheme.outline
                } else {
                    MaterialTheme.colorScheme.onBackground
                },
                maxColor = if (viewModels.foodEntry.summary == null && viewModels.goals.currentGoalPeriod == null) {
                    MaterialTheme.colorScheme.outline
                } else {
                    MaterialTheme.colorScheme.onBackground
                },
                size = Enums.IndicatorSize.SMALL,
                suffix = "g",
            )
        }
        /* TODO: Add this back when line mode is implemented
        Box(modifier = Modifier.align(Alignment.TopEnd)) {
            NutrientModeSwitch(viewModels.settings)
        }*/
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 24.dp, top = centerYOffset)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Goal",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = "${determineMaxValueToUse(Enums.Nutrition.CALORIES).toInt()}",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 24.dp, top = centerYOffset)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Eaten",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = "${determineValueToShow(Enums.Nutrition.CALORIES).toInt()}",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
}
