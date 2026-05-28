package fi.nutrifier.ui.components.inputs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PreviousNextOption(
    showPrevious: Boolean = true,
    showNext: Boolean = true,
    allowPrevious: Boolean = true,
    allowNext: Boolean = false,
    previousButtonText: String = "Previous",
    nextButtonText: String = "Next",
    padding: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 24.dp),
    handleStepChange: (Int) -> Unit,
) {
    Row(modifier = Modifier.padding(padding)) {
        if (showPrevious) {
            Button(
                enabled = allowPrevious,
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp),
                onClick = { handleStepChange(-1) },
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.surface),
                colors = ButtonDefaults.buttonColors(
                    //containerColor = Color.Transparent,
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
            ) {
                Text(previousButtonText)
            }
            Spacer(modifier = Modifier.width(16.dp))
        }
        if (showNext) {
            Button(
                enabled = allowNext,
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp),
                onClick = { handleStepChange(1) }
            ) {
                Text(nextButtonText)
            }
        }
    }
}