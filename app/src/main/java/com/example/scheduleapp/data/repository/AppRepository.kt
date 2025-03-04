package com.example.scheduleapp.data.repository

import android.content.Context
import androidx.room.Room
import com.example.scheduleapp.data.model.Student
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