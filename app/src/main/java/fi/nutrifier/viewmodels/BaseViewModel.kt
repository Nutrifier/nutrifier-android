package fi.nutrifier.viewmodels

import android.app.Application
import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import fi.nutrifier.utils.Alert
import fi.nutrifier.utils.AlertType
import fi.nutrifier.utils.SharedPreferencesKeys
import fi.nutrifier.utils.SharedPreferencesManager
import java.util.UUID

open class BaseViewModel(application: Application): AndroidViewModel(application) {
    protected val encryptedSharedPreferences: SharedPreferences;

    init {
        val masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        encryptedSharedPreferences = EncryptedSharedPreferences.create(
            SharedPreferencesKeys.SECRET_PREFS_NAME,
            masterKey,
            application.applicationContext,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private val _loading: MutableState<Boolean> = mutableStateOf(false)
    val loading get() = _loading.value
    val setLoading: (Boolean) -> Unit = { _loading.value = it }

    private val _alert: MutableState<Alert?> = mutableStateOf(null)
    val alert get() = _alert.value

    fun showAlert(message: String, type: AlertType? = AlertType.ERROR) {
        _alert.value = Alert(message, type)
    }

    fun getUserId(): String {
        return SharedPreferencesManager.getUser(encryptedSharedPreferences)?.id
            ?: "ff9bd4c4-8292-49ad-8b56-9c638051212e"
    }

    fun clearAlert() {
        _alert.value = null
    }
}