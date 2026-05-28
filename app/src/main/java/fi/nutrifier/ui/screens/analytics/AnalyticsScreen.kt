package fi.nutrifier.ui.screens.analytics

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fi.nutrifier.ui.components.buttons.AddRecipeButton
import fi.nutrifier.ui.components.buttons.ProfileButton
import fi.nutrifier.ui.components.inputs.TimePeriodNavigator
import fi.nutrifier.ui.components.navigation.NavBar
import fi.nutrifier.ui.components.layout.TopBar
import fi.nutrifier.ui.components.layout.nutrient.NutrientColumn
import fi.nutrifier.ui.components.misc.LabelValueOrientation
import fi.nutrifier.ui.components.misc.LabeledComponent
import fi.nutrifier.ui.components.misc.StatusCircle
import fi.nutrifier.ui.components.switches.SelectionSwitch
import fi.nutrifier.ui.screens.BaseScreen
import fi.nutrifier.utils.Constants.IS_DEV
import fi.nutrifier.utils.ConversionUtils
import fi.nutrifier.utils.Enums
import fi.nutrifier.utils.FormattingUtils
import fi.nutrifier.viewmodels.ViewModelWrapper
import java.time.LocalDate
import java.time.Month

/**
 * Composable function for displaying the Analytics screen.
 * @param navController The navigation controller for navigating between screens.
 * @param viewModels The ViewModelWrapper containing the necessary view models for the screen.
 */
@Composable
fun AnalyticsScreen(
    navController: NavController,
    viewModels: ViewModelWrapper,
) {
    var weight by remember { mutableDoubleStateOf(viewModels.weight.current?.weight ?: 0.0) }
    val startDate = if (viewModels.analytics.analytics?.startOfPeriod != null) {
        LocalDate.parse(viewModels.analytics.analytics?.startOfPeriod)
    } else null
    val endDate = if (viewModels.analytics.analytics?.endOfPeriod != null) {
        LocalDate.parse(viewModels.analytics.analytics?.endOfPeriod)
    } else null

    val isPeriodYear = viewModels.analytics.analyticsTimePeriod == Enums.AnalyticsTimePeriod.YEAR

    fun handleWeightSubmit() {
        viewModels.weight.addNewWeighInt(weight)
    }

    val dates = mutableListOf<LocalDate>()
    if (startDate != null && endDate != null) {
        var currentDate: LocalDate = startDate
        while (!currentDate.isAfter(endDate)) {
            dates.add(currentDate)
            currentDate = currentDate.plusDays(1)
        }
    }

    LaunchedEffect(Unit) {
        Log.d("AnalyticsScreen", "In launched effect")
        viewModels.analytics.getAnalytics()
    }

    fun determineStatus(key: String, useDayResults: Boolean = true): Enums.Status {
        if (useDayResults) {
            return when (viewModels.analytics.analytics?.dayResults?.get(key)) {
                Enums.DayGoalResult.SUCCESS -> Enums.Status.SUCCESS
                Enums.DayGoalResult.FAILED -> Enums.Status.FAILED
                Enums.DayGoalResult.UNCERTAIN -> Enums.Status.UNCERTAIN
                else -> Enums.Status.TBD
            }
        } else {
            return when (viewModels.analytics.analytics?.monthResults?.get(key)) {
                Enums.DayGoalResult.SUCCESS -> Enums.Status.SUCCESS
                Enums.DayGoalResult.FAILED -> Enums.Status.FAILED
                Enums.DayGoalResult.UNCERTAIN -> Enums.Status.UNCERTAIN
                else -> Enums.Status.TBD
            }
        }
    }

    BaseScreen(
        topBar = { TopBar(title = "Analytics", actionButton = { ProfileButton(navController) }) },
        bottomBar = { NavBar(navController, "analytics") },
        screen = Enums.Screen.DASHBOARD,
        viewModels,
        navController,
        floatingActionButton = {
            AddRecipeButton(navController, viewModels.inspection)
        }
    ) {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
            LabeledComponent(
                label = "Time period", orientation = LabelValueOrientation.ROW,
            ) {
                SelectionSwitch(
                    value = viewModels.analytics.analyticsTimePeriod,
                    items = Enums.AnalyticsTimePeriod.entries,
                    labelMapper = { it.displayName },
                    onItemClick = { viewModels.analytics.setTimePeriod(it) }
                )
            }
        }
        Spacer(Modifier.height(24.dp))
        TimePeriodNavigator(
            selectedDate = viewModels.analytics.selectedDate,
            startDateStr = viewModels.analytics.analytics?.startOfPeriod,
            endDateStr = viewModels.analytics.analytics?.endOfPeriod,
            timePeriod = viewModels.analytics.analyticsTimePeriod,
            onDateChange = {
                Log.d("AnalyticsScreen", "TimePeriodNavigator date change: $it")
                viewModels.analytics.setSelectedDate(it)
            },
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(if (isPeriodYear) 4 else 7),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(if (isPeriodYear) 16.dp else 8.dp)
        ) {
            if (viewModels.analytics.analyticsTimePeriod == Enums.AnalyticsTimePeriod.YEAR) {
                items(Month.entries) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        // TODO: Make this element clickable and take the user to the specific day
                    ) {
                        Text(
                            text = it.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                        StatusCircle(determineStatus(
                            key = it.toString(),
                            useDayResults = false
                        ))
                    }
                }
            } else {
                items(dates) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.clickable {
                            navController.navigate("logs/${it}")
                        }
                        // TODO: Make this element clickable and take the user to the specific day
                    ) {
                        Text(
                            text = FormattingUtils.formatDateLabel(it, false, false),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                        StatusCircle(determineStatus(it.toString()))
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            NutrientColumn(
                nutrient = "Total calories consumed",
                value = ConversionUtils.convertEnergy(
                    energy = viewModels.analytics.analytics?.totalConsumedCalories ?: 0.0,
                    energyUnit = viewModels.settings.settings?.energyUnit
                ),
                suffix = viewModels.settings.settings?.energyUnit?.displayName ?: "kcal",
                color = if (viewModels.analytics.analytics?.totalConsumedCalories != null
                    && viewModels.analytics.analytics?.totalGoalCalories != null
                    && (viewModels.analytics.analytics!!.totalConsumedCalories
                            > viewModels.analytics.analytics!!.totalGoalCalories)
                    ) MaterialTheme.colorScheme.error else null
            )
            Text("/", style = MaterialTheme.typography.headlineMedium)
            NutrientColumn(
                nutrient = "Total calories goal",
                value = ConversionUtils.convertEnergy(
                    energy = viewModels.analytics.analytics?.totalGoalCalories ?: 0.0,
                    energyUnit = viewModels.settings.settings?.energyUnit
                ),
                suffix = viewModels.settings.settings?.energyUnit?.displayName ?: "kcal"
            )
        }
        Spacer(Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            NutrientColumn(
                nutrient = "Average calorie balance",
                value = ConversionUtils.convertEnergy(
                    energy = viewModels.analytics.analytics?.avgCalorieBalance ?: 0.0,
                    energyUnit = viewModels.settings.settings?.energyUnit
                ),
                style = MaterialTheme.typography.headlineSmall,
                prefix = if (viewModels.analytics.analytics?.avgCalorieBalance != null
                    && viewModels.analytics.analytics!!.avgCalorieBalance >= 0.0
                ) "+" else null,
                suffix = viewModels.settings.settings?.energyUnit?.displayName ?: "kcal",
                // TODO: Show error color when avg goes over the goal
            )
            NutrientColumn(
                nutrient = "Total calorie balance",
                value = ConversionUtils.convertEnergy(
                    energy = viewModels.analytics.analytics?.totalCalorieBalance ?: 0.0,
                    energyUnit = viewModels.settings.settings?.energyUnit
                ),
                style = MaterialTheme.typography.headlineSmall,
                prefix = if (viewModels.analytics.analytics?.avgCalorieBalance != null
                    && viewModels.analytics.analytics!!.avgCalorieBalance >= 0.0
                ) "+" else null,
                suffix = viewModels.settings.settings?.energyUnit?.displayName ?: "kcal"
            )
        }
    }
}
