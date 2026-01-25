package fi.nutrifier.viewmodels

import fi.nutrifier.utils.Alert
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.merge

/**
 * Data class representing a wrapper for various view models related to recipe management.
 *
 * This class encapsulates view models for managing favourite recipes, personal recipes,
 * recipes under inspection, search functionality, and today's specials.
 *
 * @property favourite The [FavouriteRecipesViewModel] responsible for managing favourite recipes.
 * @property personal The [PersonalRecipesViewModel] responsible for managing personal recipes.
 * @property inspection The [RecipeUnderInspectionViewModel] responsible for managing recipes under inspection.
 * @property search The [SearchViewModel] responsible for managing recipe search functionality.
 * @property specials The [TodaysSpecialsViewModel] responsible for managing today's specials.
 */
data class ViewModelWrapper (
    val favourite: FavouriteRecipesViewModel,
    val personal: PersonalRecipesViewModel,
    val inspection: RecipeUnderInspectionViewModel,
    val search: SearchViewModel,
    val specials: TodaysSpecialsViewModel,
    val shopping: ShoppingListViewModel,
    val foodEntry: FoodEntryViewModel,
    val authViewModel: AuthViewModel,
    val barcode: BarcodeScannerViewModel,
    val foods: FoodsViewModel,
    val user: UserViewModel,
) {
    val alerts: Flow<Alert> = merge(
        favourite.alert,
        personal.alert,
        inspection.alert,
        search.alert,
        specials.alert,
        shopping.alert,
        foodEntry.alert,
        authViewModel.alert,
        barcode.alert,
        foods.alert,
        user.alert,
    )
}
