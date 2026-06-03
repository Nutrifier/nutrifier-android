package fi.nutrifier.ui.components.layout.nutrient.progress.indicator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fi.nutrifier.utils.Enums
import fi.nutrifier.utils.GraphicsUtils.valueToLineDrawn
import fi.nutrifier.viewmodels.SettingsViewModel

@Composable
fun NutrientProgressIndicator(
    settingsViewModel: SettingsViewModel,
    value: Number,
    max: Number,
    title: String,
    color: Color,
    modifier: Modifier = Modifier,
    size: Enums.IndicatorSize = Enums.IndicatorSize.LARGE,
    valueColor: Color = MaterialTheme.colorScheme.onBackground,
    maxColor: Color = MaterialTheme.colorScheme.onBackground,
    suffix: String? = null,
    showMax: Boolean = true,
    showLeftValue: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    fun onClickInner() {
        if (onClick != null) onClick()
    }

    BoxWithConstraints(modifier = modifier) {
        val baseSize = minOf(maxWidth, maxHeight) *
                if (size == Enums.IndicatorSize.LARGE) 0.55f else 0.44f

        when (settingsViewModel.settings?.nutrientDisplayMode) {
            /* TODO: Implement line mode
            Enums.NutrientDisplayMode.LINE -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    if (size == Enums.IndicatorSize.LARGE) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = value.toInt().toString(),
                                color = if (value.toInt() < 0) MaterialTheme.colorScheme.error else valueColor,
                                style =
                                    if(size === Enums.IndicatorSize.LARGE) MaterialTheme.typography.headlineMedium
                                    else MaterialTheme.typography.headlineSmall
                            )
                            HorizontalDivider(modifier = Modifier.width(baseSize * 0.3f))
                            Text(
                                text = if (suffix != null) "${max.toInt()} $suffix" else max.toInt().toString(),
                                color = if (max.toInt() < 0) MaterialTheme.colorScheme.error else maxColor,
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = value.toInt().toString(),
                                color = if (value.toInt() < 0) MaterialTheme.colorScheme.error else valueColor,
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Text(
                                text = if (suffix != null) " / ${max.toInt()} $suffix" else " / ${max.toInt()}",
                                color = if (max.toInt() < 0) MaterialTheme.colorScheme.error else maxColor,
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
                                .width(baseSize * 0.8f)
                                .clickable { onClickInner() },
                        )
                        Text(
                            text = title,
                            style = if (size == Enums.IndicatorSize.LARGE) {
                                MaterialTheme.typography.bodyMedium
                            } else MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }*/
            else -> {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.clip(CircleShape),
                    ) {
                        CircularProgressIndicator(
                            progress = { valueToLineDrawn(value, max) },
                            color = color,
                            gapSize = 0.dp,
                            strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
                            strokeWidth = if (size == Enums.IndicatorSize.LARGE) 8.dp else 6.dp,
                            modifier = Modifier
                                .rotate(180f)
                                .size(baseSize)
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            if (size === Enums.IndicatorSize.LARGE) Text(text = title)
                            Text(
                                text = if (showLeftValue) {
                                    (max.toDouble() - value.toDouble()).toInt().toString()
                                } else value.toInt().toString(),
                                color = if (value.toInt() < 0) MaterialTheme.colorScheme.error else valueColor,
                                style =
                                    if(size === Enums.IndicatorSize.LARGE) MaterialTheme.typography.headlineMedium
                                    else MaterialTheme.typography.headlineSmall
                            )
                            if (showLeftValue) {
                                Text(
                                    text = "Left",
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            }
                            if (showMax) {
                                Spacer(modifier = Modifier.height(baseSize * 0.05f))
                                HorizontalDivider(
                                    modifier = Modifier.width(baseSize * 0.25f),
                                    thickness = 1.dp,
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = if (suffix != null) "${max.toInt()} $suffix" else "/${max.toInt()}",
                                    color = if (max.toInt() < 0) MaterialTheme.colorScheme.error else maxColor,
                                    style = MaterialTheme.typography.bodySmall,
                                )
                            }
                        }
                    }
                    if (size === Enums.IndicatorSize.SMALL) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = title)
                    }
                }
            }
        }
    }
}