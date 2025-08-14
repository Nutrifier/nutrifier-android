package fi.nutrifier.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import fi.nutrifier.ui.screens.food.AddFoodScreen
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
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddFoodScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

    val viewModels = ViewModelWrapper(
        favourite = FavouriteRecipesViewModel(ApplicationProvider.getApplicationContext()),
        personal = PersonalRecipesViewModel(ApplicationProvider.getApplicationContext()),
        inspection = RecipeUnderInspectionViewModel(ApplicationProvider.getApplicationContext()),
        search = SearchViewModel(ApplicationProvider.getApplicationContext()),
        specials = TodaysSpecialsViewModel(ApplicationProvider.getApplicationContext()),
        shopping = ShoppingListViewModel(ApplicationProvider.getApplicationContext()),
        logsScreen = LogsScreenViewModel(ApplicationProvider.getApplicationContext()),
        authViewModel = AuthViewModel(ApplicationProvider.getApplicationContext()),
        barcode = BarcodeScannerViewModel(ApplicationProvider.getApplicationContext())
    )

    @Test
    fun testComposable_showsScreen() {
        composeTestRule.setContent { 
            AddFoodScreen(navController = navController, viewModels = viewModels, snackbarHostState = SnackbarHostState())
        }

        composeTestRule.onNodeWithText("Add food").assertIsDisplayed()
    }
    
}