package fi.nutrifier.ui.components.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fi.nutrifier.ui.components.inputs.DatePickerButton
import fi.nutrifier.ui.components.inputs.NumberCounter
import fi.nutrifier.ui.components.inputs.NutrientInputRow
import fi.nutrifier.ui.components.misc.LabeledComponent
import fi.nutrifier.utils.Constants
import fi.nutrifier.utils.FormattingUtils
import fi.nutrifier.viewmodels.ViewModelWrapper

@Composable
fun MealPlan(
    viewModels: ViewModelWrapper,
    modifier: Modifier = Modifier,
    editable: Boolean = false,
    showPeriodNumber: Boolean = true,
    showDeleteButton: Boolean = false,
    saveAfterValueChange: Boolean = false,
) {
    var startDateError by remember { mutableStateOf<String?>(null) }
    var endDateError by remember { mutableStateOf<String?>(null) }

    fun handleStartDateChange(newDate: String) {
        /*

        val updatedPeriod = period.copy(startDate = newDate)
        viewModels.goals.updatePeriod(periodIndex, updatedPeriod, saveAfterValueChange)
         */
    }

    fun handleEndDateChange(newDate: String) {
        /*
        val updatedPeriod = period.copy(endDate = newDate)
        viewModels.goals.updatePeriod(periodIndex, updatedPeriod, saveAfterValueChange)
         */
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        if (editable) {
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                LabeledComponent(
                    label = "Start date",
                    error = startDateError,
                ) {
                    DatePickerButton(viewModels.goals.goals?.startDate) { handleStartDateChange(it) }
                }
                LabeledComponent(
                    label = "End date",
                    error = endDateError,
                ) {
                    DatePickerButton(viewModels.goals.goals?.targetDate) { handleEndDateChange(it) }
                }
            }
                Spacer(modifier = Modifier.height(16.dp))
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                LabeledComponent(
                    label = "Daily calories (${viewModels.settings.settings?.energyUnit?.displayName ?: "kcal"})",
                ) {
                    NumberCounter(
                        value = viewModels.goals.goals?.dailyCalorieTarget ?: 0.0,
                        onNumberChange = { newCalories ->
                            /*
                            val updatedPeriod = period.copy(calorieTarget = newCalories)
                            viewModels.goals.updatePeriod(
                                periodIndex,
                                updatedPeriod,
                                saveAfterValueChange
                            )
                            // TODO: Add manual updating of calorie target
                            */
                        },
                        min = Constants.MIN_KCAL,
                        max = Constants.MAX_KCAL,
                        editable = true,
                    )
                }
                LabeledComponent(
                    label = "Daily fat (g)",
                ) {
                    NumberCounter(
                        value = viewModels.goals.goals?.dailyFatTarget ?: 0.0,
                        onNumberChange = { newFat ->
                            /*
                            val updatedPeriod = period.copy(fatTarget = newFat)
                            viewModels.goals.updatePeriod(
                                periodIndex,
                                updatedPeriod,
                                saveAfterValueChange
                            )
                            // TODO: Add manual updating of calorie target
                            */
                        },
                        min = Constants.MIN_FAT,
                        max = Constants.MAX_FAT,
                        editable = true,
                    )
                }
            }
            FlowRow(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                LabeledComponent(
                    label = "Daily carbs (g)",
                ) {
                    NumberCounter(
                        value = viewModels.goals.goals?.dailyCarbTarget ?: 0.0,
                        onNumberChange = { newCarbs ->
                            /*
                            val updatedPeriod = period.copy(carbTarget = newCarbs)
                            viewModels.goals.updatePeriod(periodIndex, updatedPeriod, saveAfterValueChange)
                            // TODO: Add manual updating of calorie target
                            */
                        },
                        min = Constants.MIN_CARBS,
                        max = Constants.MAX_CARBS,
                        editable = true,
                    )
                }
                LabeledComponent(
                    label = "Daily protein (g)",
                ) {
                    NumberCounter(
                        value = viewModels.goals.goals?.dailyProteinTarget ?: 0.0,
                        onNumberChange = { newProtein ->
                            /*
                            val updatedPeriod = period.copy(proteinTarget = newProtein)
                            viewModels.goals.updatePeriod(periodIndex, updatedPeriod, saveAfterValueChange)
                            // TODO: Add manual updating of calorie target
                            */
                        },
                        min = Constants.MIN_PROTEIN,
                        max = Constants.MAX_PROTEIN,
                        editable = true,
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        } else {
            Text("${FormattingUtils.formatDateStr(viewModels.goals.goals?.startDate.toString())} - ${FormattingUtils.formatDateStr(viewModels.goals.goals?.targetDate.toString())}")
            Spacer(modifier = Modifier.height(8.dp))
            NutrientInputRow(
                text = "Daily calories",
                value = "${viewModels.goals.goals?.dailyCalorieTarget ?: 0.0}",
                suffixText = viewModels.settings.settings?.energyUnit?.displayName ?: "kcal",
                editable = false,
            )
            NutrientInputRow(
                text = "Daily fat",
                value = "${viewModels.goals.goals?.dailyFatTarget ?: 0.0}",
                suffixText = "g",
                editable = false,
            )
            NutrientInputRow(
                text = "Daily carbs",
                value = "${viewModels.goals.goals?.dailyCarbTarget ?: 0.0}",
                suffixText = "g",
                editable = false,
            )
            NutrientInputRow(
                text = "Daily protein",
                value = "${viewModels.goals.goals?.dailyProteinTarget ?: 0.0}",
                suffixText = "g",
                editable = false,
            )
        }
    }
}
