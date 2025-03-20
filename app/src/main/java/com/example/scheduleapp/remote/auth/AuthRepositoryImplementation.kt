package com.example.scheduleapp.remote.auth

import android.util.Log
import com.example.scheduleapp.data.model.User
import com.example.scheduleapp.data.services.UserService
import retrofit2.HttpException
import java.net.ConnectException
import java.util.UUID

class AuthRepositoryImplementation(
    private val authApiService: AuthApiService,
    private val sessionManager: SessionManager
) : AuthRepository {
    private val userService = UserService()
    override suspend fun signIn(userData: UserData): AuthResult<UUID> {
        return try {
            val response = authApiService.signIn(userData)
            sessionManager.saveAuthToken(response.token, response.userId)
            val user = userService.getUserById(UUID.fromString(response.userId))
            if (user == null) {
                userService.insertUser(
                    User(
                        id = UUID.fromString(response.userId),
                        email = userData.email
                    )
                )
            }
            AuthResult.Authorized(UUID.fromString(response.userId))
        } catch (e: HttpException) {
            if(e.code() == 401) {
                AuthResult.Unauthorized(e.message())
            } else {
                AuthResult.UnknownError(e.message())
            }
        } catch (e: Exception) {
            AuthResult.UnknownError(e.message.toString())
        }
    }

    override suspend fun authenticate(): AuthResult<UUID> {
        val token = sessionManager.fetchAuthToken()
        if (token == null) {
            return AuthResult.Unauthorized()
        } else {
            val id = sessionManager.fetchUserId()
            if (id == null) {
                return AuthResult.Unauthorized()
            } else {
                try {
                    val header = mapOf(
                        "Authorization" to "Bearer $token"
                    )
                    authApiService.auth(header)
                    return AuthResult.Authorized(UUID.fromString(id))
                } catch (e: HttpException) {
                    Log.d("AUTH", e.code().toString())
                    return if (e.code() == 401) {
                        AuthResult.Unauthorized()
                    } else {
                        AuthResult.UnknownError()
                    }
                } catch (e: ConnectException) {
                    Log.d("AUTH", e.javaClass.name)
                    return AuthResult.AuthorizedOffline(UUID.fromString(id))
                } catch (e: Exception) {
                    return AuthResult.UnknownError()
                }
            }
        }
    }
}