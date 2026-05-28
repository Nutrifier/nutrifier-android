package fi.nutrifier.ui.screens.register

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fi.nutrifier.ui.components.inputs.PreviousNextOption
import fi.nutrifier.ui.components.layout.TopBar
import fi.nutrifier.ui.screens.BaseScreen
import fi.nutrifier.ui.screens.register.steps.ActivityStep
import fi.nutrifier.ui.screens.register.steps.CredentialsStep
import fi.nutrifier.ui.screens.register.steps.MealPlanStep
import fi.nutrifier.ui.screens.register.steps.ProfileStep
import fi.nutrifier.ui.screens.register.steps.GoalsStep
import fi.nutrifier.utils.Constants.emptyGoal
import fi.nutrifier.utils.Constants.emptyProfile
import fi.nutrifier.utils.Enums
import fi.nutrifier.utils.RegistrationFormState
import fi.nutrifier.viewmodels.ViewModelWrapper

enum class SetupScreenStep {
    CREDENTIAL,
    PROFILE,
    ACTIVITY,
    GOAL,
    MEAL_PLAN,
}

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModels: ViewModelWrapper,
) {
    var stepIndex by remember { mutableIntStateOf(0) }
    val currentStep = SetupScreenStep.entries[stepIndex]
    var newGoals by remember { mutableStateOf(viewModels.goals.goals ?: emptyGoal) }
    var newProfile by remember { mutableStateOf(viewModels.profile.profile ?: emptyProfile) }
    var currentWeight by remember { mutableStateOf<Double?>(null) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var registrationFormState by remember { mutableStateOf(RegistrationFormState(
        profile = newProfile,
        goal = newGoals,
    )) }

    // TODO: Fix this! This seems to something not be null when it should
    val isRegistering = viewModels.user.user == null

    LaunchedEffect(viewModels.user.user, isRegistering) {
        Log.d("RegisterScreen", "User: ${viewModels.user.user}")
        Log.d("RegisterScreen", "isRegistering: ${isRegistering}")
    }

    LaunchedEffect(viewModels.goals.goals) {
        if (viewModels.goals.goals != null) {
            newGoals = viewModels.goals.goals!!
        }
    }

    fun handleStepIncrement(direction: Int) {
        val newStepIndex = stepIndex + direction
        stepIndex = newStepIndex.coerceIn(0, SetupScreenStep.entries.lastIndex)
    }

    fun handleRegistration() {
        Log.d("RegisterScreen", "Handling registration...")
        registrationFormState = registrationFormState.validateGoals()
        if (registrationFormState.isProfileValid) {
            Log.d("RegisterScreen", "inside if")

            val registerRequest = registrationFormState.toRegistrationRequest()
            Log.d("RegisterScreen", "registerRequest $registerRequest")

            if (registerRequest != null) {
                viewModels.authViewModel.register(registerRequest) {
                    viewModels.user.getUser()
                    viewModels.goals.getGoals()
                    viewModels.profile.getProfile()
                    viewModels.settings.getSettings()
                    viewModels.weight.getWeighIns()
                    handleStepIncrement(1)
                }
            }
        } else {
            Log.d("RegisterScreen", "Form validation failed: $registrationFormState")
        }
    }

    BaseScreen(
        topBar = { TopBar(
            actionButton = if (currentStep == SetupScreenStep.MEAL_PLAN) {
                { TextButton(onClick = { navController.navigate("logs") {
                    popUpTo("login") { inclusive = true }
                } }) { Text("Skip") } }
            } else null
        ) },
        bottomBar = {
            PreviousNextOption(
                allowNext = true,
                showPrevious = stepIndex > 0,
                nextButtonText = if (stepIndex == SetupScreenStep.entries.lastIndex) "Save" else "Next",
                padding = PaddingValues(24.dp)
            ) { it ->
                if (it < 0) {
                    handleStepIncrement(it)
                } else {
                    when (currentStep) {
                        SetupScreenStep.CREDENTIAL -> {
                            if (isRegistering) {
                                registrationFormState = registrationFormState.validateCredential()

                                Log.d("RegisterScreen", "registrationFormState $registrationFormState")

                                if (registrationFormState.isCredentialValid) {
                                    handleStepIncrement(it)
                                } else {
                                    Log.d("RegisterScreen", "Credential not valid!")
                                }
                            }
                        }
                        SetupScreenStep.PROFILE -> {
                            if (!isRegistering) {
                                Log.d("SetupScreen", "Trying to save profile: $newProfile")
                                viewModels.profile.updateProfile(newProfile)
                            } else {
                                registrationFormState = registrationFormState.validateProfile()

                                Log.d("RegisterScreen", "registrationFormState $registrationFormState")

                                if (registrationFormState.isProfileValid) {
                                    handleStepIncrement(it)
                                } else {
                                    Log.d("RegisterScreen", "Profile not valid!")
                                }
                            }
                        }
                        SetupScreenStep.ACTIVITY -> {
                            if (!isRegistering) {
                                Log.d("SetupScreen", "Trying to save activity: $newProfile")
                                viewModels.profile.updateProfile(newProfile)
                            } else {
                                registrationFormState = registrationFormState.validateActivity()

                                Log.d("RegisterScreen", "registrationFormState $registrationFormState")

                                if (registrationFormState.isActivityValid) {
                                    handleStepIncrement(it)
                                } else {
                                    Log.d("RegisterScreen", "Activity not valid!")
                                }
                            }
                        }
                        SetupScreenStep.GOAL -> {
                            // If profile is null, we are registering
                            if (isRegistering) {
                                handleRegistration()
                            } else {
                                Log.d("SetupScreen", "Trying to save goals: $newGoals")
                                viewModels.goals.updateGoals(newGoals)
                                handleStepIncrement(it)
                            }
                        }
                        SetupScreenStep.MEAL_PLAN -> {
                            //Log.d("SetupScreen", "Trying to save meal plan: $newMealPlan")
                            //viewModels.goals.updateGoals(newGoals)
                            navController.navigate("logs")
                        }
                    }
                }
            }
        },
        screen = Enums.Screen.SETUP,
        viewModels,
        navController,
        padding = PaddingValues(0.dp),

    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(52.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                when (currentStep) {
                    SetupScreenStep.CREDENTIAL -> CredentialsStep(registrationFormState) {
                        registrationFormState = it
                    }
                    SetupScreenStep.PROFILE -> ProfileStep(registrationFormState) {
                        registrationFormState = it
                    }
                    SetupScreenStep.ACTIVITY -> ActivityStep(registrationFormState) {
                        registrationFormState = it
                    }
                    SetupScreenStep.GOAL -> GoalsStep(
                        viewModels,
                        navController,
                        registrationFormState,
                        { registrationFormState = it },
                        newGoals,
                    ) {
                        Log.d("SetupScreen", "Updating goals... $it")
                        newGoals = it
                    }
                    else -> MealPlanStep(viewModels)
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
