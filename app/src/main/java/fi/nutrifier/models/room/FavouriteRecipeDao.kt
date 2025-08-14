package fi.nutrifier.models.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * Data Access Object (DAO) for interacting with the 'favourite_recipes' table in the Room database.
 */
@Dao
interface FavouriteRecipeDao {
    /**
     * Retrieves all favorite recipes from the database.
     */
    @Query("SELECT * FROM favourite_recipes")
    suspend fun getAll(): List<FavouriteRecipe>

    /**
     * Retrieves a favorite recipe by its unique identifier.
     *
     * @param id The unique identifier of the recipe to retrieve.
     * @return The favorite recipe with the specified ID, or null if not found.
     */
    @Query("SELECT * FROM favourite_recipes WHERE id == :id")
    suspend fun getRecipeById(id: Int): FavouriteRecipe?

    /**
     * Inserts one or more favorite recipes into the database.
     *
     * @param recipes The favorite recipes to insert.
     */
    @Insert
    suspend fun insertRecipe(vararg recipes: FavouriteRecipe)

    /**
     * Updates one or more favorite recipes in the database.
     *
     * @param recipes The favorite recipes to update.
     */
    @Update
    suspend fun updateRecipe(vararg recipes: FavouriteRecipe)

    /**
     * Deletes a favorite recipe from the database.
     *
     * @param recipe The favorite recipe to delete.
     */
    @Delete
    suspend fun deleteRecipe(recipe: FavouriteRecipe)

    /**
     * Clears all data from the 'favourite_recipes' table.
     */
    @Query("DELETE FROM favourite_recipes")
    suspend fun clearData()

    /**
     * Resets the auto-increment value of the primary key for the 'favourite_recipes' table.
     */
    @Query("DELETE FROM sqlite_sequence WHERE name = 'favourite_recipes'")
    suspend fun resetAutoIncrement()
}
