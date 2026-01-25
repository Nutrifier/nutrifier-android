package fi.nutrifier.ui.screens.recipe.steps

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import fi.nutrifier.R
import fi.nutrifier.ui.components.inputs.NumberCounter
import fi.nutrifier.ui.components.misc.RecipeImage
import fi.nutrifier.utils.Constants
import fi.nutrifier.utils.LocalApplicationContext
import fi.nutrifier.utils.ValidatorUtils
import fi.nutrifier.viewmodels.RecipeUnderInspectionViewModel

/**
 * Composable function for rendering the title step of the recipe editor.
 *
 * @param viewModel The [RecipeUnderInspectionViewModel] containing the recipe being edited.
 * @param handleAllowNextChange Callback function to handle changes in allowing navigation to the next step.
 * @param toNextStep Callback function to navigate to the next step.
 */
@Composable
internal fun TitleStep(
    viewModel: RecipeUnderInspectionViewModel,
    handleAllowNextChange: (Boolean) -> Unit,
    toNextStep: (Int) -> Unit
) {
    var title: String by remember { mutableStateOf(viewModel.recipe.value.title) }
    var servings by remember { mutableIntStateOf(viewModel.recipe.value.servings) }
    var image by remember { mutableStateOf(viewModel.recipe.value.image) }

    LaunchedEffect(title, servings, image) {
        val titleOk = ValidatorUtils.recipeTitle(title)
        val servingsOk = ValidatorUtils.recipeServings(servings)

        if (titleOk && servingsOk) {
            val newRecipe = viewModel.recipe.value.copy(
                title = title,
                servings = servings,
                image = image,
            )
            viewModel.setRecipe(newRecipe)
            handleAllowNextChange(true)
        } else {
            handleAllowNextChange(false)
        }
    }

    val context = LocalApplicationContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {
            if (it != null) {
                // Persist access to the uri even after app restarts
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                // Saving image uri string
                image = it.toString()
            }
        },
    )

    fun handlePickImage() {
        launcher.launch("image/*")
    }

    fun handleTakePhoto() {
        // TODO: Add functionality
    }

    /* === TITLE SECTION === */
    TextField(
        value = title,
        onValueChange = { title = it },
        placeholder = { Text("Insert title...") },
        label = { Text("Title *") },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { toNextStep(1) }
        )
    )
    Spacer(modifier = Modifier.height(24.dp))

    /* === SERVING SIZE SECTION === */
    Text("Set serving size:", style = MaterialTheme.typography.headlineSmall)
    Spacer(modifier = Modifier.height(8.dp))
    NumberCounter(
        value = servings,
        onNumberChange = { servings = it.toInt() },
        max = 40
    )
    Spacer(modifier = Modifier.height(24.dp))

    /* === IMAGE SECTION === */
    // TODO: Add take photo functionality
    Text("Change image:", style = MaterialTheme.typography.headlineSmall)
    Spacer(modifier = Modifier.height(8.dp))
    OutlinedButton(
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(0.dp),
        onClick = { handlePickImage() }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.height(Constants.IMAGE_HEIGHT.dp)
        ) {
            if (image == "") {
                RecipeImage(painter = painterResource(id = R.drawable.meal))
            } else {
                RecipeImage(model = image)
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .fillMaxSize()
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.7f),
                                Color.Black.copy(alpha = 0.3f),
                                Color.Black.copy(alpha = 0.7f),
                            ),
                            start = Offset(0f, 0f),
                            end = Offset(0f, Float.POSITIVE_INFINITY)
                        )
                    )
            )
            Icon(Icons.Rounded.Image, "image")
        }
    }
}
