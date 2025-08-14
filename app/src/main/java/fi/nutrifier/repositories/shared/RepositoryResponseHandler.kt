package fi.nutrifier.repositories.shared

/**
 * A generic data class for handling responses from repository operations.
 *
 * @property success The successful result of the operation, if any.
 * @property error The error message in case of failure, if any.
 */
data class RepositoryResponseHandler<T>(
    val success: T? = null,
    val error: String? = null,
)