package fi.nutrifier.ui.components.dialogs

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fi.nutrifier.utils.AlertType
import fi.nutrifier.utils.Enums
import fi.nutrifier.viewmodels.ViewModelWrapper
import kotlinx.coroutines.launch

@Composable
fun DevDialog(
    screen: Enums.Screen,
    viewModels: ViewModelWrapper,
    navController: NavController,
    isVisible: Boolean,
    exitDialog: () -> Unit,
) {
    NutrifierDialog(
        isVisible = isVisible,
        onDismiss = { exitDialog() },
        title = "Dev Menu"
    ) {
        Text(text = "Only for the eyes of the developers!")
        Spacer(modifier = Modifier.height(16.dp))
        DevDialogOptions(selected = screen, viewModels, navController)
    }
}

@Composable
private fun DevDialogOptions(
    selected: Enums.Screen,
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
        TextButton(onClick = {
            viewModels.user.clear()
            viewModels.goals.clear()
            viewModels.profile.clear()
            viewModels.settings.clear()
            viewModels.weight.clear()
            viewModels.authViewModel.logout {
                println("Mocked logout!")
                navController.navigate("login")
            }
        }) {
            Text(text = "Logout")
        }
    }

    when (selected) {
        Enums.Screen.SHOPPING_LIST -> {
            baseActions()
            TextButton(onClick = { notYetImplemented() }) {
                Text(text = "Shopping list")
            }
        }
        Enums.Screen.COOKBOOK -> {
            baseActions()
            TextButton(onClick = { notYetImplemented() }) {
                Text(text = "Cookbook")
            }
        }
        Enums.Screen.DASHBOARD -> {
            baseActions()
            TextButton(onClick = { notYetImplemented() }) {
                Text(text = "Dashboard")
            }
        }
        Enums.Screen.FOOD_EDIT -> {
            baseActions()
            TextButton(onClick = { notYetImplemented() }) {
                Text(text = "Food edit")
            }
        }
        Enums.Screen.FOOD_ADD -> {
            baseActions()
            TextButton(onClick = { notYetImplemented() }) {
                Text(text = "Food add")
            }
        }
        Enums.Screen.LOGIN -> {
            TextButton(onClick = {
                navController.navigate("dashboard")
            }) {
                Text(text = "Login")
            }
        }
        Enums.Screen.FOOD_ENTRIES -> {
            baseActions()
            TextButton(onClick = { notYetImplemented() }) {
                Text(text = "Logs")
            }
        }
        Enums.Screen.MEAL -> {
            baseActions()
            TextButton(onClick = { notYetImplemented() }) {
                Text(text = "Meal")
            }
        }
        Enums.Screen.RECIPE_EDIT -> {
            baseActions()
            TextButton(onClick = { notYetImplemented() }) {
                Text(text = "Recipe edit")
            }
        }
        Enums.Screen.RECIPE_VIEW -> {
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