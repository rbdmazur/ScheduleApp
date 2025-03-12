package com.example.scheduleapp.mainscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scheduleapp.data.model.Student
import com.example.scheduleapp.data.services.UserService
import com.example.scheduleapp.remote.ScheduleApiService
import com.example.scheduleapp.remote.model.StudentRemote
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

@HiltViewModel(assistedFactory = MainViewModel.Factory::class)
class MainViewModel @AssistedInject constructor(
    private val apiService: ScheduleApiService,
    @Assisted val id: UUID
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(id: UUID): MainViewModel
    }

    private val _mainUiState = MutableStateFlow(MainUiState())
    val mainUiState = _mainUiState.asStateFlow()

    private val userService = UserService()

    init {
        viewModelScope.launch {
            _mainUiState.value = _mainUiState.value.copy(isLoading = true)
            var student = userService.getStudentById(id)
            if (student == null) {
                try {
                    val studentResponse = apiService.getStudent(id)
                    student = studentResponseToStudentModel(studentResponse)
                    userService.insertStudent(student)
                } catch(e: Exception) {
                    Log.d("MAINSCREEN", e.message.toString())
                }
            }
            _mainUiState.value = _mainUiState.value.copy(isLoading = false, currentStudent = student)
        }
    }

    private fun studentResponseToStudentModel(studentRemote: StudentRemote): Student =
        Student(
            userId = UUID.fromString(studentRemote.userId),
            name = studentRemote.name,
            facultyId = studentRemote.info.facultyId.toInt(),
            specialization = studentRemote.info.specialization,
            course = studentRemote.info.course.toInt(),
            group = studentRemote.info.group.toInt()
        )
}