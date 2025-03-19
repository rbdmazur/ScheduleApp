package com.example.scheduleapp.remote.model

data class FacultyResponse(
    val faculties: List<FacultyRemote>
)


data class FacultyRemote(
    val id: String,
    val title: String,
    val fullTitle: String
)
