package fi.nutrifier.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fi.nutrifier.ui.components.buttons.ProfileButton
import fi.nutrifier.ui.components.navigation.NavBar
import fi.nutrifier.ui.components.inputs.ShoppingListItemForm
import fi.nutrifier.ui.components.layout.ShoppingListItemRow
import fi.nutrifier.ui.components.layout.TopBar
import fi.nutrifier.ui.components.misc.EmptyShoppingList
import fi.nutrifier.utils.Enums
import fi.nutrifier.viewmodels.ViewModelWrapper

@Composable
fun ShoppingListScreen(
    navController: NavController,
    viewModels: ViewModelWrapper,
) {
    BaseScreen(
        topBar = { TopBar(
            title = "Shopping List",
            actionButton = { ProfileButton(navController) }
        )},
        bottomBar = { NavBar(navController, "shopping_list") },
        screen = Enums.Screen.SHOPPING_LIST,
        viewModels,
        navController,
    ) {
        Column {
            if (viewModels.shopping.items.isEmpty()) {
                EmptyShoppingList {
                    ShoppingListItemForm(viewModels.shopping)
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        itemsIndexed(viewModels.shopping.items.reversed()) { index, item ->
                            ShoppingListItemRow(index, item, viewModels.shopping)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                    Column {
                        ShoppingListItemForm(viewModels.shopping)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}