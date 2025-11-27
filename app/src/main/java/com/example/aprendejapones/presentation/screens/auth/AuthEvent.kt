package com.example.aprendejapones.presentation.screens.auth

/**
 * Events for authentication screens
 */
sealed class AuthEvent {
    // Field updates
    data class EmailChanged(val email: String) : AuthEvent()
    data class PasswordChanged(val password: String) : AuthEvent()
    data class UsernameChanged(val username: String) : AuthEvent()
    data class ConfirmPasswordChanged(val confirmPassword: String) : AuthEvent()
    
    // Visibility toggles
    object TogglePasswordVisibility : AuthEvent()
    object ToggleConfirmPasswordVisibility : AuthEvent()
    
    // Auth actions
    object Login : AuthEvent()
    object Register : AuthEvent()
    data class GoogleSignIn(val idToken: String) : AuthEvent()
    object Logout : AuthEvent()
    
    // Error handling
    object DismissError : AuthEvent()
    object ClearForm : AuthEvent()
}
