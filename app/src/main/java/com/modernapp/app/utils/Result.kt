package com.modernapp.app.utils

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val message: String, val cause: Throwable? = null) : Result<Nothing>()
    data object Loading : Result<Nothing>()

    val isSuccess get() = this is Success
    val isError get() = this is Error
    val isLoading get() = this is Loading

    fun getOrNull(): T? = if (this is Success) data else null

    fun getOrDefault(default: @UnsafeVariance T): T = if (this is Success) data else default
}

inline fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    if (this is Result.Success) action(data)
    return this
}

inline fun <T> Result<T>.onError(action: (String, Throwable?) -> Unit): Result<T> {
    if (this is Result.Error) action(message, cause)
    return this
}

inline fun <T> Result<T>.onLoading(action: () -> Unit): Result<T> {
    if (this is Result.Loading) action()
    return this
}

suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> {
    return try {
        Result.Success(apiCall())
    } catch (e: retrofit2.HttpException) {
        Result.Error(
            message = "HTTP Error ${e.code()}: ${e.message()}",
            cause = e
        )
    } catch (e: java.io.IOException) {
        Result.Error(
            message = "Network error. Please check your connection.",
            cause = e
        )
    } catch (e: Exception) {
        Result.Error(
            message = e.message ?: "An unexpected error occurred.",
            cause = e
        )
    }
}
