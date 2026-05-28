package fi.nutrifier.ui

import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fi.nutrifier.ui.components.misc.CustomSnackbar
import fi.nutrifier.ui.screens.BarcodeScreen
import fi.nutrifier.utils.LocalApplicationContext
import fi.nutrifier.ui.screens.recipe.RecipeEditorScreen
import fi.nutrifier.ui.screens.analytics.AnalyticsScreen
import fi.nutrifier.ui.screens.cookbook.CookbookScreen
import fi.nutrifier.ui.screens.food.FoodScreen
import fi.nutrifier.ui.screens.AddEntryScreen
import fi.nutrifier.ui.screens.LoginScreen
import fi.nutrifier.ui.screens.FoodEntryScreen
import fi.nutrifier.ui.screens.MealScreen
import fi.nutrifier.ui.screens.settings.SettingsScreen
import fi.nutrifier.ui.screens.recipe.RecipeScreen
import fi.nutrifier.ui.screens.ShoppingListScreen
import fi.nutrifier.ui.screens.register.RegisterScreen
import fi.nutrifier.utils.Alert
import fi.nutrifier.viewmodels.AnalyticsViewModel
import fi.nutrifier.viewmodels.AuthViewModel
import fi.nutrifier.viewmodels.BarcodeScannerViewModel
import fi.nutrifier.viewmodels.FavouriteRecipesViewModel
import fi.nutrifier.viewmodels.FoodsViewModel
import fi.nutrifier.viewmodels.FoodEntryViewModel
import fi.nutrifier.viewmodels.GoalsViewModel
import fi.nutrifier.viewmodels.PersonalRecipesViewModel
import fi.nutrifier.viewmodels.ProfileViewModel
import fi.nutrifier.viewmodels.RecipeUnderInspectionViewModel
import fi.nutrifier.viewmodels.SearchViewModel
import fi.nutrifier.viewmodels.SettingsViewModel
import fi.nutrifier.viewmodels.ShoppingListViewModel
import fi.nutrifier.viewmodels.TodaysSpecialsViewModel
import fi.nutrifier.viewmodels.UserSessionViewModel
import fi.nutrifier.viewmodels.ViewModelFactory
import fi.nutrifier.viewmodels.ViewModelWrapper
import fi.nutrifier.viewmodels.WeightViewModel

/**
 * Composable function representing the entry point of the Recipe App.
 *
 * This function sets up navigation using Jetpack Compose's NavHost and defines
 * different screens of the app such as DashboardScreen, CookbookScreen, RecipeScreen,
 * and RecipeEditorScreen.
 *
 * @param applicationContext The application context required for providing dependencies.
 */
@Composable
fun App(applicationContext: Context) {
    val snackbarHostState = remember { SnackbarHostState() }
    val navController = rememberNavController()
    var currentAlert by remember { mutableStateOf<Alert?>(null) }

    val factory = ViewModelFactory(applicationContext)

    val inspectionViewModel: RecipeUnderInspectionViewModel = viewModel(factory = factory)
    val personalRecipesViewModel: PersonalRecipesViewModel = viewModel(factory = factory)
    val favouriteRecipesViewModel: FavouriteRecipesViewModel = viewModel(factory = factory)
    val searchViewModel: SearchViewModel = viewModel(factory = factory)
    val todaysSpecialsViewModel: TodaysSpecialsViewModel = viewModel(factory = factory)
    val shoppingListViewModel: ShoppingListViewModel = viewModel(factory = factory)
    val foodEntryViewModel: FoodEntryViewModel = viewModel(factory = factory)
    val authViewModel: AuthViewModel = viewModel(factory = factory)
    val barcodeScannerViewModel: BarcodeScannerViewModel = viewModel(factory = factory)
    val foodsViewModel: FoodsViewModel = viewModel(factory = factory)
    val userSessionViewModel: UserSessionViewModel = viewModel(factory = factory)
    val settingsViewModel: SettingsViewModel = viewModel(factory = factory)
    val goalsViewModel: GoalsViewModel = viewModel(factory = factory)
    val profileViewModel: ProfileViewModel = viewModel(factory = factory)
    val weightViewModel: WeightViewModel = viewModel(factory = factory)
    val analyticsViewModel: AnalyticsViewModel = viewModel(factory = factory)

    val viewModels = ViewModelWrapper(
        favourite = favouriteRecipesViewModel,
        personal = personalRecipesViewModel,
        inspection = inspectionViewModel,
        search = searchViewModel,
        specials = todaysSpecialsViewModel,
        shopping = shoppingListViewModel,
        foodEntry = foodEntryViewModel,
        authViewModel = authViewModel,
        barcode = barcodeScannerViewModel,
        foods = foodsViewModel,
        user = userSessionViewModel,
        settings = settingsViewModel,
        goals = goalsViewModel,
        profile = profileViewModel,
        weight = weightViewModel,
        analytics = analyticsViewModel
    )

    LaunchedEffect(Unit) {
        viewModels.alerts.collect {
            currentAlert = it
            snackbarHostState.showSnackbar(
                message = it.message,
                withDismissAction = true,
            )
        }
    }

    CompositionLocalProvider(LocalApplicationContext provides applicationContext) {
        Scaffold(
            snackbarHost = { CustomSnackbar(snackbarHostState, currentAlert) },
        ) {
            NavHost(
                navController = navController,
                startDestination = "login",
                modifier = Modifier.padding(it)
            ) {
                composable("login") {
                    LoginScreen(navController, viewModels)
                }
                composable("register") {
                    RegisterScreen(navController, viewModels)
                }
                composable("analytics") {
                    AnalyticsScreen(navController, viewModels)
                }
                composable("logs") {
                    FoodEntryScreen(navController, viewModels, null)
                }
                composable("logs/{date}") {
                    val date = it.arguments?.getString("date")
                    FoodEntryScreen(navController, viewModels, date)
                }
                composable("add_food") {
                    AddEntryScreen(navController, viewModels)
                }
                composable("add_food/{barcodeQuery}") {
                    val barcodeQuery = it.arguments?.getString("barcodeQuery")
                    AddEntryScreen(navController, viewModels, barcodeQuery)
                }
                composable("food_editor/{mode}") {
                    val mode = it.arguments?.getString("mode")
                    FoodScreen(navController, viewModels, mode ?: "VIEW")
                }
                composable("cookbook") {
                    CookbookScreen(navController, viewModels)
                }
                composable("recipe") {
                    RecipeScreen(navController, viewModels)
                }
                composable("recipe_editor") {
                    RecipeEditorScreen(navController, viewModels)
                }
                composable("shopping_list") {
                    ShoppingListScreen(navController, viewModels)
                }
                composable("meal") {
                    MealScreen(navController, viewModels)
                }
                composable("barcode/{case}") {
                    val case = it.arguments?.getString("case")
                    BarcodeScreen(navController, viewModels, case)
                }
                composable("settings") {
                    SettingsScreen(navController, viewModels)
                }
            }
        }
    }
}
