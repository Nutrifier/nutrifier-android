package fi.nutrifier.ui.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import fi.nutrifier.ui.components.buttons.FoodButton
import fi.nutrifier.ui.components.inputs.ActionButtons
import fi.nutrifier.ui.components.inputs.CustomSearchBar
import fi.nutrifier.ui.components.layout.TopBar
import fi.nutrifier.ui.components.misc.ItemDivider
import fi.nutrifier.utils.Enums
import fi.nutrifier.viewmodels.ViewModelWrapper
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddEntryScreen(
    navController: NavController,
    viewModels: ViewModelWrapper,
    barcodeQuery: String? = ""
) {
    var showResult by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val isLoading by viewModels.foodEntry.loading.collectAsState()

    LaunchedEffect(Unit) {
        viewModels.foods.loadFoods()
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex
        }
            .collectLatest {
                val lastVisibleItemIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                if (lastVisibleItemIndex != null
                    && lastVisibleItemIndex >= viewModels.foods.foods.size - 5
                    && isLoading
                ) {
                    viewModels.foods.loadMoreFoods()
                }
            }
    }

    BaseScreen(
        topBar = { TopBar(subtitle = { BackButton(navController) }) },
        bottomBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                ActionButtons(onSecondaryAction = { navController.navigateUp() })
            }
        },
        screen = Enums.Screen.FOOD_ADD,
        viewModels,
        navController,
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Add foods", style = MaterialTheme.typography.headlineLarge)
                Row {
                    TextButton(onClick = { navController.navigate("food_editor/${Enums.FoodMode.CREATE}/") }) {
                        Icon(imageVector = Icons.Filled.AddCircle, contentDescription = "Add")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Create food")
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
            Box(
                modifier = Modifier.weight(1f)
            ) {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    state = listState,
                    flingBehavior = ScrollableDefaults.flingBehavior(),
                ) {
                    itemsIndexed(
                        viewModels.foods.recentFoods,
                        key = { index, food -> (food.id + index) }
                    ) { index, food ->
                        FoodButton(viewModels.settings, food, true) {
                            viewModels.foods.setSelectedFood(food)
                            navController.navigate("food_editor/${Enums.FoodMode.CREATE_ENTRY}/")
                        }
                        if (index < viewModels.foods.foods.size - 1) ItemDivider()
                    }
                    itemsIndexed(
                        viewModels.foods.foods,
                        key = { index, food -> (food.id + index) }
                    ) { index, food ->
                        FoodButton(viewModels.settings, food) {
                            viewModels.foods.setSelectedFood(food)
                            navController.navigate("food_editor/${Enums.FoodMode.CREATE_ENTRY}/")
                        }
                        if (index < viewModels.foods.foods.size - 1) ItemDivider()
                        if (index == viewModels.foods.foods.size - 1) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                TextButton({ viewModels.foods.loadMoreFoods() }) {
                                    Text("Load more foods")
                                }
                            }
                        }
                    }
                }
                if (isLoading) {
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
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${viewModels.foods.firstItemIndex}-${viewModels.foods.lastItemIndex} of ${viewModels.foods.totalFoodsCount} foods",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
