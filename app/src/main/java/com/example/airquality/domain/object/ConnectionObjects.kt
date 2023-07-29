package com.example.airquality.domain.`object`

/**
 * Wrapper class for events that should be processed only once
 * It is necessary to wrap [Live Data]-events for [Toast] and for navigation into this class to avoid repeated calls
 *
 * Used as a wrapper for data that is provided through the current data representing the event
 * received from https://medium.com/androiddevelopers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150
 */
open class EventWrapper<out T>(private val content: T) {

    var hasBeenHandled = false
        private set

    /**
     * Returns the content and prevents its use again.
     */
    fun get(): T? = if (hasBeenHandled) {
        null
    } else {
        hasBeenHandled = true
        content
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peek(): T = content
}

enum class Status {
    SHOW_LOADING,
    HIDE_LOADING,
    SHOW_PULL_TO_REFRESH_LOADING,
    HIDE_PULL_TO_REFRESH_LOADING,
    SHOW_PAGGING_LOADING,
    HIDE_PAGGING_LOADING,
    ERROR,
    SUCCESS,
    REDIRECT_LOGIN
}

/**
 * The model is used to validate fields or something
 * Processing takes place exclusively in the ViewModel
 */
data class UIValidation(
    var type : String,
    var message : String
)

sealed class ResultApi<out T : Any> {

    data class Success<out T : Any>(val data: T?) : ResultApi<T>()
    data class HttpError<T>(val error: T?, val code: Int = 0) : ResultApi<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is HttpError<*> -> "Error[exception=${error}]"
        }
    }
}

/**
 * Types of progress bar calls
 */
enum class LoadingType{
    /**
     * Standard
     */
    DEFAULT,

    /**
     * on Swipe
     */
    PULL_TO_REFRESH,

    /**
     * on Paging
     */
    PAGGING,

    /**
     * Do not show Progress bar
     */
    NONE
}

interface NetworkErrorHttpPrinter<T> {

    fun print(response: String?, default: String?): T
}