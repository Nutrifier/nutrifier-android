package fi.nutrifier.ui.screens.register.steps

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
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
import fi.nutrifier.models.database.Goal
import fi.nutrifier.ui.components.inputs.NutrifierDropdown
import fi.nutrifier.ui.components.inputs.NumberCounter
import fi.nutrifier.ui.components.misc.LabeledComponent
import fi.nutrifier.ui.components.misc.TitleSubtitle
import fi.nutrifier.utils.Constants
import fi.nutrifier.utils.ConversionUtils
import fi.nutrifier.utils.Enums
import fi.nutrifier.utils.FormattingUtils
import fi.nutrifier.utils.GoalErrors
import fi.nutrifier.utils.RegistrationFormState
import fi.nutrifier.viewmodels.ViewModelWrapper
import java.time.LocalDate

@Composable
fun GoalsStep(
    viewModels: ViewModelWrapper,
    navController: NavController,
    registrationFormState: RegistrationFormState,
    setRegistrationFormState: (RegistrationFormState) -> Unit,
    goal: Goal,
    updateGoals: (Goal) -> Unit,
) {
    var targetDate by remember { mutableStateOf(FormattingUtils.formatDateStr(registrationFormState.goal.targetDate)) }
    var isDatePickerOpen by remember { mutableStateOf(false) }

    val today = LocalDate.now()
    val state = rememberDatePickerState(
        initialSelectedDateMillis = if (registrationFormState.goal.targetDate != null) {
            ConversionUtils.dateStrToMillis(registrationFormState.goal.targetDate)
        } else null,
        initialDisplayMode = DisplayMode.Picker,
        yearRange = today.year..today.plusYears(10).year,
    )

    val selectedDateAsLocalDate = ConversionUtils.millisToLocalDate(state.selectedDateMillis)
    val selectedDateAsString = FormattingUtils.localDateToString(selectedDateAsLocalDate)

    val weightSuffix = when (viewModels.settings.settings?.weightUnit) {
        Enums.FoodWeightUnit.GRAMS -> "kg"
        else -> viewModels.settings.settings?.weightUnit?.displayName
    } ?: "kg"

    LaunchedEffect(goal) {
        Log.d("WeightStep", "Goals: $goal")
    }

    TitleSubtitle(
        title = "Set Your Goals",
        subtitle = "Set your current weight, target weight, and when you plan to reach it.",
        alignment = Alignment.CenterHorizontally,
    )
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LabeledComponent(
            label = "Reasoning",
            error = registrationFormState.goalErrors.reasoning,
        ) {
            NutrifierDropdown(
                value = registrationFormState.goal.goalType,
                items = Enums.GoalType.entries.toList(),
                labelMapper = { it.displayName },
                modifier = Modifier.width(152.dp)
            ) {
                //updateGoals(goals.copy(reasoning = it))
                setRegistrationFormState(registrationFormState.copy(
                    goalErrors = GoalErrors(),
                    goal = registrationFormState.goal.copy(
                        goalType = it
                    )
                ))
            }
        }
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            LabeledComponent(
                label = "Reached by date",
                error = registrationFormState.goalErrors.targetDate,
            ) {
                TextButton(
                    onClick = { isDatePickerOpen = !isDatePickerOpen },
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                ) {
                    Text(
                        text = targetDate ?: "Select a date",
                        color = if (registrationFormState.goal.targetDate != null) {
                            MaterialTheme.colorScheme.onSurface
                        } else {
                            MaterialTheme.colorScheme.outline
                        }
                    )
                }
            }
            LabeledComponent(
                label = "Target weight (${weightSuffix})",
                error = registrationFormState.goalErrors.targetWeight,
            ) {
                NumberCounter(
                    value = registrationFormState.goal.targetWeight ?: 0.0,
                    onNumberChange = {
                        //updateGoals(goals.copy(targetWeight = it))
                        setRegistrationFormState(registrationFormState.copy(
                            goalErrors = GoalErrors(),
                            goal = registrationFormState.goal.copy(
                                targetWeight = it
                            )
                        ))
                    },
                    min = Constants.MIN_WEIGHT,
                    max = Constants.MAX_WEIGHT,
                    editable = true,
                )
            }
        }
    }
    if (isDatePickerOpen) {
        DatePickerDialog(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            onDismissRequest = { isDatePickerOpen = false },
            confirmButton = {
                TextButton(onClick = {
                    isDatePickerOpen = false
                    targetDate = FormattingUtils.localDateToString(
                        date = selectedDateAsLocalDate,
                    )
                    //updateGoals(goals.copy(targetDate = selectedDateAsLocalDate.toString()))
                    setRegistrationFormState(registrationFormState.copy(
                        goalErrors = GoalErrors(),
                        goal = registrationFormState.goal.copy(
                            targetDate = selectedDateAsLocalDate.toString()
                        )
                    ))
                }) {
                    Text("Select")
                }
            },
            dismissButton = {
                TextButton(onClick = { isDatePickerOpen = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(
                state = state,
                showModeToggle = true,
                title = {
                    Text("Select a date", modifier = Modifier.padding(16.dp))
                },
                headline = {
                    Text(
                        text = selectedDateAsString ?: "No date selected",
                        modifier = Modifier.padding(16.dp),
                    )
                },
            )
        }
    }
}
