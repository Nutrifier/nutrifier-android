package fi.nutrifier.ui.screens.settings.sections

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fi.nutrifier.ui.components.inputs.Dropdown
import fi.nutrifier.ui.components.inputs.NumberCounter
import fi.nutrifier.ui.components.misc.LabeledComponent

@Composable
internal fun GoalsSection() {
    var goal by remember { mutableStateOf("Select a goal") }
    var isExpanded by remember { mutableStateOf(false) }
    var current by remember { mutableIntStateOf(60) } // TODO: Fetch from user
    var target by remember { mutableIntStateOf(60) } // TODO: Fetch from user

    // TODO: Fetch users goal from the database

    Section("Goals", "Set the goals of your food logging.") {
        Dropdown(goal) {
            DropdownMenuItem(text = { Text("Maintain weight") }, { goal = "Maintain weight" })
            DropdownMenuItem(text = { Text("Loose weight") }, { goal = "Loose weight" })
            DropdownMenuItem(text = { Text("Gain weight") }, { goal = "Gain weight" })
            DropdownMenuItem(text = { Text("Just for fun") }, { goal = "Just for fun" })
        }
        Row {
            LabeledComponent("Current weight:") {
                NumberCounter(value = current,min = 2, max = 635, editable = true) // TODO: Use user's actual weight
            }
            Spacer(modifier = Modifier.width(8.dp))
            LabeledComponent("Target weight:") {
                NumberCounter(value = target, min = 2, max = 635, editable = true) // TODO: Use user's actual target
            }
        }
    }
}