package fi.nutrifier.ui.components.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun <T> Dropdown(
    value: T?,
    items: List<T>,
    modifier: Modifier = Modifier,
    isSearchable: Boolean = false,
    labelMapper: (T) -> String,
    onItemClick: (item: T) -> Unit,
) {
    // TODO: Implement search functionality

    var isExpanded by remember { mutableStateOf(false) }

    fun onClick() { isExpanded = !isExpanded }
    fun onItemClickInner(item: T) {
        onItemClick(item)
        isExpanded = false
    }

    val shape = if (isExpanded) {
        RoundedCornerShape(
            topStart = 8.dp,
            topEnd = 8.dp,
            bottomStart = 0.dp,
            bottomEnd = 0.dp,
        )
    } else {
        RoundedCornerShape(8.dp)
    }

    Surface(
        shape = shape,
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier.height(48.dp),
        onClick = { onClick() },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        ) {
            if (isSearchable) {
                BasicTextField(
                    value = if (value != null) labelMapper(value) else "Select a value",
                    onValueChange = { },
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                    textStyle = TextStyle(fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface),
                    readOnly = true,
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            } else {
                Text(if (value != null) labelMapper(value) else "Select a value")
            }
            IconButton(
                onClick = { onClick() },
                modifier = Modifier.size(32.dp),
            ) {
                Icon(
                    imageVector =
                        if (isExpanded) Icons.Rounded.KeyboardArrowUp
                        else Icons.Rounded.KeyboardArrowDown,
                    contentDescription =
                        if (isExpanded) "Close dropdown"
                        else "Open dropdown",
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = modifier
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
        ) {
            items.forEach {
                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .background(
                            if (value == it) MaterialTheme.colorScheme.surface
                            else MaterialTheme.colorScheme.surfaceVariant
                        )
                        .clickable { onItemClickInner(it) }
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = labelMapper(it),
                        color =
                            if (value == it) MaterialTheme.colorScheme.outline
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}
