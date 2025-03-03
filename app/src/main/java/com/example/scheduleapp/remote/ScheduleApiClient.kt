package com.example.scheduleapp.remote

import android.content.Context
import com.example.scheduleapp.utils.NetworkConstants.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ScheduleApiClient {
    private lateinit var apiService: ScheduleApiService

    fun getApiService(context: Context): ScheduleApiService {
        if (!::apiService.isInitialized) {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkhttpClient(context))
                .build()

            apiService = retrofit.create(ScheduleApiService::class.java)
        }

        return apiService
    }

    private fun getOkhttpClient(context: Context): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .build()
}