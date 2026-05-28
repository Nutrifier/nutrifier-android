package fi.nutrifier.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import fi.nutrifier.models.database.UserWeight
import fi.nutrifier.repositories.database.WeightRepository
import kotlinx.coroutines.launch

class WeightViewModel(
    private val repository: WeightRepository,
    encryptedSharedPreferences: SharedPreferences
): BaseViewModel(encryptedSharedPreferences) {

    private var _weights: MutableState<List<UserWeight>?> = mutableStateOf(null)
    val weights get() = _weights.value

    private var _current: MutableState<UserWeight?> = mutableStateOf(null)
    val current get() = _current.value

    fun getWeighIns(page: Int = 0, size: Int = 10) {
        setLoading(true)

        viewModelScope.launch {
            try {
                val response = repository.getWeighIns(page, size)
                if (response.isSuccessful() && response.value != null) {
                    _weights.value = response.value.content
                    _current.value = response.value.content.first()
                    Log.d("WeightViewModel", "GET: success")
                } else {
                    Log.d("WeightViewModel", "Error occurred in getting user weights: ${response.message} (${response.errorCode}).")
                    showAlert("Error occurred in getting the user (${response.errorCode}).")
                }

                setLoading(false)
            } catch (e: Exception) {
                showAlert("Error: ${e.localizedMessage}")
            }
        }
    }

    fun addNewWeighInt(weight: Double) {
        setLoading(true)

        viewModelScope.launch {
            try {
                // Token can be null if the token is saved in SharedPrefs
                val response = repository.addNewWeighIn(weight)
                Log.d("WeightViewModel", "Weight add response: $response")
                if (response.isSuccessful() && response.value != null) {
                    _current.value = response.value
                } else {
                    Log.d("WeightViewModel", "Error occurred in adding user weight: ${response.message} (${response.errorCode}).")
                    showAlert("Error occurred in adding weight (${response.errorCode}).")
                }

                setLoading(false)
            } catch (e: Exception) {
                showAlert("Error: ${e.localizedMessage}")
            }
        }
    }

    fun clear() {
        _weights.value = null
        _current.value = null
    }
}
