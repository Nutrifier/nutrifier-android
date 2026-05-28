package fi.nutrifier.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fi.nutrifier.ui.components.buttons.MockButton
import fi.nutrifier.ui.components.dialogs.DevDialog
import fi.nutrifier.utils.Constants.IS_DEV
import fi.nutrifier.utils.Enums
import fi.nutrifier.viewmodels.ViewModelWrapper

@Composable
fun BaseScreen(
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
    screen: Enums.Screen,
    viewModels: ViewModelWrapper,
    navController: NavController,
    padding: PaddingValues? = null,
    floatingActionButton: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    var showMockDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { Column { topBar() }},
        bottomBar = { Column { bottomBar() }},
        modifier = Modifier.imePadding(),
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {
                if (IS_DEV) MockButton { showMockDialog = true }
                if (floatingActionButton !== null) floatingActionButton()
            }
        },
    ) {
        Column(modifier = Modifier.padding(padding ?: it).padding(horizontal = 24.dp)) {
            content()
            DevDialog(screen, viewModels, navController, showMockDialog) {
                showMockDialog = false
            }
        }
    }
}
