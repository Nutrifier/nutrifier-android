package fi.nutrifier.ui.components.buttons

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Composable function that displays a button for removing an item.
 *
 * @param index The index of the item to be removed.
 * @param onClick The callback function to be invoked when the button is clicked.
 */
@Composable
fun RemoveButton(index: Int, onClick: (Int) -> Unit) {
    IconButton(
        onClick = { onClick(index) },
        modifier = Modifier.height(20.dp).width(20.dp),
        colors = IconButtonDefaults.iconButtonColors().copy(
            contentColor = MaterialTheme.colorScheme.error
        )
    ) {
        Icon(
            imageVector = Icons.Rounded.Clear,
            contentDescription = "delete",
            modifier = Modifier.height(16.dp).width(16.dp)
        )
    }
}
