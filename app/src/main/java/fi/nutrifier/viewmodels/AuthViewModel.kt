package fi.nutrifier.viewmodels

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import fi.nutrifier.BuildConfig
import fi.nutrifier.models.database.AuthRequest
import fi.nutrifier.models.database.User
import fi.nutrifier.repositories.database.AuthRepository
import fi.nutrifier.utils.SharedPreferencesManager
import kotlinx.coroutines.launch

class AuthViewModel(application: Application): BaseViewModel(application) {
    private val repository = AuthRepository()

    private var _user: MutableState<User?> = mutableStateOf(null)
    val user get() = _user.value

    init {
        setLoading(true)
        Log.d("AuthViewModel", "Base url: ${BuildConfig.BASE_URL}")
    }

    // TODO: Add check that the auth token isn't expired
    fun checkAuthToken(): Boolean {
        val token = SharedPreferencesManager.getAuthToken(encryptedSharedPreferences)
        val user = SharedPreferencesManager.getUser(encryptedSharedPreferences)

        Log.d("AUTH", "Token found: $token")
        Log.d("AUTH", "User found: $user")

        setLoading(false)

        return token != null && user != null
    }

    fun register(authRequest: AuthRequest, callback: () -> Unit) {
        setLoading(true)
        viewModelScope.launch {
            try {
                // Calling the api for auth token
                val response = repository.register(authRequest)
                if (response.isSuccessful() && response.value != null) {
                    Log.d("RegisterResponse", response.value.toString())

                    // Saving the token to SharedPrefs
                    SharedPreferencesManager.saveAuthToken(
                        encryptedSharedPreferences,
                        response.value.token,
                    )

                    // Saving the user to SharedPrefs
                    val user = User(response.value.userId, response.value.userEmail)
                    SharedPreferencesManager.saveUser(encryptedSharedPreferences, user)
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

                    // Saving the token to SharedPrefs
                    SharedPreferencesManager.saveAuthToken(
                        encryptedSharedPreferences,
                        response.value.token,
                    )

                    // Saving the user to SharedPrefs
                    val user = User(response.value.userId, response.value.userEmail)

                    Log.d("AuthViewModel", "Kirjauduttu sisään käyttäjällä: $user")

                    SharedPreferencesManager.saveUser(encryptedSharedPreferences, user)
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