package dev.alvr.marvel.ui.base.state

sealed class ViewState<out T> {
    object Uninitialized : ViewState<Nothing>()
    object Loading : ViewState<Nothing>()
    data class Success<out T>(val value: T) : ViewState<T>()
    data class Failure(val error: Throwable) : ViewState<Nothing>()
}
