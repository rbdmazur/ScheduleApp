package com.example.scheduleapp.data.services

import com.example.scheduleapp.data.model.Student
import com.example.scheduleapp.data.model.User
import com.example.scheduleapp.data.repository.AppRepository
import java.util.UUID

class UserService {
    private val repository = AppRepository.get()

    suspend fun getAllUsers(): List<User> = repository.getAllUsers()
    suspend fun getUserById(id: UUID): User? = repository.getUserById(id)
    suspend fun insertUser(user: User) {
        repository.insertUser(user)
    }
    suspend fun deleteUser(user: User) {
        repository.deleteUser(user)
    }

    suspend fun getAllStudents(): List<Student> = repository.getAllStudents()
    suspend fun getStudentById(id: UUID): Student? = repository.getStudentById(id)
    suspend fun insertStudent(student: Student) {
        repository.insertStudent(student)
    }
    suspend fun deleteStudent(student: Student) {
        repository.deleteStudent(student)
    }
}