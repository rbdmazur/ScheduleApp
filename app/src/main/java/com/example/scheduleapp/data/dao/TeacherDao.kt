package com.example.scheduleapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.scheduleapp.data.model.Teacher
import java.util.UUID

@Dao
interface TeacherDao {
    @Query("SELECT * FROM teacher")
    suspend fun getAllTeachers(): List<Teacher>

    @Query("SELECT * FROM teacher WHERE userId=(:id)")
    suspend fun getTeacherById(id: UUID): Teacher?

    @Insert
    suspend fun insertTeacher(teacher: Teacher)

    @Delete
    suspend fun deleteTeacher(teacher: Teacher)
}