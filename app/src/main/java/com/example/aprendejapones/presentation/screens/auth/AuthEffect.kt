package com.example.aprendejapones.presentation.screens.auth

/**
 * One-time effects for authentication screens
 */
sealed class AuthEffect {
    object LoginSuccess : AuthEffect()
    object RegisterSuccess : AuthEffect()
    object LogoutSuccess : AuthEffect()
    data class ShowError(val message: String) : AuthEffect()
    data class ShowToast(val message: String) : AuthEffect()
}
