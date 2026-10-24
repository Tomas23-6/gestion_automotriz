package com.example.gestion_automotriz.ui.theme.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.gestion_automotriz.model.LoginCredentials
import com.example.gestion_automotriz.repository.AuthRepository

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class LoginViewModel(
    private val authRepository: AuthRepository = AuthRepository()
) : ViewModel() {

    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onUsernameChange(username: String) {
        uiState = uiState.copy(username = username, error = null)
    }

    fun onPasswordChange(password: String) {
        uiState = uiState.copy(password = password, error = null)
    }

    fun submit(onSuccess: (String) -> Unit) {
        if (uiState.username.isBlank() || uiState.password.isBlank()) {
            uiState = uiState.copy(error = "Ingrese usuario y PIN de acceso")
            return
        }

        uiState = uiState.copy(isLoading = true, error = null)

        val result = authRepository.login(LoginCredentials(uiState.username, uiState.password))
        result.onSuccess { session ->
            uiState = uiState.copy(isLoading = false)
            onSuccess(session.username)
        }.onFailure { err ->
            uiState = uiState.copy(
                isLoading = false,
                error = err.message ?: "Error de autenticación"
            )
        }
    }
}