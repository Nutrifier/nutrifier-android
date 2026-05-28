package fi.nutrifier.ui.screens.register.steps

import android.text.Layout
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fi.nutrifier.models.database.UserProfile
import fi.nutrifier.ui.components.inputs.Dropdown
import fi.nutrifier.ui.components.inputs.NumberCounter
import fi.nutrifier.ui.components.misc.LabeledComponent
import fi.nutrifier.ui.components.misc.TitleSubtitle
import fi.nutrifier.utils.ActivityErrors
import fi.nutrifier.utils.Constants
import fi.nutrifier.utils.Enums
import fi.nutrifier.utils.ProfileErrors
import fi.nutrifier.utils.RegistrationFormState
import fi.nutrifier.viewmodels.ViewModelWrapper

@Composable
fun ActivityStep(
    registrationFormState: RegistrationFormState,
    setRegistrationFormState: (RegistrationFormState) -> Unit,
) {
    TitleSubtitle(
        title = "Set Your Activity Level",
        subtitle = "Set how much activity you have in your everyday life ",
        alignment = Alignment.CenterHorizontally,
    )
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        LabeledComponent(
            label = "Activity level",
            error = registrationFormState.activityErrors.activityLevel,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Enums.ActivityLevel.entries.forEach {
                    Row(
                        verticalAlignment = Alignment.Top,
                    ) {
                        RadioButton(
                            selected = registrationFormState.activity.activityLevel == it,
                            onClick = {
                                setRegistrationFormState(
                                    registrationFormState.copy(
                                        activityErrors = ActivityErrors(),
                                        activity = registrationFormState.activity.copy(
                                            activityLevel = it
                                        )
                                    )
                                )
                            }
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(it.displayName, modifier = Modifier.padding(top = 8.dp))
                            Text(
                                text = it.description,
                                color = MaterialTheme.colorScheme.outline,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }
                }
            }
        }
    }
}