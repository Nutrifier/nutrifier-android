package fi.nutrifier.ui.components.misc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fi.nutrifier.viewmodels.ViewModelWrapper

@Composable
fun EmptyRecipeList(
    navController: NavController,
    viewModels: ViewModelWrapper,
    isSavedRecipes: Boolean = false,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.MenuBook,
            contentDescription = "Menubook",
            modifier = Modifier.size(36.dp),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (isSavedRecipes) "You have no saved recipes" else "Empty recipe list",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
        )
        Text(
            text = if (isSavedRecipes) "Search for recipes and save them as favourite" else "Add new recipe to the recipe list",
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Center,
        )
        if (!isSavedRecipes) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    viewModels.inspection.setRecipe(null)
                    navController.navigate("recipe_editor")
                }
            ) {
                Text("Add new recipe")
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}