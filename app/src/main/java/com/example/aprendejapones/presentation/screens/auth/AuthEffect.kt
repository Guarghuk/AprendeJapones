package com.example.aprendejapones.presentation.screens.auth

/**
 * Efectos secundarios para las pantallas de autenticación.
 *
 * Representa eventos de una sola vez que deben manejarse por la UI,
 * como navegación después de login exitoso o mostrar mensajes de error.
 *
 * ## Diferencia con AuthState
 * - **AuthState:** Estado persistente de la UI
 * - **AuthEffect:** Eventos transitorios que se consumen una vez
 *
 * ## Efectos Disponibles
 * - [LoginSuccess]: Login exitoso, navegar a Home
 * - [RegisterSuccess]: Registro exitoso, navegar a Home
 * - [LogoutSuccess]: Logout exitoso, navegar a Login
 * - [ShowError]: Mostrar error en UI
 * - [ShowToast]: Mostrar mensaje toast
 *
 * @see AuthViewModel Emite estos efectos.
 * @see AuthState Estado de la UI.
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
sealed class AuthEffect {
    /** Efecto emitido cuando el login es exitoso. Navegar a Home. */
    object LoginSuccess : AuthEffect()

    /** Efecto emitido cuando el registro es exitoso. Navegar a Home. */
    object RegisterSuccess : AuthEffect()

    /** Efecto emitido cuando el logout es exitoso. Navegar a Login. */
    object LogoutSuccess : AuthEffect()

    /**
     * Efecto para mostrar un error en la UI.
     * @property message Mensaje de error a mostrar.
     */
    data class ShowError(val message: String) : AuthEffect()

    /**
     * Efecto para mostrar un toast informativo.
     * @property message Mensaje del toast.
     */
    data class ShowToast(val message: String) : AuthEffect()
}
