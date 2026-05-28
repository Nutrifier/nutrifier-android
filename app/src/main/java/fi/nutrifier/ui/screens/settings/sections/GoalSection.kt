package fi.nutrifier.ui.screens.settings.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditOff
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Replay
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import fi.nutrifier.ui.components.dialogs.GoalUpdateDialog
import fi.nutrifier.ui.components.inputs.ActionButtons
import fi.nutrifier.ui.components.inputs.DatePickerButton
import fi.nutrifier.ui.components.inputs.Dropdown
import fi.nutrifier.ui.components.inputs.NumberCounter
import fi.nutrifier.ui.components.layout.GoalPeriodItem
import fi.nutrifier.ui.components.misc.LabeledComponent
import fi.nutrifier.utils.Enums
import fi.nutrifier.viewmodels.ViewModelWrapper

@Composable
internal fun GoalsSection(viewModels: ViewModelWrapper) {
    var mealPlanEditable by remember { mutableStateOf(false) }
    var goalType by remember { mutableStateOf(Enums.GoalType.JUST_FOR_FUN) }
    var target by remember { mutableDoubleStateOf(viewModels.goals.goals?.targetWeight ?: 0.0) }
    var menuOpen by remember { mutableStateOf(false) }
    var showRecalculationDialog by remember { mutableStateOf(false) }
    val lazyListState = rememberLazyListState()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    fun handleRecalculateMealPlan() {
        viewModels.goals.calculate(true)
        mealPlanEditable = false
        menuOpen = false
    }

    fun toggleEdit() {
        mealPlanEditable = !mealPlanEditable
        menuOpen = false
    }

    // TODO: Restrict that next period can only start straight after the other one ends
    fun handleSaveClick() {
        if (viewModels.goals.goals != null) {
            viewModels.goals.updateGoals(viewModels.goals.goals!!)
        }
    }

    fun handleTargetDateChange(newTargetDate: String) {
        if (viewModels.goals.goals != null) {
            viewModels.goals.updateGoals(viewModels.goals.goals!!.copy(
                targetDate = newTargetDate
            ))

            viewModels.goals.calculate() // Don't save the recalculation yet
            showRecalculationDialog = true
        }
    }

    fun calculateScrollBarState(): List<Float> {
        if (lazyListState.scrollIndicatorState?.contentSize != null &&
            lazyListState.scrollIndicatorState?.viewportSize != null &&
            lazyListState.scrollIndicatorState?.scrollOffset != null
        ) {
            val contentSize = lazyListState.scrollIndicatorState!!.contentSize
            val viewportSize = lazyListState.scrollIndicatorState!!.viewportSize
            val currentScroll = lazyListState.scrollIndicatorState!!.scrollOffset

            val maxScroll = contentSize - viewportSize

            if (maxScroll > 0) {
                // First bar: track, scaled so it never reaches 100%
                val trackProgress = (currentScroll.toFloat() / contentSize.toFloat()).coerceIn(0f, 1f)

                // Second bar: thumb, width proportional to viewport size
                val thumbWidth = viewportSize.toFloat() / contentSize.toFloat()
                val thumbStart = (currentScroll.toFloat() / contentSize.toFloat()).coerceIn(0f, 1f)
                val thumbProgress = (thumbStart + thumbWidth).coerceIn(0f, 1f)

                return listOf(thumbProgress, trackProgress)
            }
        }
        return listOf(0f, 0f)
    }

    Section("Goals", "Set the goals of your food logging.") {
        LabeledComponent(label = "Reasoning:") {
            Dropdown(
                value = goalType,
                items = Enums.GoalType.entries.toList(),
                modifier = Modifier.width(180.dp),
                labelMapper = { it.displayName }
            ) {
                goalType = it
            }
        }
        FlowRow(
            verticalArrangement = Arrangement.Center,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LabeledComponent(label = "Target weight:") {
                NumberCounter(
                    value = target,
                    min = 2.0,
                    max = 635.0,
                    editable = true,
                )
            }
            LabeledComponent(label = "Target date:") {
                DatePickerButton(viewModels.goals.goals?.targetDate.toString()) {
                    handleTargetDateChange(it)
                }
            }
        }
        LabeledComponent(label = "Meal plan:") {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Your meal plan",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Column {
                        IconButton(onClick = { menuOpen = !menuOpen }) {
                            Icon(Icons.Default.MoreVert, "More")
                        }
                        DropdownMenu(
                            expanded = menuOpen,
                            onDismissRequest = { menuOpen = false },
                            containerColor = MaterialTheme.colorScheme.surface
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        if (mealPlanEditable) {
                                            Icon(Icons.Default.EditOff, "Edit off")
                                            Text("Cancel")
                                        } else {
                                            Icon(Icons.Default.Edit, "Edit")
                                            Text("Edit")
                                        }
                                    }
                                },
                                onClick = { toggleEdit() }
                            )
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(Icons.Rounded.Replay, "Replay")
                                        Text("Recalculate")
                                    }
                                },
                                onClick = { handleRecalculateMealPlan() }
                            )
                        }
                    }
                }
                GoalPeriodItem(viewModels, editable = mealPlanEditable)
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)
                ) {
                    LinearProgressIndicator(
                        progress = { calculateScrollBarState()[0] },
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        drawStopIndicator = {},
                        modifier = Modifier.fillMaxWidth()
                    )
                    LinearProgressIndicator(
                        progress = { calculateScrollBarState()[1] },
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        trackColor = Color.Transparent,
                        drawStopIndicator = {},
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    // TODO: Add functionality to add meal plan periods
                    if (mealPlanEditable) {
                        ActionButtons(
                            padding = PaddingValues(0.dp),
                            onSecondaryAction = { toggleEdit() },
                            onPrimaryAction = { handleSaveClick() },
                        )
                    }
                }
            }
        }
    }
    GoalUpdateDialog(viewModels, showRecalculationDialog) { showRecalculationDialog = false }
}
