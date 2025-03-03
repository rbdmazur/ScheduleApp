package com.example.scheduleapp.remote

import com.example.scheduleapp.remote.model.StudentRemote
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.UUID

interface ScheduleApiService {

    @GET("/user")
    suspend fun getStudent(@Query("id")id: UUID): StudentRemote
}