package fi.nutrifier.models.other

/**
 * Represents a fraction with its range.
 * @property range The range within which the fraction applies.
 * @property fraction The fraction represented as a string.
 */
data class Fraction(
    val range: ClosedFloatingPointRange<Double>,
    val fraction: String
)