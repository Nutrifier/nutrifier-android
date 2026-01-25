package fi.nutrifier.ui.screens.settings.sections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Section(
    title: String? = null,
    subtitle: String? = null,
    isCollapsible: Boolean = true,
    content: @Composable () -> Unit,
) {
    var isExpanded by remember { mutableStateOf(true) }

    fun onClick() { if (isCollapsible) isExpanded = !isExpanded }

    Column {
        Row(
            modifier = Modifier.fillMaxWidth().clickable { onClick() },
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                if (title != null) {
                    Text(title, style = MaterialTheme.typography.headlineMedium)
                }
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline,
                    )
                }
            }
            if (isCollapsible) {
                IconButton({ onClick() }) {
                    if (isExpanded) {
                        Icon(Icons.Rounded.KeyboardArrowUp, "Collapse dropdown")
                    } else {
                        Icon(Icons.Rounded.KeyboardArrowDown, "Expand dropdown")
                    }
                }
            }
        }
        AnimatedVisibility(
            visible = if (isCollapsible) isExpanded else true,
            enter = expandVertically(animationSpec = tween(300)) + fadeIn(animationSpec = tween(300)),
            exit = shrinkVertically(animationSpec = tween(300)) + fadeOut(animationSpec = tween(300)),
        ) {
            Column(
                modifier = Modifier.padding(start = 4.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                content()
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}