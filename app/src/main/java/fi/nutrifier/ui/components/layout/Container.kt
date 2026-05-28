package fi.nutrifier.ui.components.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun TitledContainer(
    modifier: Modifier = Modifier,
    title: String? = null,
    actionButton: (@Composable () -> Unit)? = null,
    titleSize: TextStyle = MaterialTheme.typography.headlineMedium,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    borderColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    content: @Composable () -> Unit
) {
    val titleSpacing = when (titleSize) {
        MaterialTheme.typography.titleSmall -> Modifier.padding(vertical = 9.dp)
        MaterialTheme.typography.titleMedium -> Modifier.padding(vertical = 9.dp)
        MaterialTheme.typography.titleLarge -> Modifier.padding(vertical = 12.dp)
        MaterialTheme.typography.headlineSmall -> Modifier.padding(vertical = 9.dp)
        MaterialTheme.typography.headlineLarge -> Modifier.padding(vertical = 13.5.dp)
        else -> Modifier.padding(vertical = 10.dp)
    }

    Box(modifier = modifier) {
        Column {
            if (title != null) {
                Spacer(modifier = titleSpacing)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, borderColor, RoundedCornerShape(8.dp))
                .clip(RoundedCornerShape(8.dp))
                .background(backgroundColor)
                .padding(20.dp, 24.dp, 24.dp, 12.dp)
            ) {
                content()
            }
        }
        Row(
            horizontalArrangement = if (title != null) Arrangement.SpaceBetween else Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            if (title != null) {
                Text(title,
                    style = titleSize,
                    modifier = Modifier
                        .padding(start = 16.dp, top = 8.dp)
                        .background(
                            Brush.linearGradient(
                            start = Offset(0f, 0f),
                            end = Offset(0f, 100f),
                            colors = listOf(
                                MaterialTheme.colorScheme.background,
                                backgroundColor,
                            ),
                        ))
                )
            }
            if (actionButton != null) actionButton()
        }

    }
}
