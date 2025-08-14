package fi.nutrifier.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import fi.nutrifier.ui.components.buttons.MockButton
import fi.nutrifier.ui.components.dialogs.MockDialog
import fi.nutrifier.ui.components.misc.CustomSnackbar
import fi.nutrifier.utils.Constants.Screen
import fi.nutrifier.utils.Constants.IS_DEV
import fi.nutrifier.viewmodels.ViewModelWrapper

@Composable
fun Screen(
    topBar: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
    screen: Screen,
    viewModels: ViewModelWrapper,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    floatingActionButton: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    var showMockDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { Column {
            topBar()
        }},
        content = { Column(Modifier.padding(it)) {
            content()
            if (showMockDialog) {
                MockDialog(screen, viewModels, navController, snackbarHostState) { showMockDialog = false }
            }
        }},
        bottomBar = { Column {
            bottomBar()
        }},
        snackbarHost = { CustomSnackbar(snackbarHostState) },
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {
                if (IS_DEV) MockButton { showMockDialog = true }
                if (floatingActionButton !== null) floatingActionButton()
            }
        },
    )
}