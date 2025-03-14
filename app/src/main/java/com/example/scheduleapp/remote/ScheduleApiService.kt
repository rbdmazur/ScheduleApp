package com.example.scheduleapp.remote

import com.example.scheduleapp.remote.model.ScheduleResponse
import com.example.scheduleapp.remote.model.StudentRemote
import com.example.scheduleapp.remote.model.StudiesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.UUID

interface ScheduleApiService {

    @GET("/user")
    suspend fun getStudent(@Query("id")id: UUID): StudentRemote

    @GET("/schedules")
    suspend fun getSchedules(): ScheduleResponse

    @GET("/schedules/{scheduleId}")
    suspend fun getStudiesForSchedule(@Path("scheduleId") scheduleId: Int): StudiesResponse
}