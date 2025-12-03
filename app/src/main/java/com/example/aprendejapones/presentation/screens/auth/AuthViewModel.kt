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

/**
 * ViewModel para las pantallas de autenticación (Login y Register).
 *
 * Gestiona el estado y la lógica de negocio para el flujo de autenticación
 * de usuarios, incluyendo login con email/contraseña, registro de nuevos
 * usuarios y autenticación con Google.
 *
 * ## Arquitectura MVI
 * - **Model:** [AuthState] representa el estado del formulario
 * - **View:** Composables (LoginScreen, RegisterScreen)
 * - **Intent:** [AuthEvent] representa las acciones del usuario
 *
 * ## Flujos
 * - [state]: Estado observable del formulario
 * - [effects]: Eventos de una sola vez (navegación, toasts)
 *
 * ## Funcionalidades
 * - Login con email y contraseña
 * - Registro con email, contraseña y nombre de usuario
 * - Login con Google Sign-In
 * - Logout
 * - Validación de formulario en tiempo real
 *
 * ## Uso
 *
 * ```kotlin
 * @Composable
 * fun LoginScreen(viewModel: AuthViewModel = hiltViewModel()) {
 *     val state by viewModel.state.collectAsState()
 *
 *     LaunchedEffect(Unit) {
 *         viewModel.effects.collect { effect ->
 *             when (effect) {
 *                 is AuthEffect.LoginSuccess -> navigateToHome()
 *                 is AuthEffect.ShowError -> showError(effect.message)
 *             }
 *         }
 *     }
 *
 *     TextField(
 *         value = state.email,
 *         onValueChange = { viewModel.onEvent(AuthEvent.EmailChanged(it)) }
 *     )
 *     // ...
 * }
 * ```
 *
 * @property authRepository Repositorio para operaciones de autenticación.
 * @property preferencesManager Manager para guardar preferencias del usuario.
 *
 * @see AuthState Estado del formulario de autenticación.
 * @see AuthEvent Eventos del usuario.
 * @see AuthEffect Efectos secundarios (navegación, mensajes).
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    /** Estado mutable interno del formulario */
    private val _state = MutableStateFlow(AuthState())

    /** Estado público e inmutable para la UI */
    val state: StateFlow<AuthState> = _state.asStateFlow()

    /** Flujo mutable de efectos secundarios */
    private val _effects = MutableSharedFlow<AuthEffect>()

    /** Efectos públicos para eventos de una sola vez */
    val effects: SharedFlow<AuthEffect> = _effects.asSharedFlow()

    /**
     * Procesa los eventos de la UI.
     *
     * @param event El evento a procesar.
     */
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

    /**
     * Actualiza el campo de email en el estado.
     */
    private fun updateEmail(email: String) {
        _state.update { it.copy(email = email) }
    }

    /**
     * Actualiza el campo de contraseña en el estado.
     */
    private fun updatePassword(password: String) {
        _state.update { it.copy(password = password) }
    }

    /**
     * Actualiza el campo de nombre de usuario en el estado.
     */
    private fun updateUsername(username: String) {
        _state.update { it.copy(username = username) }
    }

    /**
     * Actualiza el campo de confirmación de contraseña.
     */
    private fun updateConfirmPassword(confirmPassword: String) {
        _state.update { it.copy(confirmPassword = confirmPassword) }
    }

    /**
     * Alterna la visibilidad de la contraseña.
     */
    private fun togglePasswordVisibility() {
        _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    /**
     * Alterna la visibilidad de la confirmación de contraseña.
     */
    private fun toggleConfirmPasswordVisibility() {
        _state.update { it.copy(isConfirmPasswordVisible = !it.isConfirmPasswordVisible) }
    }

    /**
     * Inicia sesión con email y contraseña.
     *
     * Valida el formulario antes de intentar el login.
     * En caso de éxito, emite [AuthEffect.LoginSuccess].
     * En caso de error, actualiza el estado con el mensaje de error.
     */
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

    /**
     * Registra un nuevo usuario con email, contraseña y nombre.
     *
     * Valida todos los campos del formulario antes de intentar el registro.
     * Proporciona mensajes de error específicos según el campo inválido.
     * En caso de éxito, guarda el nombre de usuario y emite [AuthEffect.RegisterSuccess].
     */
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

    /**
     * Inicia sesión con Google Sign-In.
     *
     * @param idToken Token de ID obtenido del proceso de Google Sign-In.
     */
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

    /**
     * Cierra la sesión del usuario actual.
     *
     * Limpia el formulario y emite [AuthEffect.LogoutSuccess].
     */
    private fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            clearForm()
            _effects.emit(AuthEffect.LogoutSuccess)
        }
    }

    /**
     * Descarta el mensaje de error actual.
     */
    private fun dismissError() {
        _state.update { it.copy(errorMessage = null) }
    }

    /**
     * Limpia todos los campos del formulario.
     *
     * Restablece el estado a valores por defecto.
     */
    private fun clearForm() {
        _state.update { 
            AuthState()
        }
    }
}
