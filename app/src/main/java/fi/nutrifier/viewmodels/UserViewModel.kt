package fi.nutrifier.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import fi.nutrifier.models.database.User
import fi.nutrifier.repositories.database.UserRepository
import fi.nutrifier.utils.AlertType
import kotlinx.coroutines.launch

class UserViewModel(
    private val repository: UserRepository,
    encryptedSharedPreferences: SharedPreferences
): BaseViewModel(encryptedSharedPreferences) {

    private var _user: MutableState<User?> = mutableStateOf(null)
    val user get() = _user.value

    /*
    private var _settings: MutableState<UserSettings?> = mutableStateOf(null)
    val settings get() = _settings.value
    */

    init {
        setLoading(true)
    }

    fun getUser() {
        setLoading(true)
        viewModelScope.launch {
            try {
                val response = repository.getUser()
                if (response.isSuccessful() && response.value != null) {
                    Log.d("UserResponse", response.value.toString())

                    _user.value = response.value

                    /*
                    // Saving the user to SharedPrefs
                    SharedPreferencesManager.saveAuthToken(
                        encryptedSharedPreferences,
                        response.value.token,
                    )
                    */

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

    fun updateSettings(
        weightUnit: String? = null,
        energyUnit: String? = null,
        language: String? = null,
        timeBetweenMeals: Int? = null,
        diet: String? = null,
        weekStartsOn: Int? = null,
        proteinEfficiencyEnabled: Boolean? = null,
        mealReminderEnabled: Boolean? = null,
        weighInReminderEnabled: Boolean? = null,
        motivationMessagesEnabled: Boolean? = null,
    ) {
        if (user == null || user?.settings == null) {
            viewModelScope.launch {
                showAlert("Error: User settings is not found. Try to log in again!")
            }
            return
        }

        val updatedSettings = user!!.settings.copy(
            weightUnit = weightUnit ?: user!!.settings.weightUnit,
            energyUnit = energyUnit ?: user!!.settings.energyUnit,
            language = language ?: user!!.settings.language,
            timeBetweenMeals = timeBetweenMeals ?: user!!.settings.timeBetweenMeals,
            diet = diet ?: user!!.settings.diet,
            weekStartsOn = weekStartsOn ?: user!!.settings.weekStartsOn,
            proteinEfficiencyEnabled = proteinEfficiencyEnabled ?: user!!.settings.proteinEfficiencyEnabled,
            mealReminderEnabled = mealReminderEnabled ?: user!!.settings.mealReminderEnabled,
            weighInReminderEnabled = weighInReminderEnabled ?: user!!.settings.weighInReminderEnabled,
            motivationMessagesEnabled = motivationMessagesEnabled ?: user!!.settings.motivationMessagesEnabled,
        )

        viewModelScope.launch {
            setLoading(true)
            try {
                Log.d("UserViewModel", "Inside viewModelScope")
                val response = repository.updateSettings(updatedSettings)

                if (response.isSuccessful() && response.value != null) {
                    Log.d("UserResponse", response.value.toString())
                    showAlert("Settings saved!", AlertType.INFO)
                    //_settings.value = response.value
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
}