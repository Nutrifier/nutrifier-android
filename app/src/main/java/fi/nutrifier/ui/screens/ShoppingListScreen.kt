package fi.nutrifier.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fi.nutrifier.ui.components.navigation.NavBar
import fi.nutrifier.ui.components.inputs.ShoppingListItemForm
import fi.nutrifier.ui.components.layout.ShoppingListItemRow
import fi.nutrifier.ui.components.layout.TopBar
import fi.nutrifier.ui.components.misc.UserFeedbackMessage
import fi.nutrifier.utils.Constants
import fi.nutrifier.viewmodels.ViewModelWrapper

@Composable
fun ShoppingListScreen(
    navController: NavController,
    viewModels: ViewModelWrapper,
    snackbarHostState: SnackbarHostState,
) {
    Screen(
        topBar = { TopBar("Shopping List", subtitle = {
            ShoppingListItemForm(viewModels.shopping)
        }) },
        bottomBar = { NavBar(navController, "shopping_list") },
        screen = Constants.Screen.SHOPPING_LIST,
        viewModels,
        navController,
        snackbarHostState,
    ) {
        Box(contentAlignment = Alignment.BottomEnd) {
            Column(modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())) {
                Column(
                    modifier = Modifier.padding(horizontal = 8.dp)) {
                    Text("Items", style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    if (viewModels.shopping.items.isEmpty()) {
                        UserFeedbackMessage("Seems like you have an empty shopping list")
                    }
                    viewModels.shopping.items.forEachIndexed { index, item ->
                        ShoppingListItemRow(index, item, viewModels.shopping)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}