package com.example.aprendejapones.domain.model

/**
 * Modelo de dominio para el Usuario
 * Este modelo representa la entidad de negocio pura, sin dependencias de frameworks
 */
data class User(
    val id: String,
    val username: String,
    val rank: String,
    val level: Int,
    val currentXP: Int,
    val maxXP: Int,
    val streak: Int,
    val drops: Int,
    val memberSince: String,
    val avatarLetter: String = username.firstOrNull()?.toString() ?: "K"
) {
    val xpProgress: Float
        get() = if (maxXP > 0) currentXP.toFloat() / maxXP.toFloat() else 0f
}

/**
 * Modelo para el Desafío Diario
 */
data class DailyChallenge(
    val id: String,
    val completed: Int,
    val total: Int,
    val timeRemaining: String,
    val rewardXP: Int = 50,
    val rewardCoins: Int = 20,
    val difficulty: ChallengeDifficulty = ChallengeDifficulty.MEDIUM
) {
    val progress: Float
        get() = if (total > 0) completed.toFloat() / total.toFloat() else 0f

    val isCompleted: Boolean
        get() = completed >= total
}

/**
 * Dificultad del desafío diario
 */
enum class ChallengeDifficulty(val displayName: String, val coinReward: Int) {
    EASY("Fácil", 10),
    MEDIUM("Normal", 20),
    HARD("Difícil", 35),
    EXTREME("Extremo", 50)
}

/**
 * Modelo para las Funciones/Lecciones disponibles
 */
data class LessonFunction(
    val id: String,
    val icon: String,
    val name: String,
    val subtitle: String,
    val isLocked: Boolean = false,
    val progress: Int = 0
)

/**
 * Modelo para el mensaje de Kitsune
 */
data class KitsuneMessage(
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)