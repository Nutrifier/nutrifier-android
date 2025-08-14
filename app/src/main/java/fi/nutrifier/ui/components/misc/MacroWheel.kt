package fi.nutrifier.ui.components.misc

import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import fi.nutrifier.utils.GraphicsUtils.valueToWheelDrawn

/**
 * A composable function that circular progression bar.
 */
@Composable
fun MacroWheel(value: Number, max: Number, title: String, color: Color, size: String = "large", modifier: Modifier? = null) {
    val drawnArea: Float by animateFloatAsState(
        targetValue = valueToWheelDrawn(value, max),
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "MacroWheel coloring animation"
    )

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val elementSize = minOf(screenWidth, screenHeight) * 0.4f
    val backgroundColor = MaterialTheme.colorScheme.surface

    val circleSize = if (size == "large") elementSize else elementSize / 2
    val innerCircleSize = if (size == "large") circleSize - 16.dp else circleSize - 12.dp

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier ?: Modifier
    ) {
        Box(contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.size(circleSize)) {
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
                        if(size === "large") MaterialTheme.typography.headlineLarge
                        else MaterialTheme.typography.headlineMedium)
                Text(text = "/${max}")
            }
        }
        if (size === "small") Text(text = title)

    }
}