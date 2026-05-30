package fi.nutrifier.ui.components.inputs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import fi.nutrifier.models.room.RecipeIngredient
import fi.nutrifier.ui.components.misc.LabeledComponent
import fi.nutrifier.ui.components.misc.TextTag
import fi.nutrifier.utils.Enums
import fi.nutrifier.utils.ValidatorUtils

/**
 * A composable function that displays a form for adding an ingredient.
 * It includes fields for the ingredient name, amount, and unit, and validates the input before adding the ingredient.
 *
 * @param addIngredient A lambda function to handle adding the ingredient.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun IngredientForm(addIngredient: (RecipeIngredient) -> Unit) {
    var name: String by remember { mutableStateOf("") }
    var amount: Double by remember { mutableDoubleStateOf(1.0) }
    var unit: Enums.IngredientUnit by remember { mutableStateOf(Enums.IngredientUnit.G) }
    var nameError by remember { mutableStateOf(false) }
    var amountError by remember { mutableStateOf(false) }
    var unitError by remember { mutableStateOf(false) }

    fun handleTagClick(clickedUnit: Enums.IngredientUnit) {
        unit = clickedUnit
        unitError = false
    }

    fun handleIngredientSave() {
        nameError = !ValidatorUtils.ingredientName(name)
        amountError = !ValidatorUtils.ingredientAmount(amount)

        if (!nameError && !amountError && !unitError) {
            addIngredient(
                RecipeIngredient(
                    name = name,
                    unit = unit,
                    amount = amount.toFloat(),
                )
            )
        }
    }

    Column {
        NutrifierTextField(
            value = name,
            label = { Text("Name") },
            onValueChange = { name = it },
            isError = nameError,
            supportingText = { if (nameError) Text("Enter proper name") },
            keyboardActions = KeyboardActions(onNext = { handleIngredientSave() }),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(
                    modifier = Modifier.fillMaxHeight().padding(end = 4.dp),
                    onClick = { handleIngredientSave() }
                ) {
                    Icon(Icons.Rounded.Save, "save")
                }
            }
        )
        LabeledComponent(label = "Unit") {
            // Using FlowRow to wrap row to separate lines on overflow
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Enums.IngredientUnit.entries.forEach {
                    TextTag(
                        text = it.displayName,
                        onClick = { handleTagClick(it) },
                        isError = unitError,
                        isHighlighted = unit == it
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        LabeledComponent(label = "Amount") {
            NumberCounter(
                value = amount,
                min = 1.0,
                max = 1000.0,
                onNumberChange = { amount = it },
                editable = true
            )
        }
    }
}
