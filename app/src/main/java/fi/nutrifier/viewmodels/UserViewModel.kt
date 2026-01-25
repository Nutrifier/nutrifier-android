package fi.nutrifier.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import fi.nutrifier.models.database.User
import fi.nutrifier.models.database.UserSettings
import fi.nutrifier.repositories.database.UserRepository
import fi.nutrifier.utils.AlertType
import kotlinx.coroutines.launch

class UserViewModel(
    private val repository: UserRepository,
    encryptedSharedPreferences: SharedPreferences
): BaseViewModel(encryptedSharedPreferences) {

    private var _user: MutableState<User?> = mutableStateOf(null)
    val user get() = _user.value

    private var _settings: MutableState<UserSettings?> = mutableStateOf(null)
    val settings get() = _settings.value

    init {
        setLoading(true)
    }

    fun getUser(token: String? = null) {
        Log.d("UserResponse", "Getting user...")

        setLoading(true)
        viewModelScope.launch {
            try {
                // Token can be null if the token is saved in SharedPrefs
                val response = repository.getUser(token)
                if (response.isSuccessful() && response.value != null) {
                    Log.d("UserResponse", response.value.toString())

                    _user.value = response.value
                    _settings.value = response.value.settings

                    Log.d("UserViewModel", "Got user: ${response.value}")

                } else {
                    Log.d("UserViewModel", "Ongelma käyttäjän hakemisessa: ${response.message} (${response.errorCode}).")
                    showAlert("Error occurred in user (${response.errorCode}).")
                }
                setLoading(false)
            } catch (e: Exception) {
                showAlert("Error: ${e.localizedMessage}")
            }
        }
    }

    fun updateSettings(updatedSettings: UserSettings) {
        Log.d("UserViewModel", "updating settings...")

        if (user == null || user?.settings == null) {
            viewModelScope.launch {
                showAlert("Error: User settings is not found. Try to log in again!")
            }
            return
        }

        // Update current session's settings even though if the persistence fails
        _settings.value = updatedSettings

        viewModelScope.launch {

            setLoading(true)
            try {
                Log.d("UserViewModel", "Inside viewModelScope")
                val response = repository.updateSettings(updatedSettings)

                if (response.isSuccessful() && response.value != null) {
                    Log.d("UserResponse", response.value.toString())
                    //showAlert("Settings saved!", AlertType.INFO)
                } else {
                    Log.d("UserViewModel", "Ongelma asetusten päivittämisessä: ${response.message} (${response.errorCode}).")
                    showAlert("Couldn't save user settings (${response.errorCode}).")
                }
                setLoading(false)
            } catch (e: Exception) {
                showAlert("Error: ${e.localizedMessage}")
            }
        }
    }
}