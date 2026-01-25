package fi.nutrifier.ui.components.layout.nutrient.progress.indicator.modes

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.google.firebase.encoders.annotations.Encodable
import fi.nutrifier.utils.GraphicsUtils.valueToWheelDrawn

@Composable
fun FullCircleMode(value: Double, max: Double, onClickInner: () -> Unit) {
    /*
    Canvas(modifier = Modifier
        .size(circleSize)
        .clickable { onClickInner() }) {
        drawArc(
            color = backgroundColor,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = true,
        )
        drawArc(
            color = color,
            startAngle = 90f,
            sweepAngle = drawnArea,
            useCenter = true,
        )
    }
    Box(modifier = Modifier
        .size(innerCircleSize)
        .clip(CircleShape)
        .background(MaterialTheme.colorScheme.background)
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (size === "large") Text(text = title)
        Text(
            text = value.toInt().toString(),
            style =
                if(size === "large") MaterialTheme.typography.headlineMedium
                else MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(4.dp))
        HorizontalDivider(
            modifier = Modifier.width(24.dp),
            thickness = 1.dp,
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = if (suffix != null) "$max $suffix" else "/$max",
            style = MaterialTheme.typography.bodySmall,
        )
    }*/
}