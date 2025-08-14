package fi.nutrifier.repositories.room

import android.util.Log
import fi.nutrifier.models.room.PersonalRecipe
import fi.nutrifier.models.room.AppDatabase
import fi.nutrifier.models.room.PersonalRecipeDao
import fi.nutrifier.repositories.shared.ManageableRecipeRepository
import fi.nutrifier.repositories.shared.RepositoryResponseHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository class for managing personal recipes in the database.
 *
 * @property db The Room database instance.
 */
class PersonalRecipeRepository(db: AppDatabase): ManageableRecipeRepository<PersonalRecipe> {
    private val personalRecipeDao: PersonalRecipeDao = db.personalRecipeDao()

    // Data fetching functions
    /**
     * Fetches all personal recipes from the database.
     *
     * @return A [RepositoryResponseHandler] containing a list of personal recipes if successful, or an error message if failed.
     */
    override suspend fun getAll(): RepositoryResponseHandler<List<PersonalRecipe>> {
        return try {
            val newRecipes = withContext(Dispatchers.IO) { personalRecipeDao.getAll() }
            RepositoryResponseHandler(success = newRecipes)
        } catch (e: Exception) {
            Log.e("PersonalRecipeRepository", "getAll(): $e")
            RepositoryResponseHandler(error = "Internal room error occurred! Contact the developer!")
        }

    }
    /**
     * Fetches a personal recipe from the database by its ID.
     *
     * @param id The ID of the personal recipe to fetch.
     * @return A [RepositoryResponseHandler] containing the fetched personal recipe if successful, or an error message if failed.
     */
    override suspend fun getById(id: Int): RepositoryResponseHandler<PersonalRecipe?> {
        return try {
            val newRecipe = withContext(Dispatchers.IO) { personalRecipeDao.getRecipeById(id) }
            RepositoryResponseHandler(success = newRecipe)
        } catch (e: Exception) {
            Log.e("PersonalRecipeRepository", "getById(): $e")
            RepositoryResponseHandler(error = "Internal room error occurred! Contact the developer!")
        }
    }

    // Data management functions
    /**
     * Adds a new personal recipe to the database.
     *
     * @param r The personal recipe to add.
     * @return A [RepositoryResponseHandler] indicating the success of the operation, or an error message if failed.
     */
    override suspend fun add(r: PersonalRecipe): RepositoryResponseHandler<PersonalRecipe?> {
        return try {
            personalRecipeDao.insertRecipe(r)
            RepositoryResponseHandler() // No need for return
        } catch (e: Exception) {
            Log.e("PersonalRecipeRepository", "add(): $e")
            RepositoryResponseHandler(error = "Internal room error occurred! Contact the developer!")
        }
    }
    /**
     * Deletes an existing personal recipe from the database.
     *
     * @param r The personal recipe to delete.
     * @return A [RepositoryResponseHandler] indicating the success of the operation, or an error message if failed.
     */
    override suspend fun delete(r: PersonalRecipe): RepositoryResponseHandler<PersonalRecipe?> {
        return try {
            personalRecipeDao.deleteRecipe(r)
            RepositoryResponseHandler() // No need for return
        } catch (e: Exception) {
            Log.e("PersonalRecipeRepository", "delete(): $e")
            RepositoryResponseHandler(error = "Internal room error occurred! Contact the developer!")
        }
    }
    /**
     * Updates an existing personal recipe in the database.
     *
     * @param r The personal recipe to update.
     * @return A [RepositoryResponseHandler] indicating the success of the operation, or an error message if failed.
     */
    suspend fun update(r: PersonalRecipe): RepositoryResponseHandler<PersonalRecipe?> {
        return try {
            personalRecipeDao.updateRecipe(r)
            RepositoryResponseHandler()
        } catch (e: Exception) {
            Log.e("PersonalRecipeRepository", "update(): $e")
            RepositoryResponseHandler(error = "Internal room error occurred! Contact the developer!")
        }
    }

    // Utility functions
    /**
     * Checks if a personal recipe with the specified ID exists in the database.
     *
     * @param id The ID of the personal recipe to check.
     * @return A [RepositoryResponseHandler] indicating whether the recipe exists in the database or not,
     * or an error message if failed.
     */
    suspend fun isRecipeInDatabase(id: Int?): RepositoryResponseHandler<Boolean> {
        return try {
            if (id != null) {
                val recipeFound = personalRecipeDao.getRecipeById(id) != null
                return RepositoryResponseHandler(success = recipeFound)
            } else {
                return RepositoryResponseHandler(error = "Api id not present!")
            }
        } catch (e: Exception) {
            Log.e("PersonalRecipeRepository", "isRecipeInDatabase(): $e")
            RepositoryResponseHandler(error = "Internal room error occurred! Contact the developer!")
        }
    }
}
