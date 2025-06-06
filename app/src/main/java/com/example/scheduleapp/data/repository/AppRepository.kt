package com.example.scheduleapp.data.repository

import android.content.Context
import androidx.room.Room
import com.example.scheduleapp.data.model.Info
import com.example.scheduleapp.data.model.Notification
import com.example.scheduleapp.data.model.NotificationData
import com.example.scheduleapp.data.model.Schedule
import com.example.scheduleapp.data.model.Student
import com.example.scheduleapp.data.model.StudentToSchedule
import com.example.scheduleapp.data.model.StudentWithEmailAndInfo
import com.example.scheduleapp.data.model.StudentWithSchedule
import com.example.scheduleapp.data.model.Study
import com.example.scheduleapp.data.model.StudyWithTeacherAndSubject
import com.example.scheduleapp.data.model.Subject
import com.example.scheduleapp.data.model.Teacher
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
    suspend fun getStudentWithInfo(userId: UUID): StudentWithEmailAndInfo? = withContext(Dispatchers.IO) {
        database.getStudentDao().getStudentWithInfo(userId)
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
    //teacher
    suspend fun getAllTeachers(): List<Teacher> = withContext(Dispatchers.IO) {
        database.getTeacherDao().getAllTeachers()
    }
    suspend fun getTeacherById(id: UUID): Teacher? = withContext(Dispatchers.IO) {
        database.getTeacherDao().getTeacherById(id)
    }
    suspend fun insertTeacher(teacher: Teacher) = withContext(Dispatchers.IO) {
        database.getTeacherDao().insertTeacher(teacher)
    }
    suspend fun deleteTeacher(teacher: Teacher) = withContext(Dispatchers.IO) {
        database.getTeacherDao().deleteTeacher(teacher)
    }
    //subject
    suspend fun getAllSubjects(): List<Subject> = withContext(Dispatchers.IO) {
        database.getSubjectDao().getAllSubjects()
    }
    suspend fun getSubjectById(id: Int): Subject? = withContext(Dispatchers.IO) {
        database.getSubjectDao().getSubjectById(id)
    }
    suspend fun insertSubject(subject: Subject) = withContext(Dispatchers.IO) {
        database.getSubjectDao().insertSubject(subject)
    }
    suspend fun deleteSubject(subject: Subject) = withContext(Dispatchers.IO) {
        database.getSubjectDao().deleteSubject(subject)
    }
    suspend fun getAllSubjectsByInfoId(infoId: Int): List<Subject> = withContext(Dispatchers.IO) {
        database.getSubjectDao().getAllSubjectsByInfoId(infoId)
    }
    //study
    suspend fun getAllStudies(): List<Study> = withContext(Dispatchers.IO) {
        database.getStudyDao().getAllStudies()
    }
    suspend fun getStudyById(id: Int): Study? = withContext(Dispatchers.IO) {
        database.getStudyDao().getStudyById(id)
    }
    suspend fun insertStudy(study: Study) = withContext(Dispatchers.IO) {
        database.getStudyDao().insertStudy(study)
    }
    suspend fun deleteStudy(study: Study) = withContext(Dispatchers.IO) {
        database.getStudyDao().deleteStudy(study)
    }
    suspend fun getStudiesWithSubjectAndTeacherForSchedule(scheduleId: Int): List<StudyWithTeacherAndSubject> =
        withContext(Dispatchers.IO) {
            database.getStudyDao().getStudiesWithSubjectAndTeachersForSchedule(scheduleId)
        }

    //notification
    suspend fun getAllNotifications(userId: UUID): List<Notification> = withContext(Dispatchers.IO) {
        database.getNotificationDao().getAllNotifications(userId)
    }
    suspend fun getNotificationById(id: Int): Notification? = withContext(Dispatchers.IO) {
        database.getNotificationDao().getNotificationById(id)
    }
    suspend fun insertNotification(notificationData: NotificationData) = withContext(Dispatchers.IO) {
        database.getNotificationDao().insertNotification(notificationData)
    }
    suspend fun deleteNotification(notification: Notification) = withContext(Dispatchers.IO) {
        database.getNotificationDao().deleteNotification(notification)
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