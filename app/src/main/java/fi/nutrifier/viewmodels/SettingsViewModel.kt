package fi.nutrifier.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import fi.nutrifier.models.database.UserSettings
import fi.nutrifier.repositories.database.SettingsRepository
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repository: SettingsRepository,
    encryptedSharedPreferences: SharedPreferences
): BaseViewModel(encryptedSharedPreferences) {

    private var _settings: MutableState<UserSettings?> = mutableStateOf(null)
    val settings get() = _settings.value

    private var _apiVersion: MutableState<String?> = mutableStateOf(null)
    val apiVersion get() = _apiVersion.value

    fun getSettings() {
        setLoading(true)

        viewModelScope.launch {
            try {
                val response = repository.getSettings()
                if (response.isSuccessful() && response.value != null) {
                    _settings.value = response.value
                    Log.d("SettingsViewModel", "Got settings: ${response.value}")

                    val apiInfoResponse = repository.getApiInfo()
                    if (apiInfoResponse.isSuccessful() && apiInfoResponse.value != null) {
                        _apiVersion.value = apiInfoResponse.value.build.version
                    }

                } else {
                    Log.d("SettingsViewModel", "Error occurred in getting settings: ${response.message} (${response.errorCode}).")
                    showAlert("Error occurred in getting settings (${response.errorCode}).")
                }

                setLoading(false)
            } catch (e: Exception) {
                showAlert("Error: ${e.localizedMessage}")
            }
        }
    }

    fun updateSettings(updatedSettings: UserSettings) {
        // Update current session's settings even though if the persistence fails
        _settings.value = updatedSettings

        viewModelScope.launch {

            setLoading(true)
            try {
                val response = repository.updateSettings(updatedSettings)

                if (response.isSuccessful() && response.value != null) {
                    Log.d("SettingsViewModel", "${response.value}")
                } else {
                    Log.d("SettingsViewModel", "Error occurred in updating settings: ${response.message} (${response.errorCode}).")
                    showAlert("Couldn't save user settings (${response.errorCode}).")
                }

                setLoading(false)
            } catch (e: Exception) {
                showAlert("Error: ${e.localizedMessage}")
            }
        }
    }

    fun clear() {
        _settings.value = null
    }
}
