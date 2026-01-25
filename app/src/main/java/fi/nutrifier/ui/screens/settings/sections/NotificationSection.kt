package fi.nutrifier.ui.screens.settings.sections

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fi.nutrifier.viewmodels.ViewModelWrapper

@Composable
internal fun NotificationSection(viewModels: ViewModelWrapper) {

    Section("Notifications", "Set which notifications you want to receive.") {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Switch(viewModels.user.settings?.mealReminderEnabled ?: true, onCheckedChange = {
                val updatedSettings = viewModels.user.settings?.copy(mealReminderEnabled = it)
                if (updatedSettings != null) viewModels.user.updateSettings(updatedSettings)
            })
            Spacer(modifier = Modifier.width(8.dp))
            Text("Meal reminders")
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Switch(viewModels.user.settings?.motivationMessagesEnabled ?: true, onCheckedChange = {
                val updatedSettings = viewModels.user.settings?.copy(motivationMessagesEnabled = it)
                if (updatedSettings != null) viewModels.user.updateSettings(updatedSettings)
            })
            Spacer(modifier = Modifier.width(8.dp))
            Text("Motivation messages")
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Switch(viewModels.user.settings?.weighInReminderEnabled ?: true, onCheckedChange = {
                val updatedSettings = viewModels.user.settings?.copy(weighInReminderEnabled = it)
                if (updatedSettings != null) viewModels.user.updateSettings(updatedSettings)
            })
            Spacer(modifier = Modifier.width(8.dp))
            Text("Weigh in reminders")
        }
    }
}
