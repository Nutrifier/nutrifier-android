package fi.nutrifier.ui.components.layout

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import fi.nutrifier.models.database.Log
import fi.nutrifier.ui.components.misc.ItemDivider
import fi.nutrifier.viewmodels.ViewModelWrapper

@Composable
fun LogsSection(
    title: String,
    logs: List<Log>,
    viewModels: ViewModelWrapper,
) {
    TitledContainer(title) {
        if (logs.isEmpty()) {
            Text(
                text = "No foods logged.",
                style = TextStyle(color = MaterialTheme.colorScheme.outline)
            )
        } else {
            logs.forEachIndexed { index, log ->
                LogRow(log = log, viewModel = viewModels.logsScreen)
                if (index < logs.size - 1) ItemDivider()
            }
        }
    }
}