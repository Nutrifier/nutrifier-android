package fi.nutrifier.utils

import android.util.Patterns
import fi.nutrifier.models.database.RegisterRequest
import fi.nutrifier.models.database.Goal
import fi.nutrifier.models.database.UserProfile

data class CredentialErrors(
    val email: String? = null,
    val password: String? = null,
)

data class ProfileErrors(
    val weight: String? = null,
    val height: String? = null,
    val age: String? = null,
    val sex: String? = null,
    val activityLevel: String? = null,
)

data class ActivityErrors(
    val activityLevel: String? = null,
)

data class GoalErrors(
    val reasoning: String? = null,
    val targetWeight: String? = null,
    val targetDate: String? = null,
    val reachedDate: String? = null,
    val periods: String? = null,
)

data class Credential(
    val email: String? = null,
    val password: String? = null,
)

data class Activity(
    val activityLevel: Enums.ActivityLevel? = null
)

data class RegistrationFormState(
    val credential: Credential = Credential(null, null),
    val profile: UserProfile = UserProfile(null, null, null, null),
    val activity: Activity = Activity(null),
    val goal: Goal = Goal(null, null, null, null, null, null, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0),
    val weight: Double? = null,

    var credentialErrors: CredentialErrors = CredentialErrors(),
    var profileErrors: ProfileErrors = ProfileErrors(),
    var activityErrors: ActivityErrors = ActivityErrors(),
    var goalErrors: GoalErrors = GoalErrors(),

    var isCredentialValid: Boolean = false,
    var isProfileValid: Boolean = false,
    var isActivityValid: Boolean = false,
    var isGoalValid: Boolean = false,
) {
    fun validateCredential(): RegistrationFormState {
        val errors = CredentialErrors(
            email = if (credential.email == null || credential.email.isBlank()) {
                "Email required"
            } else if (!Patterns.EMAIL_ADDRESS.matcher(credential.email).matches()) {
                "Invalid email"
            } else null,
            password = if (credential.password == null) {
                "Password required"
            } else if (credential.password.length < 8) {
                "Password too short"
            } else null,
        )

        val isValid = listOf(
            errors.email,
            errors.password,
        ).all { it == null }

        return copy(
            credentialErrors = errors,
            isCredentialValid = isValid,
        )
    }

    fun validateProfile(): RegistrationFormState {
        val errors = ProfileErrors(
            weight = if (weight == null) "Weight required" else null,
            height = if (profile.height == null) "Height required" else null,
            age = if (profile.age == null || profile.age < 18) "Must be 18+" else null,
            sex = if (profile.sex == null) "Sex required" else null, // TODO: Change this message :D
        )

        val isValid = listOf(
            errors.weight,
            errors.age,
            errors.sex,
            errors.height,
        ).all { it == null }

        return copy(
            profileErrors = errors,
            isProfileValid = isValid,
        )
    }

    fun validateActivity(): RegistrationFormState {
        val errors = ActivityErrors(
            activityLevel = if (activity.activityLevel == null) "Activity level required" else null,
        )

        val isValid = listOf(
            errors.activityLevel,
        ).all { it == null }

        return copy(
            activityErrors = errors,
            isActivityValid = isValid,
        )
    }

    fun validateGoals(): RegistrationFormState {
        val errors = GoalErrors(
            reasoning = if (goal.goalType == null) "Reasoning required" else null,
            targetWeight = if (goal.targetWeight == null) "Target weight required" else null,
            targetDate = if (goal.targetDate == null) "Target date required" else null,
            reachedDate = null, // No need to validate
        )

        val isValid = listOf(
            errors.reasoning,
            errors.targetWeight,
            errors.targetDate,
            errors.reachedDate,
            errors.periods
        ).all { it == null }

        return copy(
            goalErrors = errors,
            isGoalValid = isValid,
        )
    }

    fun toRegistrationRequest(): RegisterRequest? {
        if (isCredentialValid && isProfileValid && isActivityValid && isGoalValid) {
            return RegisterRequest(
                email = credential.email!!,
                password = credential.password!!,
                sex = profile.sex!!,
                age = profile.age!!,
                height = profile.height!!,
                activityLevel = activity.activityLevel!!,
                goalType = goal.goalType!!,
                currentWeight = weight!!,
                targetWeight = goal.targetWeight!!,
                targetDate = goal.targetDate!!,
            )
        }
        return null
    }
}