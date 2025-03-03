package com.example.scheduleapp.remote

import com.example.scheduleapp.login.UserData
import com.example.scheduleapp.remote.model.AuthResponse
import com.example.scheduleapp.utils.NetworkConstants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface AuthApiService {

    @POST("/signin")
    suspend fun signIn(@Body userData: UserData?): AuthResponse
}

object AuthApi {
    val authService: AuthApiService = retrofit.create(AuthApiService::class.java)
}

