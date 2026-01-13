package fi.nutrifier.ui.screens.settings.sections

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fi.nutrifier.viewmodels.ViewModelWrapper

@Composable
internal fun OtherSection(navController: NavController, viewModels: ViewModelWrapper) {
    fun logout() {
        viewModels.authViewModel.logout()
        navController.navigate("login")
    }

    Section(isCollapsible = false) {
        TextButton(onClick = ::logout, contentPadding = PaddingValues(8.dp, 0.dp)) {
            Text(text = "Logout", color = MaterialTheme.colorScheme.error)
        }
    }
}