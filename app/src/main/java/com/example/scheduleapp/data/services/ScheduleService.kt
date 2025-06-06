package com.example.scheduleapp.data.services

import com.example.scheduleapp.data.model.Schedule
import com.example.scheduleapp.data.model.StudentToSchedule
import com.example.scheduleapp.data.model.StudentWithSchedule
import com.example.scheduleapp.data.model.Study
import com.example.scheduleapp.data.model.StudyWithTeacherAndSubject
import com.example.scheduleapp.data.model.Subject
import com.example.scheduleapp.data.repository.AppRepository
import java.util.UUID

class ScheduleService {
    private val repository = AppRepository.get()

    suspend fun getAllSchedules(): List<Schedule> =
        repository.getAllSchedules()
    suspend fun getScheduleById(id: Int): Schedule? =
        repository.getScheduleById(id)
    suspend fun insertSchedule(schedule: Schedule) {
        repository.insertSchedule(schedule)
    }
    suspend fun deleteSchedule(schedule: Schedule) {
        repository.deleteSchedule(schedule)
    }
    suspend fun getStudentToScheduleByStudent(studentId: UUID): List<StudentToSchedule> =
        repository.getStudentToScheduleByStudent(studentId)
    suspend fun addScheduleToStudent(studentToSchedule: StudentToSchedule) {
        repository.addScheduleToStudent(studentToSchedule)
    }
    suspend fun deleteScheduleFromStudent(studentToSchedule: StudentToSchedule) {
        repository.deleteScheduleFromStudent(studentToSchedule)
    }
    suspend fun getSchedulesForStudent(id: UUID): StudentWithSchedule? =
        repository.getSchedulesForStudent(id)

    //subject
    suspend fun getAllSubjects(): List<Subject> =
        repository.getAllSubjects()
    suspend fun getSubjectById(id: Int): Subject? =
        repository.getSubjectById(id)
    suspend fun insertSubject(subject: Subject) {
        repository.insertSubject(subject)
    }
    suspend fun deleteSubject(subject: Subject) {
        repository.deleteSubject(subject)
    }
    suspend fun getAllSubjectsByInfoId(infoId: Int): List<Subject> =
        repository.getAllSubjectsByInfoId(infoId)
    //study
    suspend fun getAllStudies(): List<Study> =
        repository.getAllStudies()
    suspend fun getStudyById(id: Int): Study? =
        repository.getStudyById(id)
    suspend fun insertStudy(study: Study) {
        repository.insertStudy(study)
    }
    suspend fun deleteStudy(study: Study) {
        repository.deleteStudy(study)
    }
    suspend fun getStudiesWithSubjectAndTeacherForSchedule(scheduleId: Int): List<StudyWithTeacherAndSubject> =
        repository.getStudiesWithSubjectAndTeacherForSchedule(scheduleId)
}