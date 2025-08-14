package fi.nutrifier.ui.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import fi.nutrifier.viewmodels.ViewModelWrapper

/**
 * A composable function that displays a delete confirmation dialog.
 * When the delete action is confirmed, it deletes the recipe and navigates back to the cookbook screen.
 *
 * @param navController The NavController used to navigate between screens.
 * @param viewModels The ViewModelWrapper that contains the view models needed for the operation.
 * @param exitDialog A lambda function to handle the dismissal of the dialog.
 */
@Composable
fun DeleteDialog(
    navController: NavController,
    viewModels: ViewModelWrapper,
    exitDialog: () -> Unit
) {
    fun handleDelete() {
        exitDialog()
        viewModels.personal.delete(viewModels.inspection.recipe.value)
        navController.navigate("cookbook")
    }

    Dialog(onDismissRequest = { exitDialog() }) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(24.dp)
        ) {
            Text(
                text = "Are you sure you want to delete this recipe?",
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 24.sp)
                )
            Spacer(modifier = Modifier.height(8.dp))
            Text("This action cannot be undone!")
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = { exitDialog() }) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { handleDelete() }) {
                    Text("Delete")
                }
            }
        }
    }
}
