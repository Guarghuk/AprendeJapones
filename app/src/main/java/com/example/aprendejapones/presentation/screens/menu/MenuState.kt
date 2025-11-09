package com.example.aprendejapones.presentation.screens.menu

/**
 * Estado de MenuScreen
 */
data class MenuState(
    val isDarkMode: Boolean = false,
    val isSoundEnabled: Boolean = true,
    val appLanguage: String = "Español",
    val dailyGoalMinutes: Int = 15,
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * Eventos de Menu
 */
sealed class MenuEvent {
    data class NavigateToScreen(val screen: String) : MenuEvent()
    data class ToggleDarkMode(val enabled: Boolean) : MenuEvent()
    data class ToggleSound(val enabled: Boolean) : MenuEvent()
    object Logout : MenuEvent()
    object DismissError : MenuEvent()
}

/**
 * Efectos secundarios
 */
sealed class MenuEffect {
    data class NavigateToScreen(val screen: String) : MenuEffect()
    object NavigateToLogin : MenuEffect()
    data class ShowToast(val message: String) : MenuEffect()
}