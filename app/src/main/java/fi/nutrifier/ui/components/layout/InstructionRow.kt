package fi.nutrifier.ui.components.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import fi.nutrifier.models.database.Instruction
import fi.nutrifier.ui.components.misc.CheckCircle
import fi.nutrifier.ui.components.buttons.RemoveButton

/**
 * A composable function that displays a row representing a cooking instruction.
 * It shows the instruction's number and text, and optionally includes a delete button.
 * The instruction can be marked as done by clicking on it.
 *
 * @param index The index of the instruction in the list.
 * @param instruction The instruction to display.
 * @param handleDelete An optional lambda function to handle the deletion of the instruction.
 */
@Composable
fun InstructionRow(
    index: Int,
    instruction: Instruction,
    handleDelete: ((Int) -> Unit)? = null,
) {
    var isDone by remember { mutableStateOf(false) }

    TextButton(
        enabled = handleDelete == null,
        shape = RoundedCornerShape(4.dp),
        onClick = { isDone = !isDone },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row {
                Column(modifier = Modifier.width(32.dp).height(32.dp)) {
                    if (isDone) CheckCircle()
                    else {
                        Text(
                            text = "${instruction.number}",
                            color = MaterialTheme.colorScheme.secondary,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = instruction.text,
                    modifier = Modifier.padding(top = 2.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    textDecoration = if (isDone) TextDecoration.LineThrough else null,
                    color = if (isDone) {
                        MaterialTheme.colorScheme.outline
                    } else {
                        MaterialTheme.colorScheme.onBackground
                    }
                )
            }
            if (handleDelete != null) {
                RemoveButton(index = index, onClick = { handleDelete(index) })
            }
        }
    }
}
