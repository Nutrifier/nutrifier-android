package fi.nutrifier.models.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson

/**
 * Database class representing the Room database for the Recipe App.
 * It includes two entity tables: PersonalRecipe and FavouriteRecipe.
 */
@Database(entities = [PersonalRecipe::class, FavouriteRecipe::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun personalRecipeDao(): PersonalRecipeDao
    abstract fun favouriteRecipeDao(): FavouriteRecipeDao
}

/**
 * Singleton object responsible for providing an instance of the AppDatabase.
 */
object DatabaseProvider {
    @Volatile private var INSTANCE: AppDatabase? = null
    fun getInstance(context: Context): AppDatabase {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                context = context,
                klass = AppDatabase::class.java,
                name = "app_db"
            ).fallbackToDestructiveMigration(false).build()

        }
        return INSTANCE as AppDatabase
    }
}

/**
 * Type converter class for converting custom types to and from Room database.
 * Room database requires this with more complex data structure as lists.
 */
class Converters {
    @TypeConverter
    fun fromIngredientList(value: List<RecipeIngredient>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toIngredientList(value: String?): List<RecipeIngredient> {
        return Gson().fromJson(value, Array<RecipeIngredient>::class.java).toList()

    }

    @TypeConverter
    fun fromInstructionList(value: List<RecipeInstruction>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toInstructionList(value: String?): List<RecipeInstruction> {
        return Gson().fromJson(value, Array<RecipeInstruction>::class.java).toList()
    }
}
