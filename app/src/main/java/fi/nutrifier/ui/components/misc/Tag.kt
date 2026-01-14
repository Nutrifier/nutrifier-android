package fi.nutrifier.ui.components.misc

import android.graphics.fonts.FontStyle
import android.util.Size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun Tag(text: String, textStyle: TextStyle? = null) {
    Surface(shape = RoundedCornerShape(16.dp)) {
        Text(
            text = text,
            style = textStyle ?: MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}

@Preview
@Composable
fun TagPreviewSmall() {
    Tag("Tag", textStyle = MaterialTheme.typography.labelSmall)
}

@Preview
@Composable
fun TagPreviewMedium() {
    Tag("Tag")
}

@Preview
@Composable
fun TagPreviewSLarge() {
    Tag("Tag", textStyle = MaterialTheme.typography.labelLarge)
}