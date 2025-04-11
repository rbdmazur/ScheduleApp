package com.example.scheduleapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.scheduleapp.data.model.Schedule
import com.example.scheduleapp.data.model.StudentToSchedule
import com.example.scheduleapp.data.model.StudentWithSchedule
import java.util.UUID

@Dao
interface ScheduleDao {

    @Query("SELECT * FROM schedule")
    suspend fun getAllSchedules(): List<Schedule>

    @Query("SELECT * FROM schedule WHERE scheduleId=(:id)")
    suspend fun getScheduleById(id: Int): Schedule?

    @Insert
    suspend fun insertSchedule(schedule: Schedule)

    @Delete
    suspend fun deleteSchedule(schedule: Schedule)

    @Insert
    suspend fun addScheduleToStudent(studentToSchedule: StudentToSchedule)

    @Delete
    suspend fun deleteScheduleFromStudent(studentToSchedule: StudentToSchedule)

    @Query("SELECT * FROM studenttoschedule WHERE userId=(:studentId)")
    suspend fun getStudentToScheduleByStudent(studentId: UUID): List<StudentToSchedule>

    @Transaction
    @Query("SELECT * FROM student WHERE userId=(:id)")
    fun getSchedulesForStudent(id: UUID): StudentWithSchedule?
}