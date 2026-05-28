package fi.nutrifier.ui.screens.register.steps

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import fi.nutrifier.models.database.GoalPeriod
import fi.nutrifier.ui.components.layout.GoalPeriodItem
import fi.nutrifier.ui.components.misc.LabeledComponent
import fi.nutrifier.ui.components.misc.TitleSubtitle
import fi.nutrifier.viewmodels.ViewModelWrapper

data class PeriodPickerState(
    val startDatePickerState: DatePickerState,
    val endDatePickerState: DatePickerState,
    var isStartDatePickerOpen: Boolean = false,
    var isEndDatePickerOpen: Boolean = false,
)

@Composable
fun MealPlanStep(viewModels: ViewModelWrapper) {
    fun emptyMealPlanPeriod(): GoalPeriod {
        return GoalPeriod(
            periodType = null,
            startDate = null,
            endDate = null,
            calorieTarget = null,
            carbTarget = null,
            fatTarget = null,
            proteinTarget = null,
        )
    }

    TitleSubtitle(
        title = "Setup a Meal Plan",
        subtitle = "We have created you a default meal plan based on your goal. These are rough estimates and you can modify these as you please.",
        alignment = Alignment.CenterHorizontally,
    )
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        GoalPeriodItem(
            viewModels,
            editable = true
        )
        Spacer(modifier = Modifier.height(60.dp))
    }

}
