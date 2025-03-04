package com.example.scheduleapp.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scheduleapp.data.model.Student
import com.example.scheduleapp.data.model.User
import com.example.scheduleapp.data.services.UserService
import com.example.scheduleapp.remote.AuthApi
import com.example.scheduleapp.remote.ScheduleApiClient
import com.example.scheduleapp.remote.SessionManager
import com.example.scheduleapp.remote.model.AuthResponse
import com.example.scheduleapp.remote.model.StudentRemote
import com.example.scheduleapp.utils.LoadingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.util.UUID

class LoginViewModel : ViewModel() {

    private val _loginUiState = MutableStateFlow<LoadingState<UUID>>(LoadingState.Loading())
    val loginUiState = _loginUiState.asStateFlow()

    private val userService = UserService()
    private val apiClient = ScheduleApiClient()


    fun signIn(userData: UserData, context: Context, toMainScreen: (String) -> Unit) {
        val sessionManager = SessionManager(context)
        viewModelScope.launch {
            try {
                val response = AuthApi.authService.signIn(userData)
                sessionManager.saveAuthToken(response.token)

                var user = userService.getUserById(UUID.fromString(response.userId))
                if (user == null) {
                    user = getUserFromResponse(userData, response)
                    userService.insertUser(user)
                }

                var student = userService.getStudentById(user.id)
                if (student == null) {
                    try {
                        val studentRemote = apiClient.getApiService(context).getStudent(user.id)
                        student = studentResponseToStudentModel(studentRemote)
                        userService.insertStudent(student)
                    } catch (e: HttpException) {
                        _loginUiState.value = LoadingState.Failure(e.message())
                    }
                }
                _loginUiState.value = LoadingState.Success(UUID.fromString(response.userId))
                toMainScreen(response.userId)
            } catch (e: HttpException) {
                _loginUiState.value = LoadingState.Failure(e.message())
            }
        }
    }

    private fun getUserFromResponse(userData: UserData, authResponse: AuthResponse): User =
        User(id = UUID.fromString(authResponse.userId), email = userData.email)

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