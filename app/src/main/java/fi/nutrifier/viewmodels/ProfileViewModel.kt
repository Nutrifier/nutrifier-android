package fi.nutrifier.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import fi.nutrifier.models.database.UserProfile
import fi.nutrifier.repositories.database.ProfileRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: ProfileRepository,
    encryptedSharedPreferences: SharedPreferences
): BaseViewModel(encryptedSharedPreferences) {

    private var _profile: MutableState<UserProfile?> = mutableStateOf(null)
    val profile get() = _profile.value

    fun getProfile() {
        setLoading(true)

        viewModelScope.launch {
            try {
                val response = repository.getProfile()
                if (response.isSuccessful() && response.value != null) {
                    _profile.value = response.value
                    Log.d("ProfileViewModel", "GET: success")
                } else {
                    Log.d("ProfileViewModel", "Error occurred in getting profile: ${response.message} (${response.errorCode}).")
                    showAlert("Error occurred in getting profile (${response.errorCode}).")
                }

                setLoading(false)
            } catch (e: Exception) {
                showAlert("Error: ${e.localizedMessage}")
            }
        }
    }

    fun updateProfile(updatedProfile: UserProfile) {
        // Update current session's settings even though if the persistence fails
        _profile.value = updatedProfile

        viewModelScope.launch {
            setLoading(true)

            try {
                Log.d("UserViewModel", "Inside updateGoals")
                val response = repository.updateProfile(updatedProfile)

                if (response.isSuccessful() && response.value != null) {
                    Log.d("ProfileViewModel", response.value.toString())
                } else {
                    Log.d("ProfileViewModel", "Error occurred in updating profile: ${response.message} (${response.errorCode}).")
                    showAlert("Couldn't save user profile (${response.errorCode}).")
                }

                setLoading(false)
            } catch (e: Exception) {
                showAlert("Error: ${e.localizedMessage}")
            }
        }
    }

    fun clear() {
        _profile.value = null
    }
}
