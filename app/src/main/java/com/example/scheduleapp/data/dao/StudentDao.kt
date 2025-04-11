package com.example.scheduleapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.scheduleapp.data.model.Student
import com.example.scheduleapp.data.model.StudentWithEmailAndInfo
import java.util.UUID

@Dao
interface StudentDao {
    @Query("SELECT * FROM student")
    suspend fun getAllStudents(): List<Student>

    @Query("SELECT * FROM student WHERE userId=(:id)")
    suspend fun getStudentById(id: UUID): Student?

    @Insert
    suspend fun insertStudent(student: Student)

    @Delete
    suspend fun deleteStudent(student: Student)

    @Transaction
    @Query("SELECT * FROM student WHERE userId=(:userId)")
    suspend fun getStudentWithInfo(userId: UUID): StudentWithEmailAndInfo?
}