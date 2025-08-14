package fi.nutrifier.utils

data class Result<T>(
    val value: T? = null,
    val errorCode: Int? = null,
    val message: String? = null,
){
    companion object {
        fun <T> success(result: T?): Result<T> {
            return if (result != null) Result(value = result) else Result()
        }
        fun <T> fail(result: Int, message: String? = null): Result<T> {
            return Result(errorCode = result)
        }
    }

    fun isSuccessful(): Boolean {
        return this.value != null && this.errorCode == null
    }
}