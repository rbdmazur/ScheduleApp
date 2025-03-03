package com.example.scheduleapp.data.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.scheduleapp.data.dao.StudentDao
import com.example.scheduleapp.data.dao.UserDao
import com.example.scheduleapp.data.model.User

@Database(
    version = 1,
    entities = [
        User::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getUserDao(): UserDao
    abstract fun getStudentDao(): StudentDao
}

