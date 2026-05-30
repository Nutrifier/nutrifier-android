package fi.nutrifier.ui.screens.register.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fi.nutrifier.ui.components.inputs.NutrifierDropdown
import fi.nutrifier.ui.components.inputs.NumberCounter
import fi.nutrifier.ui.components.misc.LabeledComponent
import fi.nutrifier.ui.components.misc.TitleSubtitle
import fi.nutrifier.utils.Constants
import fi.nutrifier.utils.Enums
import fi.nutrifier.utils.ProfileErrors
import fi.nutrifier.utils.RegistrationFormState

@Composable
fun ProfileStep(
    registrationFormState: RegistrationFormState,
    setRegistrationFormState: (RegistrationFormState) -> Unit,
) {
    TitleSubtitle(
        title = "Set Your Profile",
        subtitle = "Set your ",
        alignment = Alignment.CenterHorizontally,
    )
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            LabeledComponent(
                label = "Gender/sex",
                error = registrationFormState.profileErrors.sex,
            ) {
                NutrifierDropdown(
                    value = registrationFormState.profile.sex,
                    items = Enums.Sex.entries.toList(),
                    labelMapper = { it.displayName },
                    modifier = Modifier.width(152.dp)
                ) {
                    //updateProfile(profile.copy(sex = it))
                    setRegistrationFormState(registrationFormState.copy(
                        profileErrors = ProfileErrors(),
                        profile = registrationFormState.profile.copy(
                            sex = it
                        )
                    ))
                }
            }
            LabeledComponent(
                label = "Age",
                error = registrationFormState.profileErrors.age,
            ) {
                NumberCounter(
                    value = registrationFormState.profile.age?.toDouble() ?: 0.0,
                    onNumberChange = {
                        //updateProfile(profile.copy(age = it.toInt()))
                        setRegistrationFormState(registrationFormState.copy(
                            profileErrors = ProfileErrors(),
                            profile = registrationFormState.profile.copy(
                                age = it.toInt()
                            )
                        ))
                    },
                    editable = true,
                )
            }
        }
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            LabeledComponent(
                label = "Height",
                error = registrationFormState.profileErrors.height,
            ) {
                NumberCounter(
                    value = registrationFormState.profile.height?.toDouble() ?: 0.0,
                    onNumberChange = {
                        //updateProfile(profile.copy(height = it.toInt()))
                        setRegistrationFormState(registrationFormState.copy(
                            profileErrors = ProfileErrors(),
                            profile = registrationFormState.profile.copy(
                                height = it.toInt()
                            )
                        ))
                    },
                    min = Constants.MIN_HEIGHT,
                    max = Constants.MAX_HEIGHT,
                    editable = true,
                )
            }
            LabeledComponent(
                label = "Weight",
                error = registrationFormState.profileErrors.weight,
            ) {
                NumberCounter(
                    value = registrationFormState.weight ?: 0.0,
                    onNumberChange = {
                        setRegistrationFormState(registrationFormState.copy(
                            profileErrors = ProfileErrors(),
                            weight = it,
                        ))
                    },
                    min = Constants.MIN_WEIGHT,
                    max = Constants.MAX_WEIGHT,
                    editable = true,
                )
            }
        }
    }
}