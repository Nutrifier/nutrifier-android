package fi.nutrifier.ui.screens.recipe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fi.nutrifier.R
import fi.nutrifier.ui.components.dialogs.DeleteDialog
import fi.nutrifier.ui.components.layout.IngredientRow
import fi.nutrifier.ui.components.layout.InstructionRow
import fi.nutrifier.ui.components.inputs.NumberCounter
import fi.nutrifier.ui.components.inputs.NutrientInputRow
import fi.nutrifier.ui.components.misc.RecipeImage
import fi.nutrifier.ui.components.misc.UserFeedbackMessage
import fi.nutrifier.utils.AlertType
import fi.nutrifier.viewmodels.ViewModelWrapper

/**
 * Composable function for rendering the recipe screen.
 *
 * @param navController The navigation controller for navigating between screens.
 * @param viewModels The [ViewModelWrapper] containing view models for recipe inspection and favorites.
 * @param isPreview Flag indicating whether the screen is in preview mode.
 */
@Composable
fun RecipeScreen(
    navController: NavController,
    viewModels: ViewModelWrapper,
    isPreview: Boolean = false,
) {
    val recipe by viewModels.inspection.recipe
    var imageLoading by remember { mutableStateOf(true) }
    var isFavourite by remember { mutableStateOf(false) }
    var showMore by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showServingsChangedNotice by remember { mutableStateOf(false) }
    var initialServings by remember { mutableDoubleStateOf(recipe.servings) }
    val isLoading by viewModels.inspection.loading.collectAsState()

    LaunchedEffect(Unit) {
        if (!recipe.isPersonalRecipe && !isPreview) {
            viewModels.inspection.fetchRecipe(recipe.id)

            // Checking if the recipe is a favourite and updating state based on it
            val recipeIds = viewModels.favourite.recipes.map { it.id }
            isFavourite = recipeIds.contains(recipe.id)
        }

        // Updating initial servings state
        initialServings = recipe.servings
    }

    fun handleFavouriteClick() {
        isFavourite = !isFavourite
        if (isFavourite) {
            viewModels.favourite.add(recipe)
        } else {
            viewModels.favourite.delete(recipe)
        }
    }

    fun handleEditClick() {
        navController.navigate("recipe_editor")
    }

    fun handleDeleteClick() {
        showDeleteDialog = true
    }

    fun calculateIngredients(newServings: Double) {
        showServingsChangedNotice = newServings != initialServings

        // Calculate new ingredient amounts based on new servings size
        val ingredientsPerServing = recipe.ingredients.map { it.amount / recipe.servings }
        val newIngredientAmounts = ingredientsPerServing.map { it * newServings }

        // Forming a new ingredient list
        val newIngredients = recipe.ingredients.mapIndexed { index, item ->
            item.copy(amount = newIngredientAmounts[index].toFloat())
        }

        // Saving a new recipe with updated amounts to view model
        val newRecipe = newIngredients.let { recipe.copy(ingredients = it, servings = newServings) }
        viewModels.inspection.setRecipe(newRecipe)
    }

    val titleStyle = SpanStyle(
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
    )
    val subtitleStyle = SpanStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        color = Color.Gray,
    )

    Column(modifier =
        if (isPreview) Modifier.padding(32.dp)
        else Modifier.padding(32.dp).verticalScroll(rememberScrollState())
    ) {
        if (isLoading) LinearProgressIndicator()
        else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    overflow = TextOverflow.Visible,
                    modifier = Modifier.weight(1f),
                    style = TextStyle(lineHeight = 32.sp),
                    text = buildAnnotatedString {
                        withStyle(style = titleStyle) {
                            append(recipe.title)
                        }
                        withStyle(style = subtitleStyle) {
                            append(" #${if (recipe.id == -1) "UNKNOWN" else recipe.id }")
                        }
                    },
                )
                if (recipe.isPersonalRecipe) {
                    Column {
                        IconButton(
                            enabled = !isPreview,
                            onClick = { showMore = !showMore }
                        ) {
                            Icon(Icons.Default.MoreVert, "more")
                        }
                        DropdownMenu(expanded = showMore, onDismissRequest = { showMore = false }) {
                            DropdownMenuItem(
                                modifier = Modifier.height(44.dp),
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Rounded.Edit,
                                            contentDescription = "edit",
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = "Edit")
                                    }
                                },
                                onClick = { handleEditClick() }
                            )
                            DropdownMenuItem(
                                modifier = Modifier.height(44.dp),
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Delete,
                                            contentDescription = "delete",
                                            tint = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Delete",
                                            style = TextStyle(color = MaterialTheme.colorScheme.error)
                                        )
                                    }
                                },
                                onClick = { handleDeleteClick() }
                            )
                        }
                        DeleteDialog(
                            navController,
                            viewModels,
                            isVisible = showDeleteDialog,
                            exitDialog = { showDeleteDialog = false }
                        )
                    }
                } else {
                    IconButton(
                        enabled = !isPreview,
                        onClick = { handleFavouriteClick() }
                    ) {
                        Icon(
                            imageVector = if (isFavourite) {
                                Icons.Rounded.Star
                            } else {
                                Icons.Rounded.StarBorder
                            },
                            contentDescription = "star icon",
                            modifier = Modifier
                                .width(32.dp)
                                .height(32.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            /* === IMAGE SECTION === */
            if (recipe.image != "") {
                if (imageLoading) CircularProgressIndicator()
                RecipeImage(
                    model = recipe.image,
                    onLoadSuccess = { imageLoading = false }
                )
            } else {
                RecipeImage(painter = painterResource(id = R.drawable.meal))
            }
            Spacer(modifier = Modifier.height(24.dp))

            /* === SERVINGS SECTION === */
            NumberCounter(
                value = recipe.servings,
                suffix = "servings",
                onNumberChange = { calculateIngredients(it) },
                max = 50.0,
            )
            Spacer(modifier = Modifier.height(24.dp))

            /* === INGREDIENTS SECTION === */
            if (recipe.ingredients.isNotEmpty()) {
                Text("Ingredients", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))
                recipe.ingredients.forEachIndexed { index, ingredient ->
                    IngredientRow(index, ingredient, viewModels)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            /* === INSTRUCTIONS SECTION === */
            if (recipe.instructions.isNotEmpty()) {
                Text("Instructions", style = MaterialTheme.typography.headlineMedium)
                if (showServingsChangedNotice) {
                    Spacer(modifier = Modifier.height(8.dp))
                    UserFeedbackMessage("Serving size changed and instructions are not updated accordingly. Instructions may include some serving size specific instructions.", type = AlertType.WARNING)
                }
                Spacer(modifier = Modifier.height(8.dp))
                recipe.instructions.forEachIndexed { index, instruction ->
                    InstructionRow(index, instruction)
                    Spacer(modifier = Modifier.height(16.dp))
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            /* === NUTRITION SECTION === */
            if (recipe.nutrition?.nutrients?.isNotEmpty() == true) {
                Text("Nutrition", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))
                recipe.nutrition?.nutrients?.forEachIndexed { index, info ->
                    NutrientInputRow(
                        text = info.name,
                        value = info.amount.toString(),
                        suffixText = info.unit,
                        editable = false,
                        showConnectingLine = true,
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
            // TODO: Add call to action to subscribe to premium for nutritional information
        }
    }
}
