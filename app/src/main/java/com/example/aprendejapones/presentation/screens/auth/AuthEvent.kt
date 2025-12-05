package com.example.aprendejapones.presentation.screens.auth

/**
 * Eventos para las pantallas de autenticación.
 *
 * Representa todas las acciones del usuario que el [AuthViewModel]
 * debe procesar, siguiendo el patrón MVI (Model-View-Intent).
 *
 * ## Categorías de Eventos
 * - **Actualización de campos:** Cambios en los campos del formulario
 * - **Visibilidad:** Toggle de visibilidad de contraseñas
 * - **Acciones de auth:** Login, registro, Google Sign-In, logout
 * - **Manejo de errores:** Descartar errores, limpiar formulario
 *
 * ## Uso
 *
 * ```kotlin
 * // En Composable
 * TextField(
 *     value = state.email,
 *     onValueChange = { viewModel.onEvent(AuthEvent.EmailChanged(it)) }
 * )
 *
 * Button(onClick = { viewModel.onEvent(AuthEvent.Login) }) {
 *     Text("Iniciar Sesión")
 * }
 * ```
 *
 * @see AuthViewModel Procesa estos eventos.
 * @see AuthState Estado modificado por estos eventos.
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
sealed class AuthEvent {
    // ============ Field Updates ============

    /**
     * Evento cuando cambia el campo de email.
     * @property email Nuevo valor del email.
     */
    data class EmailChanged(val email: String) : AuthEvent()

    /**
     * Evento cuando cambia el campo de contraseña.
     * @property password Nuevo valor de la contraseña.
     */
    data class PasswordChanged(val password: String) : AuthEvent()

    /**
     * Evento cuando cambia el campo de nombre de usuario.
     * @property username Nuevo valor del nombre de usuario.
     */
    data class UsernameChanged(val username: String) : AuthEvent()

    /**
     * Evento cuando cambia el campo de confirmación de contraseña.
     * @property confirmPassword Nuevo valor de la confirmación.
     */
    data class ConfirmPasswordChanged(val confirmPassword: String) : AuthEvent()
    
    // ============ Visibility Toggles ============

    /** Evento para alternar la visibilidad de la contraseña. */
    object TogglePasswordVisibility : AuthEvent()

    /** Evento para alternar la visibilidad de la confirmación de contraseña. */
    object ToggleConfirmPasswordVisibility : AuthEvent()
    
    // ============ Auth Actions ============

    /** Evento para iniciar sesión con email y contraseña. */
    object Login : AuthEvent()

    /** Evento para registrar un nuevo usuario. */
    object Register : AuthEvent()

    /**
     * Evento para iniciar sesión con Google.
     * @property idToken Token de ID obtenido de Google Sign-In.
     */
    data class GoogleSignIn(val idToken: String) : AuthEvent()

    /** Evento para cerrar sesión. */
    object Logout : AuthEvent()
    
    // ============ Error Handling ============

    /** Evento para descartar el mensaje de error actual. */
    object DismissError : AuthEvent()

    /** Evento para limpiar todos los campos del formulario. */
    object ClearForm : AuthEvent()
}
