package fi.nutrifier.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import fi.nutrifier.models.database.User
import fi.nutrifier.repositories.database.UserRepository
import kotlinx.coroutines.launch

class UserSessionViewModel(
    private val repository: UserRepository,
    encryptedSharedPreferences: SharedPreferences
): BaseViewModel(encryptedSharedPreferences) {

    private var _user: MutableState<User?> = mutableStateOf(null)
    val user get() = _user.value

    init {
        setLoading(true)
    }

    fun getUser(token: String? = null) {
        setLoading(true)

        viewModelScope.launch {
            try {
                // Token can be null if the token is saved in SharedPrefs
                val response = repository.getUser(token)
                if (response.isSuccessful() && response.value != null) {
                    _user.value = response.value
                    Log.d("UserSessionViewModel", "GET: success")
                } else {
                    Log.d("UserSessionViewModel", "Error occurred in getting the user: ${response.message} (${response.errorCode}).")
                    showAlert("Error occurred in getting the user (${response.errorCode}).")
                }

                setLoading(false)
            } catch (e: Exception) {
                showAlert("Error: ${e.localizedMessage}")
            }
        }
    }

    fun clear() {
        _user.value = null
    }
}
