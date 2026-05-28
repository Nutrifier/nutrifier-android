package fi.nutrifier.ui.screens.register.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import fi.nutrifier.ui.components.inputs.EmailField
import fi.nutrifier.ui.components.inputs.PasswordField
import fi.nutrifier.ui.components.misc.TitleSubtitle
import fi.nutrifier.utils.CredentialErrors
import fi.nutrifier.utils.RegistrationFormState

@Composable
fun CredentialsStep(
    registrationFormState: RegistrationFormState,
    setRegistrationFormState: (RegistrationFormState) -> Unit,
) {
    TitleSubtitle(
        title = "Set Your Credentials",
        subtitle = "Set your email and password for login again later.",
        alignment = Alignment.CenterHorizontally,
    )
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        EmailField(
            value = registrationFormState.credential.email ?: "",
            errorMessage = registrationFormState.credentialErrors.email,
        ) {
            setRegistrationFormState(registrationFormState.copy(
                credentialErrors = CredentialErrors(),
                credential = registrationFormState.credential.copy(
                    email = it
                )
            ))
        }
        PasswordField(
            value = registrationFormState.credential.password ?: "",
            errorMessage = registrationFormState.credentialErrors.password,
        ) {
            setRegistrationFormState(registrationFormState.copy(
                credentialErrors = CredentialErrors(),
                credential = registrationFormState.credential.copy(
                    password = it
                )
            ))
        }
    }
}