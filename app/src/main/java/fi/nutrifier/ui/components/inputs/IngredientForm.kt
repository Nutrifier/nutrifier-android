package fi.nutrifier.ui.components.inputs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fi.nutrifier.models.room.RecipeIngredient
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

    Row(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = name,
            label = { Text("Name") },
            onValueChange = { name = it },
            isError = nameError,
            supportingText = { if (nameError) Text("Enter proper name") },
            modifier = Modifier.weight(0.8f)
        )
        TextButton(
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp),
            shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier.fillMaxHeight(),
            onClick = { handleIngredientSave() }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Rounded.Save, "save")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Save")
            }
        }
    }

    Text(
        text = "Select amount:",
        color = if (amountError) {
            MaterialTheme.colorScheme.error
        } else {
            MaterialTheme.colorScheme.onBackground
        }
    )
    Spacer(modifier = Modifier.height(8.dp))
    NumberCounter(
        value = amount,
        max = 1000.0,
        onNumberChange = { amount = it },
        editable = true
    )
    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = "Select a unit:",
        color = if (unitError) {
            MaterialTheme.colorScheme.error
        } else {
            MaterialTheme.colorScheme.onBackground
        }
    )
    Spacer(modifier = Modifier.height(8.dp))

    // Using FlowRow to wrap row to separate lines on overflow
    FlowRow(
        horizontalArrangement = Arrangement.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Enums.IngredientUnit.entries.forEach {
            TextButton(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(32.dp).padding(2.dp),
                contentPadding = PaddingValues(0.dp),
                onClick = {
                    unit = it
                    unitError = false
                },
                colors = ButtonDefaults.buttonColors(
                    contentColor = if (unitError) {
                        MaterialTheme.colorScheme.onError
                    } else {
                        MaterialTheme.colorScheme.onBackground
                    },
                    containerColor = if (unitError) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                ),
                border = BorderStroke(
                    width = 2.dp,
                    color = if (it == unit) {
                        MaterialTheme.colorScheme.outline
                    } else {
                        Color.Transparent
                    }
                ),
            ) {
                Text(it.displayName)
            }
        }
    }
}
