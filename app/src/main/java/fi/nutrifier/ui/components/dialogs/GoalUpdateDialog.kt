package fi.nutrifier.ui.components.dialogs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fi.nutrifier.models.database.GoalPeriod
import fi.nutrifier.ui.components.inputs.ActionButtons
import fi.nutrifier.ui.components.layout.GoalPeriodItem
import fi.nutrifier.ui.components.misc.LabeledComponent
import fi.nutrifier.viewmodels.ViewModelWrapper

@Composable
fun GoalUpdateDialog(
    viewModels: ViewModelWrapper,
    isVisible: Boolean,
    onDismiss: () -> Unit,
) {
    NutrifierDialog(
        isVisible = isVisible,
        onDismiss = { onDismiss() },
        title = "Do you want to update your meal plan?",
        actionButtons = {
            ActionButtons(
                padding = PaddingValues(0.dp),
                onPrimaryAction = {
                    if (viewModels.goals.newGoals != null) {
                        viewModels.goals.updateGoals(viewModels.goals.newGoals!!)
                    }
                },
                onSecondaryAction = { onDismiss() },
                primaryActionButtonText = "Yes, please!",
                secondaryActionButtonText = "No, thanks!"
            )
        }
    ) {
        Text("This change could affect your meal plan. Do you want to update your meal plan?")
        Spacer(modifier = Modifier.height(16.dp))
        GoalPeriodItem(viewModels)
    }
}
