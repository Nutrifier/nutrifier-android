package fi.nutrifier.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateListOf
import fi.nutrifier.models.room.ShoppingListItem
import fi.nutrifier.repositories.room.ShoppingListRepository
import fi.nutrifier.utils.SharedPreferencesKeys.PREFS_NAME

class ShoppingListViewModel(application: Application): BaseViewModel(application) {
    private val prefs: SharedPreferences
    private val repository: ShoppingListRepository
    private val _items = mutableStateListOf<ShoppingListItem>()

    init {
        val context = application.applicationContext
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        repository = ShoppingListRepository(prefs)
        loadData()
    }
    val items: List<ShoppingListItem> get() = _items

    private fun loadData() {
        val handler = repository.getShoppingList()
        if (handler.success != null) {
            _items.clear()
            _items.addAll(handler.success)
        }
    }

    fun add(i: ShoppingListItem) {
        val newShoppingList = _items.toMutableList()
        newShoppingList.add(i)
        repository.setShoppingList(newShoppingList)
        loadData()
    }

    fun delete(ingredientIndex: Int) {
        val newShoppingList = _items.toMutableList()
        newShoppingList.removeAt(ingredientIndex)
        repository.setShoppingList(newShoppingList)
        loadData()
    }


}