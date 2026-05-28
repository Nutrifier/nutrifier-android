package fi.nutrifier.viewmodels

import fi.nutrifier.models.room.Recipe

/**
 * Interface defining the contract for view models managing recipes.
 *
 * This interface provides properties and methods for observing recipe data, loading state, and errors,
 * as well as for loading, adding, and deleting recipes.
 */
interface RecipesViewModel {
    val recipes: List<Recipe>
    fun loadData()
    fun add(r: Recipe)
    fun delete(r: Recipe)
}
