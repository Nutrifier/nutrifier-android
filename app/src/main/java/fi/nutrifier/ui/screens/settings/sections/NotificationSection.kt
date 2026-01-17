package fi.nutrifier.ui.screens.settings.sections

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fi.nutrifier.viewmodels.ViewModelWrapper
import kotlin.time.measureTime

@Composable
internal fun NotificationSection(viewModels: ViewModelWrapper) {
    var isExpanded by remember { mutableStateOf(false) }

    Section("Notifications", "Set which notifications you want to receive.") {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            /*
            Switch(viewModels.user.settings.mealReminderEnabled ?: true, onCheckedChange = {
                viewModels.user.updateSettings(mealReminderEnabled = it)
            })
            */
            Spacer(modifier = Modifier.width(8.dp))
            Text("Meal reminders")
        }
    }
}
