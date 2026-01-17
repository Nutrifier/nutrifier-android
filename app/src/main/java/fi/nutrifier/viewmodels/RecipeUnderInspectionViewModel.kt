package fi.nutrifier.viewmodels

import android.app.Application
import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import fi.nutrifier.models.database.Ingredient
import fi.nutrifier.models.database.Instruction
import fi.nutrifier.models.database.Recipe
import fi.nutrifier.repositories.room.PersonalRecipeRepository
import fi.nutrifier.repositories.room.RecipeUnderInspectionRepository
import fi.nutrifier.utils.ConversionUtils.emptyRecipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * ViewModel class responsible for managing the recipe under inspection.
 *
 * This class provides methods for setting and fetching recipe data, as well as adding and deleting ingredients
 * and instructions associated with the recipe.
 */
class RecipeUnderInspectionViewModel(
    private val repository: RecipeUnderInspectionRepository,
    encryptedSharedPreferences: SharedPreferences
): BaseViewModel(encryptedSharedPreferences) {

    private var _recipe: MutableState<Recipe> = mutableStateOf(emptyRecipe)

    val recipe: MutableState<Recipe> get() = _recipe

    /**
     * Sets the recipe being inspected to the provided [newRecipe].
     *
     * @param newRecipe The new recipe to be set as the recipe under inspection.
     */
    fun setRecipe(newRecipe: Recipe?) {
        viewModelScope.launch {
            _recipe.value = newRecipe ?: emptyRecipe
            delay(125) // Adding small delay to get rid of loading bar flashing
            setLoading(false)
        }
    }

    fun fetchRecipe(id: Int) {
        setLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            val newRecipe = repository.fetchRecipe(id)?.toRecipe()
            setRecipe(newRecipe)
        }
    }

    fun addIngredient(ingredient: Ingredient) {
        val newIngredients = recipe.value.ingredients.toMutableList()
        newIngredients.add(ingredient)

        val newRecipe = recipe.value.copy(ingredients = newIngredients)
        setRecipe(newRecipe)
    }

    fun deleteIngredient(index: Int) {
        val newIngredients = recipe.value.ingredients.toMutableList()
        newIngredients.removeAt(index)

        val newRecipe = recipe.value.copy(ingredients = newIngredients)
        setRecipe(newRecipe)
    }

    fun addInstruction(instruction: Instruction) {
        val newInstructions = recipe.value.instructions.toMutableList()
        newInstructions.add(instruction)

        val newRecipe = recipe.value.copy(instructions = newInstructions)
        setRecipe(newRecipe)
    }

    fun deleteInstruction(index: Int) {
        val newInstructions = recipe.value.instructions.toMutableList()
        newInstructions.removeAt(index)

        val newRecipe = recipe.value.copy(instructions = newInstructions)
        setRecipe(newRecipe)
    }
}
