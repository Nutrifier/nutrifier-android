package fi.nutrifier.ui.components.misc

import android.accessibilityservice.GestureDescription
import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun Tag(content: @Composable () -> Unit) {
    Surface(shape = RoundedCornerShape(16.dp)) {
        content()
    }
}

@Composable
fun TextTag(text: String, textStyle: TextStyle? = null) {
    Tag {
        Text(
            text = text,
            style = textStyle ?: MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun IconTag(imageVector: ImageVector, contentDescription: String) {
    Tag {
        Box(modifier = Modifier.padding(16.dp, 4.dp)) {
            androidx.compose.material3.Icon(
                imageVector = imageVector,
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
