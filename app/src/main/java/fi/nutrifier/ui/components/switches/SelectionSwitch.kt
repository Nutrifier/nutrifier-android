package fi.nutrifier.ui.components.switches

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> SelectionSwitch(
    value: T?,
    items: List<T>,
    modifier: Modifier = Modifier,
    labelMapper: (T) -> String,
    onItemClick: (item: T) -> Unit,
) {
    Row(modifier = modifier) {
        items.mapIndexed { index, item ->
            val isFirstItem = index == 0
            val isLastItem = index == items.size - 1

            TextButton(
                onClick = { onItemClick(item) },
                shape = RoundedCornerShape(
                    topStart = if (isFirstItem) 8.dp else 0.dp,
                    topEnd = if (isLastItem) 8.dp else 0.dp,
                    bottomEnd = if (isLastItem) 8.dp else 0.dp,
                    bottomStart = if (isFirstItem) 8.dp else 0.dp
                ),
                colors = if (value == item) {
                    ButtonDefaults.buttonColors().copy(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                } else {
                    ButtonDefaults.buttonColors().copy(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                }
            ) {
                Text(labelMapper(item))
            }
        }
    }
}
