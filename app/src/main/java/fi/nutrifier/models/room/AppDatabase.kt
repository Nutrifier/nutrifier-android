package fi.nutrifier.models.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import fi.nutrifier.models.database.Ingredient
import fi.nutrifier.models.database.Instruction
import com.google.gson.Gson

/**
 * Database class representing the Room database for the Recipe App.
 * It includes two entity tables: PersonalRecipe and FavouriteRecipe.
 */
@Database(entities = [PersonalRecipe::class, FavouriteRecipe::class], version = 2, exportSchema = false)
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
            ).build()

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
    fun fromIngredientList(value: List<Ingredient>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toIngredientList(value: String?): List<Ingredient> {
        return Gson().fromJson(value, Array<Ingredient>::class.java).toList()

    }

    @TypeConverter
    fun fromInstructionList(value: List<Instruction>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toInstructionList(value: String?): List<Instruction> {
        return Gson().fromJson(value, Array<Instruction>::class.java).toList()
    }
}
