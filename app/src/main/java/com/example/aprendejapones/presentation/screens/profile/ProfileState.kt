package com.example.aprendejapones.presentation.screens.profile

import com.example.aprendejapones.domain.model.User
import com.example.aprendejapones.utils.Achievement
import com.example.aprendejapones.utils.Activity

/**
 * Estado de la pantalla Profile
 */
data class ProfileState(
    val user: User? = null,
    val stats: ProfileStats = ProfileStats(),
    val achievements: List<Achievement> = emptyList(),
    val recentActivity: List<Activity> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
) {
    val username: String
        get() = user?.username ?: "Usuario"

    val rank: String
        get() = user?.rank ?: "初心者"

    val level: Int
        get() = user?.level ?: 1

    val xpProgress: Float
        get() = user?.xpProgress ?: 0f

    val currentXP: Int
        get() = user?.currentXP ?: 0

    val maxXP: Int
        get() = user?.maxXP ?: 100

    val memberSince: String
        get() = user?.memberSince ?: "Enero 2025"
    
    val drops: Int
        get() = user?.drops ?: 0

    val unlockedAchievementsCount: Int
        get() = achievements.count { it.isUnlocked }

    val totalAchievements: Int
        get() = achievements.size
}

/**
 * Estadísticas del perfil
 */
data class ProfileStats(
    val streak: Int = 0,
    val lessonsCompleted: Int = 0,
    val totalTimeHours: String = "0h"
)

/**
 * Eventos de la pantalla Profile
 */
sealed class ProfileEvent {
    object LoadData : ProfileEvent()
    object RefreshData : ProfileEvent()
    object NavigateToAchievements : ProfileEvent()
    object NavigateToStats : ProfileEvent()
    object DismissError : ProfileEvent()
}

/**
 * Efectos secundarios
 */
sealed class ProfileEffect {
    object NavigateToAchievements : ProfileEffect()
    object NavigateToStats : ProfileEffect()
    data class ShowToast(val message: String) : ProfileEffect()
}