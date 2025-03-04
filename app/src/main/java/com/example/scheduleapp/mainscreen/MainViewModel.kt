package com.example.scheduleapp.mainscreen

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.scheduleapp.data.model.Student
import com.example.scheduleapp.data.services.UserService
import com.example.scheduleapp.remote.ScheduleApiClient
import com.example.scheduleapp.remote.model.StudentRemote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class MainViewModel(
    private val id: UUID
) : ViewModel() {
    private val _mainUiState = MutableStateFlow(MainUiState())
    val mainUiState = _mainUiState.asStateFlow()

    private val userService = UserService()
    private val apiClient = ScheduleApiClient()

    init {
        viewModelScope.launch {
            val currentStudent = userService.getStudentById(id)
            currentStudent?.let { _mainUiState.value = MainUiState(currentStudent = it) }
        }
    }

    fun updateTest(str: String) {
        _mainUiState.value = mainUiState.value.copy(test = str)
    }

    class MainViewModelFactory(private val id: UUID) :
            ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(id) as T
        }
            }
}