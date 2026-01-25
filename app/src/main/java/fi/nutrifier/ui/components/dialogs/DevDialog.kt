package fi.nutrifier.ui.components.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import fi.nutrifier.utils.AlertType
import fi.nutrifier.utils.Constants.Screen
import fi.nutrifier.viewmodels.ViewModelWrapper
import kotlinx.coroutines.launch

@Composable
fun DevDialog(
    screen: Screen,
    viewModels: ViewModelWrapper,
    navController: NavController,
    exitDialog: () -> Unit,
) {
    Dialog(onDismissRequest = { exitDialog() }) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(24.dp)
        ) {
            Text(text = "Dev Menu", style = MaterialTheme.typography.headlineMedium)
            Text(text = "Only for the eyes of the developers!")
            Spacer(modifier = Modifier.height(16.dp))
            DevDialogOptions(selected = screen, viewModels, navController)
        }
    }
}

@Composable
private fun DevDialogOptions(
    selected: Screen,
    viewModels: ViewModelWrapper,
    navController: NavController,
) {
    val coroutineScope = rememberCoroutineScope()

    fun notYetImplemented() {
        coroutineScope.launch {
            viewModels.authViewModel.showAlert("Not yet implemented!", AlertType.INFO)
        }
    }

    @Composable
    fun baseActions() {
        TextButton(onClick = { viewModels.authViewModel.logout {
            println("Mocked logout!")
            navController.navigate("login")
        }}) {
            Text(text = "Logout")
        }
    }

    when (selected) {
        Screen.SHOPPING_LIST -> {
            baseActions()
            TextButton(onClick = { notYetImplemented() }) {
                Text(text = "Shopping list")
            }
        }
        Screen.COOKBOOK -> {
            baseActions()
            TextButton(onClick = { notYetImplemented() }) {
                Text(text = "Cookbook")
            }
        }
        Screen.DASHBOARD -> {
            baseActions()
            TextButton(onClick = { notYetImplemented() }) {
                Text(text = "Dashboard")
            }
        }
        Screen.FOOD_EDIT -> {
            baseActions()
            TextButton(onClick = { notYetImplemented() }) {
                Text(text = "Food edit")
            }
        }
        Screen.FOOD_ADD -> {
            baseActions()
            TextButton(onClick = { notYetImplemented() }) {
                Text(text = "Food add")
            }
        }
        Screen.LOGIN -> {
            TextButton(onClick = {
                navController.navigate("dashboard")
            }) {
                Text(text = "Login")
            }
        }
        Screen.FOOD_ENTRIES -> {
            baseActions()
            TextButton(onClick = { notYetImplemented() }) {
                Text(text = "Logs")
            }
        }
        Screen.MEAL -> {
            baseActions()
            TextButton(onClick = { notYetImplemented() }) {
                Text(text = "Meal")
            }
        }
        Screen.RECIPE_EDIT -> {
            baseActions()
            TextButton(onClick = { notYetImplemented() }) {
                Text(text = "Recipe edit")
            }
        }
        Screen.RECIPE_VIEW -> {
            baseActions()
            TextButton(onClick = { notYetImplemented() }) {
                Text(text = "Recipe view")
            }
        }
        else -> TextButton(onClick = { notYetImplemented() }) {
            Text(text = "Implementation missing")
        }

    }
}