package fi.nutrifier.ui.components.layout.nutrient.progress.indicator

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import fi.nutrifier.utils.Constants
import fi.nutrifier.utils.GraphicsUtils.valueToLineDrawn
import fi.nutrifier.utils.GraphicsUtils.valueToWheelDrawn
import fi.nutrifier.viewmodels.UserViewModel


/**
 * A composable function that circular progression bar.
 */
@Composable
fun NutrientProgressIndicator(
    userViewModel: UserViewModel,
    value: Number,
    max: Number,
    title: String,
    color: Color,
    modifier: Modifier = Modifier,
    size: String = "large",
    suffix: String? = null,
    onClick: (() -> Unit)? = null,
) {
    val drawnArea: Float by animateFloatAsState(
        targetValue = valueToWheelDrawn(value, max),
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "MacroWheel coloring animation"
    )

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val elementSize = minOf(screenWidth, screenHeight) * 0.4f
    val backgroundColor = MaterialTheme.colorScheme.surface

    val circleSize = if (size == "large") elementSize else elementSize / 5 * 3
    val innerCircleSize = if (size == "large") circleSize - 16.dp else circleSize - 12.dp

    fun onClickInner() {
        if (onClick != null) onClick()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        when (userViewModel.settings?.nutrientDisplayMode) {
            Constants.NutrientDisplayMode.LINE -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    if (size == "large") {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = value.toInt().toString(),
                                style =
                                    if(size === "large") MaterialTheme.typography.headlineMedium
                                    else MaterialTheme.typography.headlineSmall
                            )
                            HorizontalDivider(modifier = Modifier.width(44.dp))
                            Text(
                                text = if (suffix != null) "$max $suffix" else max.toString(),
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = value.toInt().toString(),
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Text(
                                text = if (suffix != null) " / $max $suffix" else " / $max",
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        LinearProgressIndicator(
                            progress = { valueToLineDrawn(value, max) },
                            color = color,
                            drawStopIndicator = {},
                            gapSize = 2.dp,
                            modifier = Modifier
                                .width(if (size == "large") 124.dp else 80.dp)
                                .clickable { onClickInner() },
                        )
                        Text(
                            text = title,
                            style = if (size == "large") {
                                MaterialTheme.typography.bodyMedium
                            } else MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
            Constants.NutrientDisplayMode.LEGACY_CIRCLE -> {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.clip(CircleShape)) {
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
                    }
                }
                if (size === "small") {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = title)
                }
            }
            else -> {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.clip(CircleShape)) {
                    CircularProgressIndicator(
                        progress = { valueToLineDrawn(value, max) },
                        color = color,
                        gapSize = 0.dp,
                        strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
                        strokeWidth = if (size == "large") 8.dp else 6.dp,
                        modifier = Modifier.rotate(180f).size(circleSize)
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
                    }
                }
                if (size === "small") {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = title)
                }
            }
        }
    }
}