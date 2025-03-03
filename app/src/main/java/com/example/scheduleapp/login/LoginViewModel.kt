package com.example.scheduleapp.login

import android.content.Context
import android.util.Log
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.util.UUID

class LoginViewModel : ViewModel() {

    private val _errorState = MutableStateFlow(LoginState())
    val errorState = _errorState.asStateFlow()

    private val userService = UserService()
    private val apiClient = ScheduleApiClient()


    fun signIn(userData: UserData, context: Context) {
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
                Log.d("NET", response.toString())
                _errorState.value = LoginState(false, "")
                var student = userService.getStudentById(user.id)
                if (student == null) {
                    try {
                        val studentResp = apiClient.getApiService(context).getStudent(user.id)
                        student = studentResponseToStudentModel(studentResp)
                        userService.insertStudent(student)
                        Log.d("NET", studentResp.toString())
                    } catch (e: HttpException) {
                        Log.e("NET", e.message())
                    }
                }
            } catch (e: HttpException) {
                _errorState.value = LoginState(true, e.message())
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