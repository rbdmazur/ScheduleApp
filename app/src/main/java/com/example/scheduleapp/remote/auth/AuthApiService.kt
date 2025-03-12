package com.example.scheduleapp.remote.auth

import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("/signin")
    suspend fun signIn(@Body userData: UserData?): AuthResponse
}

