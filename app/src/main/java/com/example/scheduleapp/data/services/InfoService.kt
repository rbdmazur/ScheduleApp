package com.example.scheduleapp.data.services

import com.example.scheduleapp.data.model.Info
import com.example.scheduleapp.data.repository.AppRepository

class InfoService {
    private val repository = AppRepository.get()

    suspend fun getAllInfo(): List<Info> =
        repository.getAllInfo()

    suspend fun getInfoById(id: Int): Info? =
        repository.getInfoById(id)

    suspend fun insertInfo(info: Info) {
        repository.insertInfo(info)
    }

    suspend fun deleteInfo(info: Info) {
        repository.deleteInfo(info)
    }
}