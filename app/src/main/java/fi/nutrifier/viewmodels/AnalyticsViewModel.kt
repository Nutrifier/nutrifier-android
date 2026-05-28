package fi.nutrifier.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import fi.nutrifier.models.database.Analytics
import fi.nutrifier.repositories.database.AnalyticsRepository
import fi.nutrifier.utils.Enums
import kotlinx.coroutines.launch
import java.time.LocalDate

class AnalyticsViewModel(
    private val repository: AnalyticsRepository,
    encryptedSharedPreferences: SharedPreferences
): BaseViewModel(encryptedSharedPreferences) {

    private var _selectedDate: MutableState<LocalDate> = mutableStateOf(LocalDate.now())
    val selectedDate get() = _selectedDate.value
    val setSelectedDate: (LocalDate) -> Unit = {
        _selectedDate.value = it
        getAnalytics()
    }

    private var _analyticsTimePeriod: MutableState<Enums.AnalyticsTimePeriod> = mutableStateOf(Enums.AnalyticsTimePeriod.WEEK)
    val analyticsTimePeriod get() = _analyticsTimePeriod.value
    val setTimePeriod: (Enums.AnalyticsTimePeriod) -> Unit = {
        _analyticsTimePeriod.value = it
        getAnalytics()
    }

    private var _analytics: MutableState<Analytics?> = mutableStateOf(null)
    val analytics get() = _analytics.value

    fun getAnalytics() {
        setLoading(true)

        Log.d("AnalyticsViewModel", "getAnalytics 1")

        viewModelScope.launch {
            try {
                Log.d("AnalyticsViewModel", "getAnalytics 2")
                val analyticsResponse = repository.getByDateAndPeriod(selectedDate, analyticsTimePeriod)
                Log.d("AnalyticsViewModel", "response $analyticsResponse")
                if (analyticsResponse.isSuccessful() && analyticsResponse.value != null) {
                    Log.d("AnalyticsViewModel", "getAnalytics 3")
                    _analytics.value = analyticsResponse.value
                    Log.d("AnalyticsViewModel", "GET: success $analyticsResponse")
                }
                Log.d("AnalyticsViewModel", "getAnalytics 4")

                setLoading(false)
            } catch (e: Exception) {
                Log.d("AnalyticsViewModel", "getAnalytics error $e")
                showAlert("Error: ${e.localizedMessage}")
            }
        }
    }
}
