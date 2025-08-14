package fi.nutrifier.ui.components.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import fi.nutrifier.utils.Constants.categories
import fi.nutrifier.viewmodels.ViewModelWrapper

/**
 * A composable function that displays a horizontal scrollable list of category buttons.
 * When a category button is clicked, it triggers a search and updates the UI to show the search results.
 *
 * @param viewModels The ViewModelWrapper that contains the view models needed for the search.
 * @param handleShowResult A lambda function to handle updating the showResult flag.
 */
@Composable
fun CategoryShelf(
    viewModels: ViewModelWrapper,
    handleShowResult: (Boolean) -> Unit
){

    fun handleCategoryClick(categoryQuery: String) {
        viewModels.search.search(categoryQuery)
        handleShowResult(true)
    }

    LazyRow {
        itemsIndexed(categories) { index, it ->
            TextButton(
                modifier = Modifier.width(96.dp).height(96.dp),
                shape = RoundedCornerShape(8.dp),
                onClick = { handleCategoryClick(it.query) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        contentScale = ContentScale.Fit,
                        painter = painterResource(id = it.drawableId),
                        contentDescription = "category",
                        modifier = Modifier.weight(0.8f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(it.title)
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}
