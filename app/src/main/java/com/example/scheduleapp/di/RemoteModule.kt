package com.example.scheduleapp.di

import android.app.Application
import com.example.scheduleapp.remote.ScheduleApiService
import com.example.scheduleapp.remote.auth.AuthApiService
import com.example.scheduleapp.remote.auth.AuthInterceptor
import com.example.scheduleapp.remote.auth.AuthRepository
import com.example.scheduleapp.remote.auth.AuthRepositoryImplementation
import com.example.scheduleapp.remote.auth.SessionManager
import com.example.scheduleapp.utils.NetworkConstants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Provides
    @Singleton
    fun provideAuthApiService(): AuthApiService =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApiService::class.java)

    @Provides
    @Singleton
    fun provideSessionManager(app: Application) =
        SessionManager(app)


    @Provides
    @Singleton
    fun provideAuthRepository(api: AuthApiService, sessionManager: SessionManager): AuthRepository =
        AuthRepositoryImplementation(api, sessionManager)

    @Provides
    @Singleton
    fun provideAuthInterceptor(sessionManager: SessionManager): AuthInterceptor =
        AuthInterceptor(sessionManager)

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideScheduleApiService(okHttpClient: OkHttpClient): ScheduleApiService =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ScheduleApiService::class.java)
}