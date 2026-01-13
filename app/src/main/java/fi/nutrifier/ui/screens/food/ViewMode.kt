package fi.nutrifier.ui.screens.food

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import fi.nutrifier.ui.components.inputs.FoodForm
import fi.nutrifier.viewmodels.ViewModelWrapper

@Composable
internal fun ViewMode(navController: NavController, viewModels: ViewModelWrapper) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text("Add food", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(24.dp))
        FoodForm(navController, viewModels)
    }
}