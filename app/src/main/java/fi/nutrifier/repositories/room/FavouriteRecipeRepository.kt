package fi.nutrifier.repositories.room

import android.util.Log
import fi.nutrifier.models.room.AppDatabase
import fi.nutrifier.models.room.FavouriteRecipe
import fi.nutrifier.models.room.FavouriteRecipeDao
import fi.nutrifier.repositories.shared.ManageableRecipeRepository
import fi.nutrifier.repositories.shared.RepositoryResponseHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository for managing favourite recipes.
 *
 * @property db The instance of the Room database.
 */
class FavouriteRecipeRepository(db: AppDatabase): ManageableRecipeRepository<FavouriteRecipe> {
    private var favouriteRecipeDao: FavouriteRecipeDao = db.favouriteRecipeDao()

    // Data fetching functions
    /**
     * Fetches all favourite recipes from the database.
     *
     * @return A [RepositoryResponseHandler] containing a list of favourite recipes if successful, or an error message if failed.
     */
    override suspend fun getAll(): RepositoryResponseHandler<List<FavouriteRecipe>> {
        return try {
            val newRecipes = withContext(Dispatchers.IO) { favouriteRecipeDao.getAll() }
            RepositoryResponseHandler(success = newRecipes)
        } catch (e: Exception) {
            Log.e("FavouriteRecipeRepository", "getAll(): $e")
            RepositoryResponseHandler(error = "Internal room error occurred! Contact the developer!")
        }
    }
    /**
     * Fetches a favourite recipe by its ID from the database.
     *
     * @param id The ID of the recipe to fetch.
     * @return A [RepositoryResponseHandler] containing the fetched favourite recipe if successful, or an error message if failed.
     */
    override suspend fun getById(id: Int): RepositoryResponseHandler<FavouriteRecipe?> {
        return try {
            val newRecipe = withContext(Dispatchers.IO) {
                favouriteRecipeDao.getRecipeById(id)
            }
            RepositoryResponseHandler(success = newRecipe)
        } catch (e: Exception) {
            Log.e("FavouriteRecipeRepository", "getById(): $e")
            RepositoryResponseHandler(error = "Internal room error occurred! Contact the developer!")
        }
    }

    // Data management functions
    /**
     * Adds a new favourite recipe to the database.
     *
     * @param r The favourite recipe to add.
     * @return A [RepositoryResponseHandler] indicating the success of the operation, or an error message if failed.
     */
    override suspend fun add(r: FavouriteRecipe): RepositoryResponseHandler<FavouriteRecipe?> {
        return try {
            favouriteRecipeDao.insertRecipe(r)
            RepositoryResponseHandler()
        } catch (e: Exception) {
            Log.e("FavouriteRecipeRepository", "add(): $e")
            RepositoryResponseHandler(error = "Internal room error occurred! Contact the developer!")
        }
    }
    /**
     * Deletes an existing favourite recipe from the database.
     *
     * @param r The favourite recipe to delete.
     * @return A [RepositoryResponseHandler] indicating the success of the operation, or an error message if failed.
     */
    override suspend fun delete(r: FavouriteRecipe): RepositoryResponseHandler<FavouriteRecipe?> {
        return try {
            favouriteRecipeDao.deleteRecipe(r)
            RepositoryResponseHandler()
        } catch (e: Exception) {
            Log.e("FavouriteRecipeRepository", "delete(): $e")
            RepositoryResponseHandler(error = "Internal room error occurred! Contact the developer!")
        }
    }
}