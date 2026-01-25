package fi.nutrifier.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import fi.nutrifier.utils.Alert
import fi.nutrifier.utils.AlertType
import fi.nutrifier.utils.SharedPreferencesManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

open class BaseViewModel(
    protected val encryptedSharedPreferences: SharedPreferences
): ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _alert = MutableSharedFlow<Alert>()
    val alert: SharedFlow<Alert> = _alert

    fun setLoading(isLoading: Boolean) {
        _loading.value = isLoading
    }

    suspend fun showAlert(message: String, type: AlertType? = AlertType.ERROR) {
        _alert.emit(Alert(message, type))
    }

    fun getUserId(): String {
        return SharedPreferencesManager.getUser(encryptedSharedPreferences)?.id
            ?: "00000000-0000-0000-0000-000000000000"
    }
}