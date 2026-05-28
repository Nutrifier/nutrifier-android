package fi.nutrifier.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.viewModelScope
import fi.nutrifier.models.database.AuthRequest
import fi.nutrifier.models.database.RegisterRequest
import fi.nutrifier.repositories.database.AuthRepository
import fi.nutrifier.utils.AlertType
import fi.nutrifier.utils.SharedPreferencesManager
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository,
    encryptedSharedPreferences: SharedPreferences,
): BaseViewModel(encryptedSharedPreferences) {

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

    fun register(registerRequest: RegisterRequest, callback: (token: String) -> Unit) {
        setLoading(true)
        Log.d("AuthViewModel", "register 1")

        viewModelScope.launch {
            try {
                // Calling the api for auth token
                Log.d("AuthViewModel", "register 2")

                val response = repository.register(registerRequest)
                if (response.isSuccessful() && response.value != null) {
                    Log.d("RegisterResponse", response.value.toString())

                    showAlert("Registeration successful!", AlertType.INFO)

                    Log.d("Register", "Register token: ${response.value.token}")

                    // Saving the token to SharedPrefs
                    SharedPreferencesManager.saveAuthToken(
                        encryptedSharedPreferences,
                        response.value.token,
                    )

                    callback(response.value.token)
                } else {
                    Log.d("AuthViewModel", "Ongelma rekisteröitymisessä: ${response.message} (${response.errorCode}).")
                    showAlert("Error occurred in registering (${response.errorCode}).")
                }
                setLoading(false)
            } catch (e: Exception) {
                Log.d("AuthViewModel", "register error $e")

                showAlert("Error: ${e.localizedMessage}")
            }
        }
    }

    fun login(authRequest: AuthRequest, callback: (token: String) -> Unit) {
        setLoading(true)

        viewModelScope.launch {
            try {
                // Calling the api for auth token
                val response = repository.login(authRequest)
                Log.d("AuthViewModel", "Login response: $response")

                if (response.isSuccessful() && response.value != null) {
                    Log.d("RegisterResponse", response.value.toString())

                    Log.d("Login", "Login token: ${response.value.token}")

                    // Saving the token to SharedPrefs
                    SharedPreferencesManager.saveAuthToken(
                        encryptedSharedPreferences,
                        response.value.token,
                    )

                    callback(response.value.token)
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