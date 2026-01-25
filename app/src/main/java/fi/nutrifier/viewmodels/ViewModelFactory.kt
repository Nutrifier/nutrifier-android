package fi.nutrifier.viewmodels

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import fi.nutrifier.models.room.AppDatabase
import fi.nutrifier.models.room.DatabaseProvider
import fi.nutrifier.repositories.database.AuthRepository
import fi.nutrifier.repositories.database.FoodEntryRepository
import fi.nutrifier.repositories.database.FoodRepository
import fi.nutrifier.repositories.database.SearchRepository
import fi.nutrifier.repositories.database.UserRepository
import fi.nutrifier.repositories.room.FavouriteRecipeRepository
import fi.nutrifier.repositories.room.PersonalRecipeRepository
import fi.nutrifier.repositories.room.RecipeUnderInspectionRepository
import fi.nutrifier.repositories.room.ShoppingListRepository
import fi.nutrifier.repositories.room.TodaysSpecialsRepository
import fi.nutrifier.utils.SharedPreferencesKeys

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    private val encryptedSharedPreferences: SharedPreferences by lazy {
        val masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        EncryptedSharedPreferences.create(
            SharedPreferencesKeys.SECRET_PREFS_NAME,
            masterKey,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private val database: AppDatabase by lazy {
        DatabaseProvider.getInstance(context)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(AuthRepository(encryptedSharedPreferences), encryptedSharedPreferences)
            }
            modelClass.isAssignableFrom(BarcodeScannerViewModel::class.java) -> {
                BarcodeScannerViewModel(encryptedSharedPreferences)
            }
            modelClass.isAssignableFrom(FavouriteRecipesViewModel::class.java) -> {
                FavouriteRecipesViewModel(FavouriteRecipeRepository(database), encryptedSharedPreferences)
            }
            modelClass.isAssignableFrom(FoodEntryViewModel::class.java) -> {
                FoodEntryViewModel(FoodEntryRepository(encryptedSharedPreferences), encryptedSharedPreferences)
            }
            modelClass.isAssignableFrom(FoodsViewModel::class.java) -> {
                FoodsViewModel(FoodRepository(encryptedSharedPreferences), encryptedSharedPreferences)
            }
            modelClass.isAssignableFrom(PersonalRecipesViewModel::class.java) -> {
                PersonalRecipesViewModel(PersonalRecipeRepository(database), encryptedSharedPreferences)
            }
            modelClass.isAssignableFrom(RecipeUnderInspectionViewModel::class.java) -> {
                RecipeUnderInspectionViewModel(RecipeUnderInspectionRepository(), encryptedSharedPreferences)
            }
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(SearchRepository(), encryptedSharedPreferences)
            }
            modelClass.isAssignableFrom(ShoppingListViewModel::class.java) -> {
                ShoppingListViewModel(ShoppingListRepository(encryptedSharedPreferences), encryptedSharedPreferences)
            }
            modelClass.isAssignableFrom(TodaysSpecialsViewModel::class.java) -> {
                TodaysSpecialsViewModel(TodaysSpecialsRepository(encryptedSharedPreferences), encryptedSharedPreferences)
            }
            modelClass.isAssignableFrom(UserViewModel::class.java) -> {
                UserViewModel(UserRepository(encryptedSharedPreferences), encryptedSharedPreferences)
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
    }
}