package fi.nutrifier.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fi.nutrifier.models.database.AuthRequest
import fi.nutrifier.ui.components.inputs.EmailField
import fi.nutrifier.ui.components.inputs.PasswordField
import fi.nutrifier.ui.components.misc.UserFeedbackMessage
import fi.nutrifier.utils.AlertType
import fi.nutrifier.utils.Enums
import fi.nutrifier.utils.LocalApplicationContext
import fi.nutrifier.utils.NetworkUtils.checkInternetConnection
import fi.nutrifier.viewmodels.ViewModelWrapper

@Composable
fun LoginScreen(
    navController: NavController,
    viewModels: ViewModelWrapper,
) {
    val context = LocalApplicationContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isLoading by viewModels.authViewModel.loading.collectAsState()
    val networkConnected = checkInternetConnection(context)

    fun navigateToRegisterScreen() {
        navController.navigate("register") {
            popUpTo("register") { inclusive = true } // Remove login from stack
        }
    }

    fun navigateToMainScreen() {
        navController.navigate("logs") {
            popUpTo("login") { inclusive = true } // Remove login from stack
        }
    }

    fun getInitialData() {
        viewModels.settings.getSettings()
        viewModels.goals.getGoals()
        viewModels.profile.getProfile()
        viewModels.weight.getWeighIns()
    }

    // Check if user's auth token is found and get the user by id
    LaunchedEffect(key1 = true) {
        if (viewModels.authViewModel.checkAuthToken()) {
            viewModels.user.getUser()
            getInitialData()
            navigateToMainScreen()
        }
    }
    
    fun handleLogin() {
        val authRequest = AuthRequest(email, password)
        viewModels.authViewModel.login(authRequest) { token ->
            viewModels.user.getUser(token)
            getInitialData()
            navigateToMainScreen()
        }
    }

    BaseScreen(
        topBar = {},
        bottomBar = {},
        screen = Enums.Screen.LOGIN,
        viewModels,
        navController,
    ) {
        if (!networkConnected) {
            Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                UserFeedbackMessage(
                    message = "No internet connection",
                    type = AlertType.ERROR,
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            }
        }
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (isLoading) CircularProgressIndicator()
            else {
                Text(text = "Recipe App", style = MaterialTheme.typography.headlineLarge)
                Spacer(modifier = Modifier.padding(vertical = 24.dp))
                EmailField(email) { email = it }
                Spacer(modifier = Modifier.padding(vertical = 8.dp))
                PasswordField(password) { password = it }
                Spacer(modifier = Modifier.padding(vertical = 16.dp))
                Button(onClick = { handleLogin() }, modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                ) {
                    Text(text = "Login")
                }
                TextButton(onClick = { navigateToRegisterScreen() }) {
                    Text(text = "Register")
                }
            }
        }
    }
}