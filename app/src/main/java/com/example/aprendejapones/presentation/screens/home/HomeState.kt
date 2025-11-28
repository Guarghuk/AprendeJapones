package com.example.aprendejapones.presentation.screens.home

import com.example.aprendejapones.domain.model.DailyChallenge
import com.example.aprendejapones.domain.model.KitsuneMessage
import com.example.aprendejapones.domain.model.LessonFunction
import com.example.aprendejapones.domain.model.User

/**
 * Estado de la pantalla Home
 * Representa todo el estado UI necesario para renderizar la pantalla
 */
data class HomeState(
    val user: User? = null,
    val dailyChallenge: DailyChallenge? = null,
    val kitsuneMessage: KitsuneMessage? = null,
    val lessonFunctions: List<LessonFunction> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
) {
    // Computed properties para facilitar el acceso desde la UI
    val userRank: String
        get() = user?.rank ?: "初心者"

    val streak: Int
        get() = user?.streak ?: 0

    val drops: Int
        get() = user?.drops ?: 0
    
    val username: String
        get() = user?.username ?: "Usuario"
    
    val avatarLetter: String
        get() = user?.avatarLetter ?: "K"
    
    val level: Int
        get() = user?.level ?: 1
    
    val currentXP: Int
        get() = user?.currentXP ?: 0
    
    val maxXP: Int
        get() = user?.maxXP ?: 100

    val hasNotifications: Boolean
        get() = false // TODO: Implementar lógica de notificaciones
}

/**
 * Eventos que pueden ocurrir en la pantalla Home
 * Representa todas las acciones del usuario
 */
sealed class HomeEvent {
    object LoadData : HomeEvent()
    object RefreshData : HomeEvent()
    data class SelectFunction(val functionName: String) : HomeEvent()
    object DismissError : HomeEvent()
    object MarkChallengeProgress : HomeEvent()
}

/**
 * Efectos secundarios (one-time events)
 * Para navegación u otras acciones que no forman parte del estado
 */
sealed class HomeEffect {
    data class NavigateToLesson(val functionName: String) : HomeEffect()
    data class ShowToast(val message: String) : HomeEffect()
}