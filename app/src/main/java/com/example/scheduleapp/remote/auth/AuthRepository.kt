package com.example.scheduleapp.remote.auth

import java.util.UUID

interface AuthRepository {
    suspend fun signIn(userData: UserData): AuthResult<UUID>
    suspend fun authenticate(): AuthResult<UUID>
}