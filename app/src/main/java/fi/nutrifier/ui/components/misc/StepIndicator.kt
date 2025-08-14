package fi.nutrifier.ui.components.misc

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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

/**
 * Composable function that displays a step indicator.
 *
 * @param steps The range of steps to display.
 * @param currentStep The current step.
 * @param handleStepChange Optional callback function to handle step changes.
 */
@Composable
fun StepIndicator(
    steps: IntRange,
    currentStep: Int,
    handleStepChange: ((Int) -> Unit)? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (handleStepChange != null) TextButton(onClick = { handleStepChange(-1) }) {
            Text("Previous")
        }
        steps.forEach {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(CircleShape)
                    .height(24.dp)
                    .width(24.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = CircleShape
                    )
            ) {
                if (currentStep > it) CheckCircle()
                else {
                    Text(
                        text = "${it + 1}",
                        style = TextStyle(
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            fontWeight =
                                if (currentStep >= it) {
                                    FontWeight.Bold
                                } else {
                                    FontWeight.Normal
                                }
                            ,
                            color =
                                if (currentStep >= it) {
                                    MaterialTheme.colorScheme.scrim
                                } else {
                                    Color.Gray.copy(alpha = 0.7f)
                                }
                        )
                    )
                }
            }
            if (it < steps.last) Box(
                modifier = Modifier
                    .height(2.dp)
                    .weight(1f)
                    .background(
                        if (currentStep > it) {
                            MaterialTheme.colorScheme.tertiary
                        } else {
                            MaterialTheme.colorScheme.outline
                        }
                    )
            )
        }
    }
}
