package fi.nutrifier.viewmodels

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import androidx.security.crypto.EncryptedSharedPreferences
import fi.nutrifier.BuildConfig
import fi.nutrifier.models.database.AuthRequest
import fi.nutrifier.models.database.User
import fi.nutrifier.repositories.database.AuthRepository
import fi.nutrifier.utils.AlertType
import fi.nutrifier.utils.SharedPreferencesManager
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository,
    encryptedSharedPreferences: SharedPreferences,
): BaseViewModel(encryptedSharedPreferences) {

    init {
        setLoading(true)
    }

    // TODO: Add check that the auth token isn't expired
    suspend fun checkAuthToken(): Boolean {
        val token = SharedPreferencesManager.getAuthToken(encryptedSharedPreferences)
            ?: return false

        setLoading(true)

        return try {
            val response = repository.validate()
            if (response.isSuccessful()) {
                showAlert("Found user from shared prefs!", AlertType.INFO)
                true
            } else {
                showAlert("Invalid credentials! Log in again!", AlertType.ERROR)
                false
            }
        } catch (e: Exception) {
            showAlert("Error: ${e.localizedMessage}")
            false
        } finally {
            setLoading(false)
        }
    }

    fun register(authRequest: AuthRequest, callback: () -> Unit) {
        setLoading(true)
        viewModelScope.launch {
            try {
                // Calling the api for auth token
                val response = repository.register(authRequest)
                if (response.isSuccessful() && response.value != null) {
                    Log.d("RegisterResponse", response.value.toString())

                    showAlert("Registeration successful!", AlertType.INFO)

                    // Saving the token to SharedPrefs
                    SharedPreferencesManager.saveAuthToken(
                        encryptedSharedPreferences,
                        response.value.token,
                    )

                    /*
                    // Saving the user to SharedPrefs
                    val user = User(response.value.userId, response.value.userEmail)
                    SharedPreferencesManager.saveUser(encryptedSharedPreferences, user)
                    */
                    callback()
                } else {
                    Log.d("AuthViewModel", "Ongelma rekisteröitymisessä: ${response.message} (${response.errorCode}).")
                    showAlert("Error occurred in registering (${response.errorCode}).")
                }
                setLoading(false)
            } catch (e: Exception) {
                showAlert("Error: ${e.localizedMessage}")
            }
        }
    }

    fun login(authRequest: AuthRequest, callback: () -> Unit) {
        setLoading(true)
        viewModelScope.launch {
            try {
                // Calling the api for auth token
                val response = repository.login(authRequest)
                Log.d("AuthViewModel", "Login response: $response")

                if (response.isSuccessful() && response.value != null) {
                    Log.d("RegisterResponse", response.value.toString())

                    showAlert("Log in successful!", AlertType.INFO)

                    // Saving the token to SharedPrefs
                    SharedPreferencesManager.saveAuthToken(
                        encryptedSharedPreferences,
                        response.value.token,
                    )

                    callback()
                } else {
                    Log.d("AuthViewModel", "Ongelma kirjautumisessa: ${response.message} (${response.errorCode}).")
                    showAlert("Error occurred in logging in (${response.errorCode}).")
                }
                setLoading(false)
            } catch (e: Exception) {
                showAlert("Error: ${e.localizedMessage}")
            }
        }
    }

    fun logout(callback: (() -> Unit)? = null) {
        SharedPreferencesManager.clearPrefs(encryptedSharedPreferences)
        if (callback !== null) callback()
    }
}