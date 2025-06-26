package com.prafullkumar.stockstream.domain.common

/**
 * A sealed class that encapsulates the result of an API operation
 * Provides a type-safe way to handle loading, success, and error states
 */
sealed class ApiResult<out T> {

    /**
     * Represents a loading state
     * @param isLoading indicates if the operation is in progress
     */
    data class Loading<T>(val isLoading: Boolean = true) : ApiResult<T>()

    /**
     * Represents a successful operation
     * @param data the result data of type T
     */
    data class Success<T>(val data: T) : ApiResult<T>()

    /**
     * Represents an error state
     * @param exception the exception that occurred
     * @param message human-readable error message
     * @param code optional error code (HTTP status, custom error codes)
     */
    data class Error<T>(
        val exception: Exception? = null,
        val message: String,
        val code: Int? = null
    ) : ApiResult<T>()

    /**
     * Represents an empty state (no data available)
     * Useful for empty lists, search results, etc.
     */
    data class Empty<T>(val message: String = "No data available") : ApiResult<T>()
}

/**
 * Extension functions to make ApiResult easier to work with
 */

// Check states
val <T> ApiResult<T>.isLoading: Boolean
    get() = this is ApiResult.Loading

val <T> ApiResult<T>.isSuccess: Boolean
    get() = this is ApiResult.Success

val <T> ApiResult<T>.isError: Boolean
    get() = this is ApiResult.Error

val <T> ApiResult<T>.isEmpty: Boolean
    get() = this is ApiResult.Empty

// Safe data access
val <T> ApiResult<T>.data: T?
    get() = if (this is ApiResult.Success) data else null

val <T> ApiResult<T>.errorMessage: String?
    get() = if (this is ApiResult.Error) message else null

// Transform data
inline fun <T, R> ApiResult<T>.map(transform: (T) -> R): ApiResult<R> {
    return when (this) {
        is ApiResult.Loading -> ApiResult.Loading()
        is ApiResult.Success -> ApiResult.Success(transform(data))
        is ApiResult.Error -> ApiResult.Error(exception, message, code)
        is ApiResult.Empty -> ApiResult.Empty(message)
    }
}

// Handle success case
inline fun <T> ApiResult<T>.onSuccess(action: (T) -> Unit): ApiResult<T> {
    if (this is ApiResult.Success) action(data)
    return this
}

// Handle error case
inline fun <T> ApiResult<T>.onError(action: (String, Exception?) -> Unit): ApiResult<T> {
    if (this is ApiResult.Error) action(message, exception)
    return this
}

// Handle loading case
inline fun <T> ApiResult<T>.onLoading(action: () -> Unit): ApiResult<T> {
    if (this is ApiResult.Loading) action()
    return this
}
