package fi.nutrifier.ui.components.misc

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun Tag(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    isError: Boolean = false,
    isHighlighted: Boolean = false,
    content: @Composable () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isError) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            width = if (isError || isHighlighted) 2.dp else 1.dp,
            color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline
        ),
        modifier = modifier.clip(RoundedCornerShape(16.dp)).clickable {
            if (onClick != null) {
                onClick()
            }
        },
    ) {
        content()
    }
}

@Composable
fun TextTag(
    text: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    isError: Boolean = false,
    isHighlighted: Boolean = false,
    textStyle: TextStyle? = null,
) {
    Tag(
        modifier = modifier,
        onClick = onClick,
        isError = isError,
        isHighlighted = isHighlighted,
    ) {
        Text(
            text = text,
            style = textStyle ?: MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun IconTag(
    imageVector: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    isError: Boolean = false,
) {
    Tag(
        modifier = modifier,
        onClick = onClick,
        isError = isError
    ) {
        Box(modifier = Modifier.padding(16.dp, 4.dp)) {
            Icon(
                imageVector = imageVector,
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
