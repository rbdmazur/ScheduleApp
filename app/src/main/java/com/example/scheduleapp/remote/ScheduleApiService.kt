package com.example.scheduleapp.remote

import com.example.scheduleapp.remote.model.FacultyResponse
import com.example.scheduleapp.remote.model.ScheduleResponse
import com.example.scheduleapp.remote.model.ScheduleSimpleResponse
import com.example.scheduleapp.remote.model.StudentRemote
import com.example.scheduleapp.remote.model.StudiesResponse
import com.example.scheduleapp.remote.requests.ScheduleRequestList
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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

    @GET("/all-schedules/{userId}")
    suspend fun getAvailableSchedules(
        @Path("userId")id: UUID,
        @Query("facultyId")facultyId: Int?,
        @Query("course")course: Int?,
        @Query("group")group: Int?
    ): ScheduleSimpleResponse

    @POST("/all-schedules/{userId}")
    suspend fun addSchedulesToStudent(
        @Path("userId")id: UUID,
        @Body schedules: ScheduleRequestList
    )

    @GET("/faculties")
    suspend fun getFaculties(): FacultyResponse
}