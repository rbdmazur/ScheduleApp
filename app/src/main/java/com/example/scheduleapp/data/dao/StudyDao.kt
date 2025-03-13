package com.example.scheduleapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.scheduleapp.data.model.Study
import com.example.scheduleapp.data.model.StudyWithTeacherAndSubject

@Dao
interface StudyDao {
    @Query("SELECT * FROM study")
    suspend fun getAllStudies(): List<Study>

    @Query("SELECT * FROM study WHERE id=(:id)")
    suspend fun getStudyById(id: Int): Study?

    @Insert
    suspend fun insertStudy(study: Study)

    @Delete
    suspend fun deleteStudy(study: Study)

    @Transaction
    @Query("SELECT * FROM study WHERE scheduleId=(:scheduleId)")
    suspend fun getStudiesWithSubjectAndTeachersForSchedule(scheduleId: Int): List<StudyWithTeacherAndSubject>
}