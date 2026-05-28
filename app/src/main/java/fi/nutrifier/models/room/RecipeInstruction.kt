package fi.nutrifier.models.room

/**
 * Represents an instruction step in a recipe.
 *
 * @property number The sequential number of the instruction step.
 * @property text The text describing the instruction.
 */
data class RecipeInstruction(
    val number: Int,
    val text: String,
)
