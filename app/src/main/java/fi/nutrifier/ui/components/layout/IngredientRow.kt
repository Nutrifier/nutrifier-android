package fi.nutrifier.ui.components.layout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import fi.nutrifier.models.room.RecipeIngredient
import fi.nutrifier.ui.components.buttons.RemoveButton
import fi.nutrifier.utils.ConversionUtils.convertToFraction
import fi.nutrifier.utils.FormattingUtils
import fi.nutrifier.viewmodels.ViewModelWrapper

/**
 * A composable function that displays a row representing an ingredient.
 * It shows the ingredient's amount, unit, and name, and optionally includes a delete button.
 *
 * @param index The index of the ingredient in the list.
 * @param ingredient The ingredient to display.
 * @param handleDelete An optional lambda function to handle the deletion of the ingredient.
 */
@Composable
fun IngredientRow(
    index: Int,
    ingredient: RecipeIngredient,
    viewModels: ViewModelWrapper? = null,
    handleDelete: ((Int) -> Unit)? = null,
) {
    val unitsToIgnoreFraction = listOf("g", "ml")
    val amountWithFraction: List<String> = if (!unitsToIgnoreFraction.any {
        ingredient.unit.displayName.lowercase() == it
    }) {
        convertToFraction(ingredient.amount.toDouble())
    } else listOf(FormattingUtils.roundUp(ingredient.amount.toDouble()).toString())
    var showSwipe by remember { mutableStateOf(true) }
    //val swipeState = rememberDismissState()
    var iconSize by remember { mutableFloatStateOf(1f) }

            /*
    if (viewModels != null) {
        LaunchedEffect(swipeState.currentValue) {
            Log.d("Ingredient", "${swipeState.currentValue}")
            if (swipeState.currentValue == DismissValue.DismissedToStart
                || swipeState.currentValue == DismissValue.DismissedToEnd) {
                iconSize = 16f
                swipeState.reset()
                viewModels.shopping.add(
                    ShoppingListItem(
                        name = ingredient.name,
                        note = "${ingredient.amount} ${ingredient.unit}"
                    )
                )
        }

        LaunchedEffect(swipeState.progress) {
            iconSize += swipeState.progress * 15
        }
    }
            }*/

    AnimatedVisibility(showSwipe, exit = fadeOut(spring())) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = amountWithFraction[0],
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = "${if (amountWithFraction.size > 1) "${amountWithFraction[1]} " else ""}${ingredient.unit.displayName}",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = ingredient.name,
                style = MaterialTheme.typography.titleMedium,
            )
            if (handleDelete != null) {
                RemoveButton(index, handleDelete)
            }
        }
    }
}
