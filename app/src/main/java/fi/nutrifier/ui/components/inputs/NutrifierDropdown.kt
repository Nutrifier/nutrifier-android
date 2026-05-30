package fi.nutrifier.ui.components.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@Composable
fun <T> NutrifierDropdown(
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

    NutrifierTextField(
        value = if (value != null) labelMapper(value) else "-",
        onValueChange = { },
        readOnly = true,
        singleLine = true,
        modifier = modifier,
        trailingIcon = {
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
    )
    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = { isExpanded = false },
        offset = DpOffset(16.dp, (-6).dp),
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.surface),
    ) {
        items.forEach {
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .background(
                        if (value == it) MaterialTheme.colorScheme.surfaceVariant
                        else MaterialTheme.colorScheme.surface
                    )
                    .clickable { onItemClickInner(it) }
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = labelMapper(it),
                    color =
                        if (value == it) MaterialTheme.colorScheme.outline
                        else MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}
