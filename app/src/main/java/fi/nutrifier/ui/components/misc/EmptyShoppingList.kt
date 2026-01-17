package fi.nutrifier.ui.components.misc

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fi.nutrifier.viewmodels.ViewModelWrapper

@Composable
fun EmptyRecipeList(navController: NavController, viewModels: ViewModelWrapper) {
    Column {
        UserFeedbackMessage(message = "Seem like you have no recipes yet.")
        Row {
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    viewModels.inspection.setRecipe(null)
                    navController.navigate("recipe_editor")
                }
            ) {
                Text("Let's change that!")
            }
        }
    }
}