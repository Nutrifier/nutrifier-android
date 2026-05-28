package fi.nutrifier.ui.components.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import fi.nutrifier.models.room.Recipe
import fi.nutrifier.ui.components.misc.RecipeImage
import fi.nutrifier.viewmodels.RecipeUnderInspectionViewModel

/**
 * A composable function that displays a recipe button.
 *
 * @param navController The [NavController] used for navigation.
 * @param recipe The recipe to be displayed.
 * @param viewModel The [RecipeUnderInspectionViewModel] associated with the recipe.
 */
@Composable
fun RecipeButton(
    navController: NavController,
    recipe: Recipe,
    viewModel: RecipeUnderInspectionViewModel
){
    var loading by remember { mutableStateOf(true) }
    var imageError by remember { mutableStateOf(false) }

    fun handleRecipeClick() {
        viewModel.setRecipe(recipe)
        navController.navigate("recipe")
    }

    TextButton(
        shape = RoundedCornerShape(8.dp),
        onClick = { handleRecipeClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.scrim
        )
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                .fillMaxSize()
                .height(96.dp)
                .border(2.dp, MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(15.dp))
        ) {
            if (!imageError) {
                RecipeImage(
                    model = recipe.image,
                    isPreview = true,
                    onLoadError = { imageError = true },
                    onLoadSuccess = {
                        imageError = false
                        loading = false
                    }
                )
            } else {
                RecipeImage(painter = painterResource(id = R.drawable.meal), isPreview = true)
            }
            // Adding shadow with box composable for better text readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f),
                            ),
                            start = Offset(0f, 0f),
                            end = Offset(0f, Float.POSITIVE_INFINITY)
                        )
                    )
            )
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxHeight().padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = recipe.title,
                    style = TextStyle(
                        fontSize = 20.sp
                    ),
                    color = Color(255, 255, 255) // Always white on images
                )
            }
            if (loading && !imageError) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
