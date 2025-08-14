package fi.nutrifier.ui.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fi.nutrifier.R
import fi.nutrifier.models.database.Recipe
import fi.nutrifier.ui.components.misc.RecipeImage
import fi.nutrifier.viewmodels.RecipeUnderInspectionViewModel

/**
 * Composable function that displays a button for today's special recipe.
 *
 * @param navController The NavController to handle navigation.
 * @param index The index of the recipe.
 * @param recipe The recipe to display.
 * @param viewModel The view model for inspecting recipes.
 */
@Composable
fun TodaysSpecialButton(
    navController: NavController,
    index: Int,
    recipe: Recipe,
    viewModel: RecipeUnderInspectionViewModel,
) {
    var loading by remember { mutableStateOf(true) }
    var imageError by remember { mutableStateOf(false) }

    fun handleRecipeClick() {
        viewModel.setRecipe(recipe)
        navController.navigate("recipe")
    }

    TextButton(
        shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(0.dp),
        onClick = { handleRecipeClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.scrim
        )
    ) {
        Box {
            if (!imageError) {
                RecipeImage(
                    model = recipe.image,
                    onLoadError = { imageError = true },
                    onLoadSuccess = { loading = false },
                )
            } else {
                RecipeImage(painter = painterResource(id = R.drawable.meal))
            }

            // Adding shadow with box composable for better text readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.7f),
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f),
                            ),
                            start = Offset(0f, 0f),
                            end = Offset(0f, Float.POSITIVE_INFINITY)
                        )
                    )
            )
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = when (index) {
                        0 -> "Breakfast"
                        1 -> "Lunch"
                        2 -> "Dinner"
                        else -> "Snack"
                    },
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = recipe.title ?: "Loading...",
                    style = TextStyle(
                        fontSize = 20.sp
                    )
                )
            }
            if (loading && !imageError) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
