package com.example.scheduleapp.data.services

import com.example.scheduleapp.data.model.Schedule
import com.example.scheduleapp.data.model.StudentToSchedule
import com.example.scheduleapp.data.model.StudentWithSchedule
import com.example.scheduleapp.data.repository.AppRepository
import java.util.UUID

class ScheduleService {
    private val repository = AppRepository.get()

    suspend fun getAllSchedules(): List<Schedule> =
        repository.getAllSchedules()

    suspend fun getScheduleById(id: Int): Schedule? =
        repository.getScheduleById(id)

    suspend fun insertSchedule(schedule: Schedule) {
        repository.insertSchedule(schedule)
    }

    suspend fun deleteSchedule(schedule: Schedule) {
        repository.deleteSchedule(schedule)
    }

    suspend fun addScheduleToStudent(studentToSchedule: StudentToSchedule) {
        repository.addScheduleToStudent(studentToSchedule)
    }

    suspend fun deleteScheduleFromStudent(studentToSchedule: StudentToSchedule) {
        repository.deleteScheduleFromStudent(studentToSchedule)
    }

    suspend fun getSchedulesForStudent(id: UUID): StudentWithSchedule? =
        repository.getSchedulesForStudent(id)
}