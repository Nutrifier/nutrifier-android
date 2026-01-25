package fi.nutrifier.ui.components.layout.nutrient.progress.indicator.modes

import androidx.compose.foundation.clickable
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fi.nutrifier.utils.GraphicsUtils.valueToWheelDrawn

@Composable
fun LineMode(value: Double, max: Double, onClickInner: () -> Unit) {
    LinearProgressIndicator(
        progress = { valueToWheelDrawn(value, max) },
        modifier = Modifier.clickable { onClickInner() },
    )
}