package fi.nutrifier.ui.components.misc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeBackgroundElement(
    swipeState: DismissState,
    icon: ImageVector,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    iconColor: Color = MaterialTheme.colorScheme.onPrimary,
    right: Boolean = false,
) {
    Box(
        contentAlignment = if (right) Alignment.CenterEnd else Alignment.CenterStart,
        modifier = Modifier
        .padding(horizontal = 2.dp)
        .clip(RoundedCornerShape(4.dp))
        .fillMaxSize()
        .background(backgroundColor)
    ){
        Icon(
            imageVector = icon,
            contentDescription = "add to shopping cart",
            tint = iconColor,
            modifier = Modifier
                .padding(horizontal = 4.dp, vertical = 2.dp)
                .size((swipeState.progress * 100).dp)
        )
    }
}
 */