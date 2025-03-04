package com.example.scheduleapp.utils


sealed class LoadingState<T>(val data: T? = null, val message: String? = null) {
    class Loading<T>(): LoadingState<T>()
    class Success<T>(data: T?): LoadingState<T>(data)
    class Failure<T>(message: String?): LoadingState<T>(message = message)
}