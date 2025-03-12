package com.example.scheduleapp.data.repository

import android.content.Context
import androidx.room.Room
import com.example.scheduleapp.data.model.Info
import com.example.scheduleapp.data.model.Schedule
import com.example.scheduleapp.data.model.Student
import com.example.scheduleapp.data.model.StudentToSchedule
import com.example.scheduleapp.data.model.StudentWithSchedule
import com.example.scheduleapp.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

private const val DATABASE_NAME = "app_database"

class AppRepository private constructor(context: Context) {

    private val database: AppDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    //user
    suspend fun getAllUsers(): List<User> = withContext(Dispatchers.IO) {
        database.getUserDao().getAllUsers()
    }
    suspend fun getUserById(id: UUID): User? = withContext(Dispatchers.IO) {
        database.getUserDao().getUserById(id)
    }
    suspend fun insertUser(user: User) = withContext(Dispatchers.IO) {
        database.getUserDao().insertUser(user)
    }
    suspend fun deleteUser(user: User) = withContext(Dispatchers.IO) {
        database.getUserDao().deleteUser(user)
    }
    //info
    suspend fun getAllInfo(): List<Info> = withContext(Dispatchers.IO) {
        database.getInfoDao().getAllInfo()
    }
    suspend fun getInfoById(id: Int): Info? = withContext(Dispatchers.IO) {
        database.getInfoDao().getInfoById(id)
    }
    suspend fun insertInfo(info: Info) = withContext(Dispatchers.IO) {
        database.getInfoDao().insertInfo(info)
    }
    suspend fun deleteInfo(info: Info) = withContext(Dispatchers.IO) {
        database.getInfoDao().deleteInfo(info)
    }
    //student
    suspend fun getAllStudents(): List<Student> = withContext(Dispatchers.IO) {
        database.getStudentDao().getAllStudents()
    }
    suspend fun getStudentById(id: UUID): Student? = withContext(Dispatchers.IO) {
        database.getStudentDao().getStudentById(id)
    }
    suspend fun insertStudent(student: Student) = withContext(Dispatchers.IO) {
        database.getStudentDao().insertStudent(student)
    }
    suspend fun deleteStudent(student: Student) = withContext(Dispatchers.IO) {
        database.getStudentDao().deleteStudent(student)
    }
    //schedule
    suspend fun getAllSchedules(): List<Schedule> = withContext(Dispatchers.IO) {
        database.getScheduleDao().getAllSchedules()
    }
    suspend fun getScheduleById(id: Int): Schedule? = withContext(Dispatchers.IO) {
        database.getScheduleDao().getScheduleById(id)
    }
    suspend fun insertSchedule(schedule: Schedule) = withContext(Dispatchers.IO) {
        database.getScheduleDao().insertSchedule(schedule)
    }
    suspend fun deleteSchedule(schedule: Schedule) = withContext(Dispatchers.IO) {
        database.getScheduleDao().deleteSchedule(schedule)
    }
    suspend fun addScheduleToStudent(studentToSchedule: StudentToSchedule) = withContext(Dispatchers.IO) {
        database.getScheduleDao().addScheduleToStudent(studentToSchedule)
    }
    suspend fun deleteScheduleFromStudent(studentToSchedule: StudentToSchedule) = withContext(Dispatchers.IO) {
        database.getScheduleDao().deleteScheduleFromStudent(studentToSchedule)
    }
    suspend fun getSchedulesForStudent(id: UUID): StudentWithSchedule? = withContext(Dispatchers.IO) {
        database.getScheduleDao().getSchedulesForStudent(id)
    }
    suspend fun getStudentToScheduleByStudent(studentId: UUID): List<StudentToSchedule> = withContext(Dispatchers.IO) {
        database.getScheduleDao().getStudentToScheduleByStudent(studentId)
    }

    companion object {
        private var INSTANCE: AppRepository? = null

        fun init(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = AppRepository(context)
            }
        }

        fun get(): AppRepository {
            return INSTANCE ?:
            throw IllegalStateException("AppRepository is not initialized!")
        }
    }
}