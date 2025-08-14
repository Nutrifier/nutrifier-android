package fi.nutrifier.ui.components.buttons

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DeleteButton(
    connectToLeft: Boolean = false,
    connectToRight: Boolean = false,
    onClick: () -> Unit
){
    val shape = if (connectToLeft && connectToRight) {
        RoundedCornerShape(0.dp)
    } else if (connectToLeft) {
        RoundedCornerShape(topStart = 0.dp, topEnd = 8.dp, bottomStart = 0.dp, bottomEnd = 8.dp)
    } else if (connectToRight) {
        RoundedCornerShape(topStart = 8.dp, topEnd = 0.dp, bottomStart = 8.dp, bottomEnd = 0.dp)
    } else {
        RoundedCornerShape(8.dp)
    }

    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
        ),
        onClick = { onClick() },
        shape = shape,
        modifier = Modifier.height(40.dp).width(40.dp),
        contentPadding = PaddingValues(vertical = 4.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.Delete,
            contentDescription = "delete",
            modifier = Modifier.size(20.dp)
        )
    }
}