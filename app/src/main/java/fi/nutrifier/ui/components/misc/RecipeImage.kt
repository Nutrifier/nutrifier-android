package fi.nutrifier.ui.components.misc

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import fi.nutrifier.utils.Constants

/**
 * Composable function that displays a recipe image.
 *
 * @param model The data model for the image. Can be a resource ID, URL, URI or a String.
 * @param painter The [Painter] instance for the image.
 * @param isPreview Indicates whether the image is displayed as a preview.
 * @param onLoadError Callback function called when an error occurs while loading the image.
 * @param onLoadSuccess Callback function called when the image is successfully loaded.
 */
@Composable
fun RecipeImage(
    model: Any? = null,
    painter: Painter? = null,
    isPreview: Boolean = false,
    onLoadError: () -> Unit = {},
    onLoadSuccess: () -> Unit = {},
) {
    var fixedModel = model
    if (model.toString().startsWith("content://")) {
        fixedModel = Uri.parse(model.toString())
    }

    BoxWithConstraints {
        val screenWidth = maxWidth
        val imageModifier: Modifier = Modifier
            .aspectRatio(Constants.LANDSCAPE_ASPECT_RATIO)
            .clip(RoundedCornerShape(16.dp))
            .fillMaxWidth()
        val previewImageModifier: Modifier = Modifier
            .size(screenWidth, Constants.IMAGE_HEIGHT.dp)
            .clip(RoundedCornerShape(16.dp))
            .fillMaxWidth()

        if (fixedModel != null) {
            AsyncImage(
                model = fixedModel,
                contentDescription = "recipe",
                contentScale = ContentScale.Crop,
                modifier = if (isPreview) previewImageModifier else imageModifier,
                onError = { onLoadError() },
                onSuccess = { onLoadSuccess() },
            )
        } else if (painter != null) {
            Image(
                painter = painter,
                contentDescription = "recipe",
                contentScale = ContentScale.Crop,
                modifier = if (isPreview) previewImageModifier else imageModifier
            )
        }
    }
}
