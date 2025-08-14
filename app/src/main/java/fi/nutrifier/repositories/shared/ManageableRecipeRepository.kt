package fi.nutrifier.repositories.shared

/**
 * Interface for a repository that manages recipes of type [T].
 *
 * @param T The type of recipe managed by the repository.
 */
interface ManageableRecipeRepository<T> {
    suspend fun getAll(): RepositoryResponseHandler<List<T>>
    suspend fun getById(id: Int): RepositoryResponseHandler<T?>
    suspend fun add(r: T): RepositoryResponseHandler<T?>
    suspend fun delete(r: T): RepositoryResponseHandler<T?>
}