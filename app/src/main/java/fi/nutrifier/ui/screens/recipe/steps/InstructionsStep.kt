package fi.nutrifier.ui.screens.recipe.steps

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import fi.nutrifier.models.room.RecipeInstruction
import fi.nutrifier.ui.components.inputs.NutrifierTextField
import fi.nutrifier.ui.components.layout.InstructionRow
import fi.nutrifier.ui.components.misc.LabeledComponent
import fi.nutrifier.viewmodels.RecipeUnderInspectionViewModel

/**
 * Composable function for rendering the instructions step of the recipe editor.
 *
 * @param viewModel The [RecipeUnderInspectionViewModel] containing the recipe being edited.
 * @param handleAllowNextChange Callback function to handle changes in allowing navigation to the next step.
 */
@Composable
internal fun InstructionsStep(
    viewModel: RecipeUnderInspectionViewModel,
    handleAllowNextChange: (Boolean) -> Unit
) {
    var text by remember { mutableStateOf("") }
    val instructions = remember { mutableStateListOf<RecipeInstruction>() }

    LaunchedEffect(Unit) {
        instructions.addAll(viewModel.recipe.value.instructions)
    }

    LaunchedEffect(instructions.size) {
        if (instructions.isNotEmpty()) {
            handleAllowNextChange(true)
        } else {
            handleAllowNextChange(false)
        }
    }

    fun addInstruction() {
        if (text.isNotEmpty()) {
            // Cleaning up line breaks
            text = text.replace("\n", "")

            // Generating the next steps number
            val nextNumber = if (instructions.isEmpty()) 1 else instructions.size + 1

            // Generating the new instruction
            val newInstruction = RecipeInstruction(nextNumber, text)

            // Adding to viewModel state
            viewModel.addInstruction(newInstruction)

            // Setting state in composable
            instructions.clear()
            instructions.addAll(viewModel.recipe.value.instructions)

            // Setting the text-field text state to null
            text = ""
        }
    }

    fun deleteInstruction(index: Int) {
        instructions.removeAt(index)

        // Mapping instructions to set new instruction numbers
        val newInstructions = instructions.mapIndexed { i, instruction ->
            instruction.copy(number = i + 1)
        }

        instructions.clear()
        instructions.addAll(newInstructions)

        // Overwriting the whole recipe so instruction numbers are also updated to view model
        val newRecipe = viewModel.recipe.value.copy(instructions = newInstructions)
        viewModel.setRecipe(newRecipe)
    }

    NutrifierTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("Text *") },
        modifier = Modifier.fillMaxWidth(),
        keyboardActions = KeyboardActions(onNext = { addInstruction() }),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
        singleLine = false,
        trailingIcon = {
            IconButton(
                onClick = ::addInstruction,
                modifier = Modifier.fillMaxHeight().padding(end = 4.dp),
            ) {
                Icon(Icons.Rounded.Add, "add")
            }
        }
    )
    Spacer(modifier = Modifier.height(32.dp))
    if (instructions.isNotEmpty()) {
        LabeledComponent(label = "Added instructions") {
            instructions.forEachIndexed { index, instruction ->
                InstructionRow(index, instruction, ::deleteInstruction)
            }
        }
    }
}