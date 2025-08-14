package fi.nutrifier.ui.components.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteSweep
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import fi.nutrifier.models.room.ShoppingListItem
import fi.nutrifier.ui.components.misc.SwipeBackgroundElement
import fi.nutrifier.ui.components.misc.CheckCircle
import fi.nutrifier.viewmodels.ShoppingListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListItemRow(index: Int, item: ShoppingListItem, viewModel: ShoppingListViewModel) {
    var checked by remember { mutableStateOf(false) }
    val swipeState = rememberDismissState()

    fun handleListItemClick() {
        checked = !checked
    }

    LaunchedEffect(swipeState.currentValue) {
        if (swipeState.currentValue == DismissValue.DismissedToStart) {
            viewModel.delete(index)
            swipeState.reset()
        }
    }

    SwipeToDismiss(
        state = swipeState,
        background = {
            SwipeBackgroundElement(
                swipeState = swipeState,
                icon = Icons.Rounded.DeleteSweep, right = true,
                backgroundColor = MaterialTheme.colorScheme.errorContainer,
                iconColor = MaterialTheme.colorScheme.onErrorContainer
            ) },
        directions = setOf(DismissDirection.EndToStart),
        dismissContent = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .clip(RoundedCornerShape(
                        topStart = 8.dp,
                        bottomStart = 8.dp,
                        topEnd = if (swipeState.progress < 1) 0.dp else 8.dp,
                        bottomEnd = if (swipeState.progress < 1) 0.dp else 8.dp
                    ))
                    .fillMaxWidth()
                    .clickable { handleListItemClick() }
                    .weight(0.8f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    CheckCircle(checked)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleMedium,
                        textDecoration =
                        if (checked) TextDecoration.LineThrough
                        else null
                    )
                }
                Text(
                    text = item.note,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    )
}
