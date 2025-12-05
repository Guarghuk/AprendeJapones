package com.example.aprendejapones.domain.model

/**
 * Modelo de dominio que representa a un usuario de la aplicación Kotodama.
 *
 * Este modelo es una entidad de negocio pura, sin dependencias de frameworks externos,
 * siguiendo los principios de Clean Architecture. Contiene toda la información
 * relacionada con el perfil del usuario y su progreso en la aplicación.
 *
 * @property id Identificador único del usuario (UUID).
 * @property username Nombre de usuario para mostrar en la aplicación.
 * @property rank Rango actual del usuario en japonés (ej: "初心者" = Principiante).
 * @property level Nivel actual del usuario basado en la experiencia acumulada.
 * @property currentXP Puntos de experiencia actuales en el nivel actual.
 * @property maxXP Puntos de experiencia necesarios para subir al siguiente nivel.
 * @property streak Racha de días consecutivos de estudio.
 * @property drops Moneda virtual del usuario (gotas/drops).
 * @property memberSince Fecha de registro del usuario formateada como "MMMM yyyy".
 * @property avatarLetter Primera letra del nombre de usuario para el avatar.
 *
 * @see DailyChallenge Para información sobre los desafíos diarios del usuario.
 * @see LessonFunction Para las lecciones disponibles.
 *
 * @author Kotodama Team
 * @since 1.0.0
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
    /**
     * Calcula el progreso de experiencia como un valor entre 0.0 y 1.0.
     *
     * Este valor es útil para mostrar barras de progreso en la UI.
     *
     * @return Valor flotante entre 0.0 (sin progreso) y 1.0 (nivel completo).
     */
    val xpProgress: Float
        get() = if (maxXP > 0) currentXP.toFloat() / maxXP.toFloat() else 0f
}

/**
 * Modelo de dominio que representa un desafío diario.
 *
 * Los desafíos diarios son objetivos que el usuario debe completar cada día
 * para ganar recompensas de XP y monedas. Se reinician a medianoche.
 *
 * @property id Identificador único del desafío.
 * @property completed Número de tareas completadas del desafío.
 * @property total Número total de tareas para completar el desafío.
 * @property timeRemaining Tiempo restante hasta que expire el desafío (formato string).
 * @property rewardXP Cantidad de puntos de experiencia otorgados al completar.
 * @property rewardCoins Cantidad de monedas otorgadas al completar.
 * @property difficulty Nivel de dificultad del desafío.
 *
 * @see ChallengeDifficulty Para los niveles de dificultad disponibles.
 * @see User Para la información del usuario que completa el desafío.
 *
 * @author Kotodama Team
 * @since 1.0.0
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
    /**
     * Calcula el progreso del desafío como un valor entre 0.0 y 1.0.
     *
     * @return Valor flotante entre 0.0 (sin progreso) y 1.0 (completado).
     */
    val progress: Float
        get() = if (total > 0) completed.toFloat() / total.toFloat() else 0f

    /**
     * Indica si el desafío ha sido completado.
     *
     * @return `true` si todas las tareas del desafío están completadas, `false` en caso contrario.
     */
    val isCompleted: Boolean
        get() = completed >= total
}

/**
 * Enumeración que define los niveles de dificultad para los desafíos diarios.
 *
 * Cada nivel de dificultad tiene un nombre para mostrar en la UI y una
 * recompensa de monedas asociada. A mayor dificultad, mayor recompensa.
 *
 * @property displayName Nombre de la dificultad para mostrar al usuario (en español).
 * @property coinReward Cantidad de monedas adicionales por completar un desafío de esta dificultad.
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
enum class ChallengeDifficulty(val displayName: String, val coinReward: Int) {
    /** Dificultad fácil: ideal para principiantes. Recompensa: 10 monedas. */
    EASY("Fácil", 10),

    /** Dificultad normal: nivel estándar. Recompensa: 20 monedas. */
    MEDIUM("Normal", 20),

    /** Dificultad difícil: para usuarios avanzados. Recompensa: 35 monedas. */
    HARD("Difícil", 35),

    /** Dificultad extrema: máximo desafío. Recompensa: 50 monedas. */
    EXTREME("Extremo", 50)
}

/**
 * Modelo de dominio que representa una función o lección disponible en la aplicación.
 *
 * Las funciones de lección son las diferentes categorías de contenido educativo
 * que el usuario puede estudiar, como Hiragana, Katakana, Kanji, etc.
 *
 * @property id Identificador único de la función/lección.
 * @property icon Emoji o código del icono para mostrar en la UI.
 * @property name Nombre de la lección (ej: "Hiragana", "Kanji").
 * @property subtitle Descripción breve del contenido de la lección.
 * @property isLocked Indica si la lección está bloqueada y requiere desbloqueo.
 * @property progress Porcentaje de progreso en la lección (0-100).
 *
 * @author Kotodama Team
 * @since 1.0.0
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
 * Modelo de dominio que representa un mensaje del personaje Kitsune.
 *
 * Kitsune es la mascota de la aplicación que proporciona mensajes motivacionales
 * y consejos al usuario durante su aprendizaje.
 *
 * @property message Contenido del mensaje del Kitsune.
 * @property timestamp Marca de tiempo Unix de cuando se generó el mensaje.
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
data class KitsuneMessage(
    val message: String,
    val timestamp: Long = System.currentTimeMillis()
)