package fi.nutrifier.repositories.room

import android.content.SharedPreferences
import fi.nutrifier.utils.SharedPreferencesManager
import fi.nutrifier.models.room.ShoppingListItem
import fi.nutrifier.repositories.shared.RepositoryResponseHandler

class ShoppingListRepository(private val prefs: SharedPreferences) {
    fun getShoppingList(): RepositoryResponseHandler<List<ShoppingListItem>> {
        val newShoppingList = SharedPreferencesManager.getShoppingList(prefs)
        return RepositoryResponseHandler(success = newShoppingList ?: listOf())
    }

    fun setShoppingList(newShoppingList: List<ShoppingListItem>) {
        SharedPreferencesManager.saveShoppingList(prefs, newShoppingList)
    }
}