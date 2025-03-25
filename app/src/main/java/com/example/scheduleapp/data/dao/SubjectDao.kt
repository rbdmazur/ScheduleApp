package com.example.scheduleapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.scheduleapp.data.model.Subject

@Dao
interface SubjectDao {
    @Query("SELECT * FROM subject")
    suspend fun getAllSubjects(): List<Subject>

    @Query("SELECT * FROM subject WHERE id=(:id)")
    suspend fun getSubjectById(id: Int): Subject?

    @Insert
    suspend fun insertSubject(subject: Subject)

    @Delete
    suspend fun deleteSubject(subject: Subject)

    @Query("SELECT * FROM subject WHERE infoId=(:infoId)")
    suspend fun getAllSubjectsByInfoId(infoId: Int): List<Subject>
}