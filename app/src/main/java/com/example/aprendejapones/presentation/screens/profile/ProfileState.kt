package com.example.aprendejapones.presentation.screens.profile

import com.example.aprendejapones.domain.model.FirestoreUser
import com.example.aprendejapones.utils.Achievement
import com.example.aprendejapones.utils.Activity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Estado de la pantalla Profile
 */
data class ProfileState(
    val user: FirestoreUser? = null,
    val stats: ProfileStats = ProfileStats(),
    val achievements: List<Achievement> = emptyList(),
    val recentActivity: List<Activity> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
) {
    private val dateFormat = SimpleDateFormat("MMMM yyyy", Locale("es", "ES"))

    val username: String
        get() = user?.username ?: "Usuario"

    val rank: String
        get() = user?.rank ?: "初心者"

    val level: Int
        get() = user?.level ?: 1

    val xpProgress: Float
        get() {
            val xp = user?.xp ?: 0
            // XP is stored as remaining XP within current level (0-99)
            // Each level requires 100 XP
            return if (XP_PER_LEVEL > 0) xp.toFloat() / XP_PER_LEVEL.toFloat() else 0f
        }

    val currentXP: Int
        get() = user?.xp ?: 0  // XP within current level (0-99)

    val maxXP: Int
        get() = XP_PER_LEVEL  // Each level requires 100 XP

    val memberSince: String
        get() = user?.createdAt?.let { dateFormat.format(Date(it)) } ?: "Enero 2025"
    
    val drops: Int
        get() = user?.drops ?: 0

    val unlockedAchievementsCount: Int
        get() = achievements.count { it.isUnlocked }

    val totalAchievements: Int
        get() = achievements.size

    companion object {
        private const val XP_PER_LEVEL = 100
    }
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