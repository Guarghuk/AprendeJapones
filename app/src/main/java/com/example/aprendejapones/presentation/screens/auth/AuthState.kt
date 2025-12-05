package com.example.aprendejapones.presentation.screens.auth

/**
 * Estado de las pantallas de autenticación (Login/Register).
 *
 * Contiene todos los datos necesarios para renderizar los formularios
 * de inicio de sesión y registro, incluyendo campos de texto, estados
 * de validación y mensajes de error.
 *
 * ## Campos del Formulario
 * - [email]: Correo electrónico del usuario
 * - [password]: Contraseña
 * - [username]: Nombre de usuario (solo registro)
 * - [confirmPassword]: Confirmación de contraseña (solo registro)
 *
 * ## Estados de UI
 * - [isLoading]: Indica operación en progreso
 * - [errorMessage]: Mensaje de error a mostrar
 * - [isPasswordVisible]: Control de visibilidad de contraseña
 *
 * ## Validación
 * Propiedades computadas que verifican la validez del formulario
 * y proporcionan mensajes de error específicos.
 *
 * @property email Correo electrónico ingresado.
 * @property password Contraseña ingresada.
 * @property username Nombre de usuario ingresado.
 * @property confirmPassword Confirmación de contraseña.
 * @property isLoading Si hay una operación de autenticación en progreso.
 * @property errorMessage Mensaje de error actual o null.
 * @property isPasswordVisible Si la contraseña debe mostrarse en texto plano.
 * @property isConfirmPasswordVisible Si la confirmación debe mostrarse en texto plano.
 *
 * @see AuthViewModel ViewModel que gestiona este estado.
 * @see AuthEvent Eventos que modifican este estado.
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
data class AuthState(
    val email: String = "",
    val password: String = "",
    val username: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false
) {
    /**
     * Indica si el formulario de login es válido.
     *
     * Requiere email y contraseña no vacíos.
     *
     * @return `true` si el formulario de login es válido.
     */
    val isLoginValid: Boolean
        get() = email.isNotBlank() && password.isNotBlank()

    /**
     * Indica si el formulario de registro es válido.
     *
     * Requisitos:
     * - Email no vacío
     * - Contraseña no vacía con mínimo 6 caracteres
     * - Nombre de usuario no vacío
     * - Contraseñas coincidentes
     *
     * @return `true` si el formulario de registro es válido.
     */
    val isRegisterValid: Boolean
        get() = email.isNotBlank() && 
                password.isNotBlank() && 
                username.isNotBlank() && 
                password == confirmPassword &&
                password.length >= 6
    
    /**
     * Mensaje de error para el campo de contraseña.
     *
     * @return Mensaje de error o null si no hay error.
     */
    val passwordError: String?
        get() = when {
            password.isNotBlank() && password.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            else -> null
        }

    /**
     * Mensaje de error para el campo de confirmación de contraseña.
     *
     * @return Mensaje de error o null si no hay error.
     */
    val confirmPasswordError: String?
        get() = when {
            confirmPassword.isNotBlank() && password != confirmPassword -> "Las contraseñas no coinciden"
            else -> null
        }
}
