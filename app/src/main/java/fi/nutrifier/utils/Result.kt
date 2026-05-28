package fi.nutrifier.utils

data class Result<T>(
    val value: T? = null,
    val errorCode: Int? = null,
    val message: String? = null,
){
    companion object {
        fun <T> success(value: T?): Result<T> {
            return if (value != null) Result(value = value) else Result()
        }
        fun <T> fail(errorCode: Int, message: String? = null): Result<T> {
            return Result(
                errorCode = errorCode,
                message = message,
            )
        }
    }

    fun isSuccessful(): Boolean {
        return this.value != null && this.errorCode == null
    }
}