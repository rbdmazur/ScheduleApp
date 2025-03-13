package com.example.scheduleapp.data.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.scheduleapp.data.dao.InfoDao
import com.example.scheduleapp.data.dao.ScheduleDao
import com.example.scheduleapp.data.dao.StudentDao
import com.example.scheduleapp.data.dao.StudyDao
import com.example.scheduleapp.data.dao.SubjectDao
import com.example.scheduleapp.data.dao.TeacherDao
import com.example.scheduleapp.data.dao.UserDao
import com.example.scheduleapp.data.model.Info
import com.example.scheduleapp.data.model.Schedule
import com.example.scheduleapp.data.model.Student
import com.example.scheduleapp.data.model.StudentToSchedule
import com.example.scheduleapp.data.model.Study
import com.example.scheduleapp.data.model.Subject
import com.example.scheduleapp.data.model.Teacher
import com.example.scheduleapp.data.model.User

@Database(
    version = 1,
    entities = [
        User::class,
        Student::class,
        Schedule::class,
        StudentToSchedule::class,
        Info::class,
        Teacher::class,
        Subject::class,
        Study::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getUserDao(): UserDao
    abstract fun getStudentDao(): StudentDao
    abstract fun getScheduleDao(): ScheduleDao
    abstract fun getInfoDao(): InfoDao
    abstract fun getTeacherDao(): TeacherDao
    abstract fun getSubjectDao(): SubjectDao
    abstract fun getStudyDao(): StudyDao
}

