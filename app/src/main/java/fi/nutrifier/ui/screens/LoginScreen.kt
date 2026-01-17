package fi.nutrifier.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fi.nutrifier.models.database.AuthRequest
import fi.nutrifier.ui.components.misc.UserFeedbackMessage
import fi.nutrifier.utils.AlertType
import fi.nutrifier.utils.Constants.Screen
import fi.nutrifier.utils.LocalApplicationContext
import fi.nutrifier.utils.NetworkUtils.checkInternetConnection
import fi.nutrifier.viewmodels.ViewModelWrapper

@Composable
fun LoginScreen(
    navController: NavController,
    viewModels: ViewModelWrapper,
    snackbarHostState: SnackbarHostState,
) {
    val context = LocalApplicationContext.current
    var mode by remember { mutableStateOf("LOGIN") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val isLoading by viewModels.authViewModel.loading.collectAsState()
    val networkConnected = checkInternetConnection(context)

    fun navigateToMainScreen() {
        navController.navigate("discover") {
            popUpTo("login") { inclusive = true } // Remove login from stack
        }
    }

    // Check if user's auth token is found and get the user by id
    LaunchedEffect(key1 = true) {
        if (viewModels.authViewModel.checkAuthToken()) {
            viewModels.user.getUser()
            navigateToMainScreen()
        }
    }

    fun toggleMode() {
        mode = if (mode == "LOGIN") "REGISTER" else "LOGIN"
    }
    
    fun handleLogin() {
        val authRequest = AuthRequest(email, password)
        Log.d("AuthScreen", "Auth: ${authRequest}")
        if (mode == "LOGIN") {
            viewModels.authViewModel.login(authRequest) {
                viewModels.user.getUser()
                navigateToMainScreen()
            }
        } else {
            viewModels.authViewModel.register(authRequest) { navigateToMainScreen() }
        }
    }

    BaseScreen(
        topBar = {},
        bottomBar = {},
        screen = Screen.LOGIN,
        viewModels,
        navController,
        snackbarHostState,
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
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Email") },
                    value = email,
                    onValueChange = { email = it },
                )
                Spacer(modifier = Modifier.padding(vertical = 8.dp))
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Password") },
                    value = password,
                    onValueChange = { password = it },
                    singleLine = true,
                    visualTransformation =
                    if (passwordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector =
                                if (passwordVisible) Icons.Filled.VisibilityOff
                                else Icons.Filled.Visibility
                                ,
                                contentDescription =
                                if (passwordVisible) "Hide password"
                                else "Show password"
                            )
                        }
                    }
                )
                Spacer(modifier = Modifier.padding(vertical = 16.dp))
                Button(onClick = { handleLogin() }, modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(text = if (mode == "LOGIN") "Login" else "Register")
                    }
                }
                TextButton(onClick = { toggleMode() }) {
                    Text(text = if (mode == "LOGIN") "Register" else "Login")
                }
            }
        }
    }
}