package com.example.aprendejapones.presentation.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aprendejapones.data.local.preferences.PreferencesManager
import com.example.aprendejapones.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<AuthEffect>()
    val effects: SharedFlow<AuthEffect> = _effects.asSharedFlow()

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.EmailChanged -> updateEmail(event.email)
            is AuthEvent.PasswordChanged -> updatePassword(event.password)
            is AuthEvent.UsernameChanged -> updateUsername(event.username)
            is AuthEvent.ConfirmPasswordChanged -> updateConfirmPassword(event.confirmPassword)
            is AuthEvent.TogglePasswordVisibility -> togglePasswordVisibility()
            is AuthEvent.ToggleConfirmPasswordVisibility -> toggleConfirmPasswordVisibility()
            is AuthEvent.Login -> login()
            is AuthEvent.Register -> register()
            is AuthEvent.GoogleSignIn -> googleSignIn(event.idToken)
            is AuthEvent.Logout -> logout()
            is AuthEvent.DismissError -> dismissError()
            is AuthEvent.ClearForm -> clearForm()
        }
    }

    private fun updateEmail(email: String) {
        _state.update { it.copy(email = email) }
    }

    private fun updatePassword(password: String) {
        _state.update { it.copy(password = password) }
    }

    private fun updateUsername(username: String) {
        _state.update { it.copy(username = username) }
    }

    private fun updateConfirmPassword(confirmPassword: String) {
        _state.update { it.copy(confirmPassword = confirmPassword) }
    }

    private fun togglePasswordVisibility() {
        _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    private fun toggleConfirmPasswordVisibility() {
        _state.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
    }

    private fun login() {
        if (!_state.value.isLoginValid) {
            _state.update { it.copy(errorMessage = "Por favor, completa todos los campos") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            
            val result = authRepository.loginWithEmail(
                email = _state.value.email,
                password = _state.value.password
            )

            result.fold(
                onSuccess = {
                    _state.update { it.copy(isLoading = false) }
                    _effects.emit(AuthEffect.LoginSuccess)
                },
                onFailure = { error ->
                    _state.update { 
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Error al iniciar sesión"
                        )
                    }
                    _effects.emit(AuthEffect.ShowError(error.message ?: "Error al iniciar sesión"))
                }
            )
        }
    }

    private fun register() {
        if (!_state.value.isRegisterValid) {
            val errorMessage = when {
                _state.value.username.isBlank() -> "Por favor, introduce un nombre de usuario"
                _state.value.email.isBlank() -> "Por favor, introduce un email"
                _state.value.password.isBlank() -> "Por favor, introduce una contraseña"
                _state.value.password.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
                _state.value.password != _state.value.confirmPassword -> "Las contraseñas no coinciden"
                else -> "Por favor, completa todos los campos correctamente"
            }
            _state.update { it.copy(errorMessage = errorMessage) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            
            val result = authRepository.registerWithEmail(
                email = _state.value.email,
                password = _state.value.password,
                username = _state.value.username
            )

            result.fold(
                onSuccess = {
                    _state.update { it.copy(isLoading = false) }
                    preferencesManager.saveUserName(_state.value.username)
                    _effects.emit(AuthEffect.RegisterSuccess)
                },
                onFailure = { error ->
                    _state.update { 
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Error al registrarse"
                        )
                    }
                    _effects.emit(AuthEffect.ShowError(error.message ?: "Error al registrarse"))
                }
            )
        }
    }

    private fun googleSignIn(idToken: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            
            val result = authRepository.loginWithGoogle(idToken)

            result.fold(
                onSuccess = {
                    _state.update { it.copy(isLoading = false) }
                    _effects.emit(AuthEffect.LoginSuccess)
                },
                onFailure = { error ->
                    _state.update { 
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Error con Google Sign In"
                        )
                    }
                    _effects.emit(AuthEffect.ShowError(error.message ?: "Error con Google Sign In"))
                }
            )
        }
    }

    private fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            clearForm()
            _effects.emit(AuthEffect.LogoutSuccess)
        }
    }

    private fun dismissError() {
        _state.update { it.copy(errorMessage = null) }
    }

    private fun clearForm() {
        _state.update { 
            AuthState()
        }
    }
}
