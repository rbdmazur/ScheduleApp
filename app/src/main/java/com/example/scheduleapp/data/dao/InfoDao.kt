package com.example.scheduleapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.scheduleapp.data.model.Info

@Dao
interface InfoDao {
    @Query("SELECT * FROM info")
    suspend fun getAllInfo(): List<Info>

    @Query("SELECT * FROM info WHERE id=(:id)")
    suspend fun getInfoById(id: Int): Info?

    @Insert
    suspend fun insertInfo(info: Info)

    @Delete
    suspend fun deleteInfo(info: Info)
}