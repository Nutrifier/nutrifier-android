package fi.nutrifier.ui.components.misc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

enum class LabelValueOrientation {
    ROW,
    COLUMN,
}

@Composable
fun LabeledComponent(
    label: String? = null,
    orientation: LabelValueOrientation = LabelValueOrientation.COLUMN,
    component: @Composable () -> Unit,
) {
    if (label != null) {
        if (orientation == LabelValueOrientation.ROW) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                component()
            }
        } else {
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface,
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
    LabeledComponent("Label:", LabelValueOrientation.COLUMN) {
        Text("Component")
    }
}
