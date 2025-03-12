package com.example.scheduleapp.remote.auth

sealed class AuthResult<T>(val data: T? = null, val message: String = "") {
    class Authorized<T>(data: T? = null): AuthResult<T>(data)
    class Unauthorized<T>(message: String = ""): AuthResult<T>(message = message)
    class UnknownError<T>(message: String = ""): AuthResult<T>(message = message)
}