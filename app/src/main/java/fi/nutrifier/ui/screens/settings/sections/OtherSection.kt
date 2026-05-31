package fi.nutrifier.ui.screens.settings.sections

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fi.nutrifier.BuildConfig
import fi.nutrifier.viewmodels.ViewModelWrapper

@Composable
internal fun OtherSection(navController: NavController, viewModels: ViewModelWrapper) {

    fun logout() {
        viewModels.authViewModel.logout()
        viewModels.user.clear()
        viewModels.goals.clear()
        viewModels.profile.clear()
        viewModels.settings.clear()
        viewModels.weight.clear()
        navController.navigate("login")
    }

    Section(isCollapsible = false) {
        Button(
            onClick = ::logout,
            contentPadding = PaddingValues(32.dp, 0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.error,
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout")
        }
        Column {
            Text(
                text = "Account id: ${viewModels.user.user?.id ?: "-"}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Application version: ${BuildConfig.VERSION_NAME}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "API version: ${viewModels.settings.apiVersion ?: "-"}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}