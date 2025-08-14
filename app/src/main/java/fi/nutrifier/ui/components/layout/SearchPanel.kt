package fi.nutrifier.ui.components.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fi.nutrifier.ui.components.misc.UserFeedbackMessage
import fi.nutrifier.viewmodels.ViewModelWrapper

/**
 * Composable function that displays a search panel.
 *
 * @param navController The [NavController] used for navigation.
 * @param viewModels The [ViewModelWrapper] containing view models needed for handling search.
 */
@Composable
fun SearchPanel(navController: NavController, viewModels: ViewModelWrapper) {
    if (viewModels.search.alert != null) {
        Spacer(modifier = Modifier.height(8.dp))
        UserFeedbackMessage(viewModels.search.alert!!.message, "error")
    } else {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxSize()
                .padding(8.dp)
        ) {
            if (viewModels.search.loading) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CircularProgressIndicator()
                }
            } else {
                RecipeList(
                    navController = navController,
                    recipes = viewModels.search.searchResults.filterNotNull(),
                    viewModels = viewModels
                ) {
                    UserFeedbackMessage(message = "Sorry, no results for this query!")
                }
            }
        }
    }
}
