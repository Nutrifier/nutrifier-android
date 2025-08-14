package fi.nutrifier.models.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * Data Access Object (DAO) interface for accessing and manipulating personal recipe entities in the Room database.
 */
@Dao
interface PersonalRecipeDao {
    /**
     * Retrieves all personal recipes from the 'personal_recipes' table.
     *
     * @return A list of all personal recipes stored in the database.
     */
    @Query("SELECT * FROM personal_recipes")
    suspend fun getAll(): List<PersonalRecipe>

    /**
     * Retrieves a personal recipe by its unique identifier.
     *
     * @param id The unique identifier of the recipe to retrieve.
     * @return The personal recipe with the specified ID, or null if not found.
     */
    @Query("SELECT * FROM personal_recipes WHERE id == :id")
    suspend fun getRecipeById(id: Int): PersonalRecipe?

    /**
     * Inserts one or more personal recipes into the database.
     *
     * @param recipes The personal recipes to insert.
     */
    @Insert
    suspend fun insertRecipe(vararg recipes: PersonalRecipe)

    /**
     * Updates one or more existing personal recipes in the database.
     *
     * @param recipes The personal recipes to update.
     */
    @Update
    suspend fun updateRecipe(vararg recipes: PersonalRecipe)

    /**
     * Deletes a personal recipe from the database.
     *
     * @param recipe The personal recipe to delete.
     */
    @Delete
    suspend fun deleteRecipe(recipe: PersonalRecipe)

    /**
     * Clears all data in the 'personal_recipes' table.
     */
    @Query("DELETE FROM personal_recipes")
    suspend fun clearData()

    /**
     * Resets the auto-increment counter for the 'personal_recipes' table.
     */
    @Query("DELETE FROM sqlite_sequence WHERE name = 'personal_recipes'")
    suspend fun resetAutoIncrement()
}
