package fi.nutrifier.ui.components.misc

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Minimize
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fi.nutrifier.utils.Enums

@Composable
fun StatusCircle(status: Enums.Status) {
    val determineBackgroundColor = when (status) {
        Enums.Status.SUCCESS -> MaterialTheme.colorScheme.secondary
        Enums.Status.FAILED -> MaterialTheme.colorScheme.errorContainer
        Enums.Status.UNCERTAIN -> MaterialTheme.colorScheme.outline
        else -> MaterialTheme.colorScheme.surface
    }

    val determineBorderColor = when (status) {
        Enums.Status.SUCCESS -> MaterialTheme.colorScheme.tertiary
        Enums.Status.FAILED -> MaterialTheme.colorScheme.error
        Enums.Status.UNCERTAIN -> MaterialTheme.colorScheme.scrim
        else -> MaterialTheme.colorScheme.outline
    }

    val determineIcon = when (status) {
        Enums.Status.SUCCESS -> Icons.Rounded.Check
        Enums.Status.FAILED -> Icons.Rounded.Close
        else -> Icons.Rounded.QuestionMark
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(CircleShape)
            .height(24.dp)
            .width(24.dp)
            .background(determineBackgroundColor)
            .border(
                width = 1.dp,
                color = determineBorderColor,
                shape = CircleShape
            )
    ) {
        if (status != Enums.Status.TBD) {
            Icon(
                imageVector = determineIcon,
                contentDescription = "check",
                tint = MaterialTheme.colorScheme.scrim,
                modifier = Modifier.padding(2.dp)
            )
        }
    }
}
