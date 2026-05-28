package fi.nutrifier.viewmodels

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import fi.nutrifier.models.database.GoalPeriod
import fi.nutrifier.models.database.Goal
import fi.nutrifier.repositories.database.GoalsRepository
import fi.nutrifier.repositories.database.SelectedDateRepository
import kotlinx.coroutines.launch
import java.time.LocalDate

class GoalsViewModel(
    private val repository: GoalsRepository,
    encryptedSharedPreferences: SharedPreferences
): BaseViewModel(encryptedSharedPreferences) {

    private val selectedDateRepository = SelectedDateRepository()

    private var _goal: MutableState<Goal?> = mutableStateOf(null)
    val goals get() = _goal.value

    private var _newGoal: MutableState<Goal?> = mutableStateOf(null)
    val newGoals get() = _newGoal.value

    private var _currentGoalPeriod: MutableState<GoalPeriod?> = mutableStateOf(null)
    val currentGoalPeriod get() = _currentGoalPeriod.value

    fun getGoals() {
        setLoading(true)

        viewModelScope.launch {
            try {
                val goalsResponse = repository.getGoals()
                if (goalsResponse.isSuccessful() && goalsResponse.value != null) {
                    _goal.value = goalsResponse.value
                    Log.d("GoalsViewModel", "GET: success ${goalsResponse.value}")
                }

                setLoading(false)
            } catch (e: Exception) {
                showAlert("Error: ${e.localizedMessage}")
            }
        }
    }

    fun updateGoals(updatedGoal: Goal) {
        // Update current session's goals even though if the persistence fails
        _goal.value = updatedGoal

        viewModelScope.launch {
            setLoading(true)

            try {
                val response = repository.updateGoals(updatedGoal)
                if (response.isSuccessful() && response.value != null) {
                    Log.d("GoalsViewModel", "Goals updated: ${response.value}")
                } else {
                    Log.d("GoalsViewModel", "Error updating goals: ${response.message} (${response.errorCode}).")
                    showAlert("Couldn't save user goals (${response.errorCode}).")
                }

                setLoading(false)
            } catch (e: Exception) {
                showAlert("Error: ${e.localizedMessage}")
            }
        }
    }

    fun calculate(updateAfterCalculation: Boolean = false) {
        viewModelScope.launch {
            setLoading(true)

            try {
                val response = repository.calculate()
                if (response.isSuccessful() && response.value != null) {
                    Log.d("GoalsViewModel", "Goals updated: ${response.value}")

                    if (updateAfterCalculation) {
                        updateGoals(response.value)
                    }
                } else {
                    Log.d("GoalsViewModel", "Error updating goals: ${response.message} (${response.errorCode}).")
                    showAlert("Couldn't save user goals (${response.errorCode}).")
                }

                setLoading(false)
            } catch (e: Exception) {
                showAlert("Error: ${e.localizedMessage}")
            }
        }
    }

    fun clear() {
        _goal.value = null
    }
}
