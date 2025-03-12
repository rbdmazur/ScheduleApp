package com.example.scheduleapp.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scheduleapp.remote.auth.AuthRepository
import com.example.scheduleapp.remote.auth.AuthResult
import com.example.scheduleapp.remote.auth.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _loginUiState = MutableStateFlow<AuthResult<UUID>>(AuthResult.Unauthorized())
    val loginUiState = _loginUiState.asStateFlow()

    init {
        authenticate()
    }

    fun signIn(userData: UserData) {
        viewModelScope.launch {
            _loginUiState.value = repository.signIn(userData)
        }
    }

    private fun authenticate() {
        viewModelScope.launch {
            _loginUiState.value = repository.authenticate()
        }
    }
}