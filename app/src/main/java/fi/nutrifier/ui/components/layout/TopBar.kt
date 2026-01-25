package fi.nutrifier.ui.components.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fi.nutrifier.ui.components.misc.UserFeedbackMessage
import fi.nutrifier.utils.AlertType
import fi.nutrifier.utils.LocalApplicationContext
import fi.nutrifier.utils.NetworkUtils.checkInternetConnection

/**
 * Composable function that displays a top bar with a title and optional subtitle.
 *
 * @param title The title to display.
 * @param subtitle The optional subtitle to display.
 */
@Composable
fun TopBar(
    title: String? = null,
    subtitle: (@Composable () -> Unit)? = null,
    tabs: (@Composable () -> Unit)? = null,
    actionButton: (@Composable () -> Unit)? = null,
    bottomPadding: Dp? = null,
) {

    val context = LocalApplicationContext.current
    val networkConnected = checkInternetConnection(context)

    Column {
        Row(
            modifier = Modifier.padding(24.dp, 24.dp, 24.dp, bottomPadding ?: 24.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                if (title != null) Text(text = title, style = MaterialTheme.typography.headlineLarge)
                if (subtitle != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    subtitle()
                }
                if (!networkConnected) {
                    Spacer(modifier = Modifier.height(8.dp))
                    UserFeedbackMessage("No internet connection", type = AlertType.ERROR)
                }
            }
            if (actionButton != null) {
                actionButton()
            }
        }
        if (tabs != null) {
            tabs()
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
