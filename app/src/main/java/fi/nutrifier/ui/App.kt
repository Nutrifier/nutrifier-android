package fi.nutrifier.ui

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fi.nutrifier.ui.screens.BarcodeScreen
import fi.nutrifier.utils.LocalApplicationContext
import fi.nutrifier.ui.screens.recipe.RecipeEditorScreen
import fi.nutrifier.ui.screens.DiscoverScreen
import fi.nutrifier.ui.screens.cookbook.CookbookScreen
import fi.nutrifier.ui.screens.food.FoodEditorScreen
import fi.nutrifier.ui.screens.food.AddFoodScreen
import fi.nutrifier.ui.screens.LoginScreen
import fi.nutrifier.ui.screens.LogsScreen
import fi.nutrifier.ui.screens.MealScreen
import fi.nutrifier.ui.screens.recipe.RecipeScreen
import fi.nutrifier.ui.screens.ShoppingListScreen
import fi.nutrifier.viewmodels.AuthViewModel
import fi.nutrifier.viewmodels.BarcodeScannerViewModel
import fi.nutrifier.viewmodels.FavouriteRecipesViewModel
import fi.nutrifier.viewmodels.LogsScreenViewModel
import fi.nutrifier.viewmodels.PersonalRecipesViewModel
import fi.nutrifier.viewmodels.RecipeUnderInspectionViewModel
import fi.nutrifier.viewmodels.SearchViewModel
import fi.nutrifier.viewmodels.ShoppingListViewModel
import fi.nutrifier.viewmodels.TodaysSpecialsViewModel
import fi.nutrifier.viewmodels.ViewModelWrapper

/**
 * Composable function representing the entry point of the Recipe App.
 *
 * This function sets up navigation using Jetpack Compose's NavHost and defines
 * different screens of the app such as DiscoverScreen, CookbookScreen, RecipeScreen,
 * and RecipeEditorScreen.
 *
 * @param applicationContext The application context required for providing dependencies.
 */
@Composable
fun App(applicationContext: Context) {
    val snackbarHostState = remember { SnackbarHostState() }
    val navController = rememberNavController()

    val inspectionViewModel: RecipeUnderInspectionViewModel = viewModel()
    val personalRecipesViewModel: PersonalRecipesViewModel = viewModel()
    val favouriteRecipesViewModel: FavouriteRecipesViewModel = viewModel()
    val searchViewModel: SearchViewModel = viewModel()
    val todaysSpecialsViewModel: TodaysSpecialsViewModel = viewModel()
    val shoppingListViewModel: ShoppingListViewModel = viewModel()
    val logsScreenViewModel: LogsScreenViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()
    val barcodeScannerViewModel: BarcodeScannerViewModel = viewModel()

    val viewModels = ViewModelWrapper(
        favourite = favouriteRecipesViewModel,
        personal = personalRecipesViewModel,
        inspection = inspectionViewModel,
        search = searchViewModel,
        specials = todaysSpecialsViewModel,
        shopping = shoppingListViewModel,
        logsScreen = logsScreenViewModel,
        authViewModel = authViewModel,
        barcode = barcodeScannerViewModel,
    )

    CompositionLocalProvider(LocalApplicationContext provides applicationContext) {
        NavHost(navController = navController, startDestination = "login") {
            composable("login") {
                LoginScreen(navController, viewModels, snackbarHostState)
            }
            composable("discover") {
                DiscoverScreen(navController, viewModels, snackbarHostState)
            }
            composable("logs") {
                LogsScreen(navController, viewModels, snackbarHostState)
            }
            composable("add_food") {
                AddFoodScreen(navController, viewModels, snackbarHostState)
            }
            composable("food_editor/{mode}") {
                val mode = it.arguments?.getString("mode")
                FoodEditorScreen(navController, viewModels, snackbarHostState, mode ?: "VIEW")
            }
            composable("cookbook") {
                CookbookScreen(navController, viewModels, snackbarHostState)
            }
            composable("recipe") {
                RecipeScreen(navController, viewModels, snackbarHostState)
            }
            composable("recipe_editor") {
                RecipeEditorScreen(navController, viewModels, snackbarHostState)
            }
            composable("shopping_list") {
                ShoppingListScreen(navController, viewModels, snackbarHostState)
            }
            composable("meal") {
                MealScreen(navController, viewModels, snackbarHostState)
            }
            composable("barcode") {
                BarcodeScreen(navController, viewModels, snackbarHostState)
            }
        }
    }
}
