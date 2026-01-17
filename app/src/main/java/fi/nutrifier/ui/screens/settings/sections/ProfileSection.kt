package fi.nutrifier.ui.screens.settings.sections

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import fi.nutrifier.ui.components.misc.LabeledComponent
import fi.nutrifier.viewmodels.ViewModelWrapper

@Composable
internal fun ProfileSection(viewModels: ViewModelWrapper) {
    Section("Profile", "Edit your profile information.") {
        LabeledComponent("First name:") { Text("-") } // TODO: Implement name and change
        LabeledComponent("Last name:") { Text("-") } // TODO: Implement name and change
        LabeledComponent("Email:") { Text("${viewModels.user.user?.email}") } // TODO: Implement email change
        LabeledComponent("Password:") { Text("************") } // TODO: Implement password change
    }
}