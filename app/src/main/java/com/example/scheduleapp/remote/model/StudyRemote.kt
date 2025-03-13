package com.example.scheduleapp.remote.model

data class StudyRemote(
    val id: String,
    val subject: SubjectRemote,
    val day: String,
    val number: String,
    val time: String,
    val type: String,
    val auditorium: String,
    val teacher: TeacherRemote,
    val scheduleId: String
)
