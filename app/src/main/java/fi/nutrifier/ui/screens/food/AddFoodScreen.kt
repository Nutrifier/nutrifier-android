package fi.nutrifier.ui.screens.food

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fi.nutrifier.ui.components.buttons.BackButton
import fi.nutrifier.ui.components.buttons.BarcodeButton
import fi.nutrifier.ui.components.layout.TopBar
import fi.nutrifier.viewmodels.ViewModelWrapper
import fi.nutrifier.ui.components.inputs.CustomSearchBar
import fi.nutrifier.ui.components.buttons.FoodButton
import fi.nutrifier.ui.components.inputs.CancelSaveOption
import fi.nutrifier.ui.components.misc.ItemDivider
import fi.nutrifier.ui.screens.Screen
import fi.nutrifier.utils.Constants
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddFoodScreen(
    navController: NavController,
    viewModels: ViewModelWrapper,
    snackbarHostState: SnackbarHostState,
    barcodeQuery: String? = ""
) {
    var showResult by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModels.foods.loadFoods()
    }

    LaunchedEffect(viewModels.logsScreen.alert) {
        viewModels.logsScreen.alert?.let {
            snackbarHostState.showSnackbar(it.message)
            viewModels.logsScreen.clearAlert()
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex
        }
            .collectLatest {
                val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                if (lastVisibleItemIndex != null
                    && lastVisibleItemIndex >= viewModels.foods.foods.size - 5
                    && !viewModels.logsScreen.loading
                ) {
                    viewModels.foods.loadMoreFoods()
                }
            }
    }

    Screen(
        topBar = { TopBar(subtitle = { BackButton(navController) })},
        bottomBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                CancelSaveOption(onClose = { navController.navigateUp() }) {
                    /* TODO */
                }
            }
        },
        screen = Constants.Screen.FOOD_ADD,
        viewModels,
        navController,
        snackbarHostState,
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Add foods", style = MaterialTheme.typography.headlineLarge)
                Row {
                    TextButton(onClick = { navController.navigate("food_editor/ADD") }) {
                        Icon(imageVector = Icons.Filled.AddCircle, contentDescription = "Add")
                        Text(text = "Add food")
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            CustomSearchBar(
                placeholder = "Search for foods",
                showResult = showResult,
                handleShowResult = { showResult = it },
                autoSearch = true,
                onClear = { viewModels.foods.loadFoods() },
                search = { viewModels.foods.searchFoods(it) },
                barcodeQuery = barcodeQuery ?: "",
                suffix = {
                    BarcodeButton(color = MaterialTheme.colorScheme.outline) {
                        navController.navigate("barcode/ADD_FOODS")
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    state = listState,
                    flingBehavior = ScrollableDefaults.flingBehavior(),
                ) {
                    itemsIndexed(
                        viewModels.foods.foods,
                        key = { index, food -> food.id ?: 0 }
                    ) {index, food ->
                        FoodButton(food) {
                            viewModels.foods.setSelectedFood(food)
                            navController.navigate("food_editor/EDIT")
                        }
                        if (index < viewModels.foods.foods.size - 1) ItemDivider()
                    }
                }
                if (viewModels.logsScreen.loading) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)),
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}