package com.example.aprendejapones.presentation.screens.auth

/**
 * State for authentication screens (Login/Register)
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
    val isLoginValid: Boolean
        get() = email.isNotBlank() && password.isNotBlank()

    val isRegisterValid: Boolean
        get() = email.isNotBlank() && 
                password.isNotBlank() && 
                username.isNotBlank() && 
                password == confirmPassword &&
                password.length >= 6
    
    val passwordError: String?
        get() = when {
            password.isNotBlank() && password.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            else -> null
        }

    val confirmPasswordError: String?
        get() = when {
            confirmPassword.isNotBlank() && password != confirmPassword -> "Las contraseñas no coinciden"
            else -> null
        }
}
