package fi.nutrifier.ui.components.misc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class LabelValueOrientation {
    ROW,
    COLUMN,
}

@Composable
fun LabeledComponent(
    label: String? = null,
    orientation: LabelValueOrientation = LabelValueOrientation.COLUMN,
    gap: Dp = 8.dp,
    component: @Composable () -> Unit,
) {
    if (label != null) {
        if (orientation == LabelValueOrientation.ROW) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(gap),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline,
                )
                component()
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(gap)) {
                Text(
                    text = label,
                    modifier = Modifier.padding(0.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline,
                )
                component()
            }
        }
    } else {
        component()
    }
}

@Preview
@Composable
fun LabelValuePreviewRow() {
    LabeledComponent("Label:") {
        Text("Component")
    }
}

@Preview
@Composable
fun LabelValuePreviewColumn() {
    LabeledComponent("Label:", LabelValueOrientation.COLUMN, gap = 0.dp) {
        Text("Component")
    }
}
