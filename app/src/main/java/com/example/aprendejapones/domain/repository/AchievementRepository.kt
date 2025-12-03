package com.example.aprendejapones.domain.repository

import com.example.aprendejapones.utils.Achievement
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio para la gestión de logros (achievements).
 *
 * Esta interfaz define el contrato para las operaciones relacionadas con
 * el sistema de logros de la aplicación. Los logros se desbloquean
 * automáticamente cuando el usuario cumple ciertos criterios.
 *
 * ## Sistema de Logros
 * - Los logros tienen un progreso que se actualiza según las acciones del usuario
 * - Cuando el progreso alcanza el objetivo, el logro se desbloquea
 * - Algunos logros pueden otorgar recompensas de XP o monedas
 *
 * ## Ejemplos de Logros
 * - "Primer paso" - Completar la primera lección
 * - "Racha de 7 días" - Estudiar 7 días consecutivos
 * - "Maestro del Hiragana" - Completar todas las lecciones de Hiragana
 *
 * @see Achievement Modelo de logro.
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
interface AchievementRepository {

    /**
     * Obtiene todos los logros del usuario como Flow reactivo.
     *
     * Emite actualizaciones cuando cambia el estado de algún logro,
     * permitiendo actualizar la UI automáticamente.
     *
     * @return Flow que emite la lista de [Achievement].
     */
    fun getUserAchievementsFlow(): Flow<List<Achievement>>

    /**
     * Obtiene todos los logros del usuario de forma única.
     *
     * Útil cuando solo se necesita el valor actual sin observar cambios.
     *
     * @return Lista de [Achievement] del usuario.
     */
    suspend fun getUserAchievements(): List<Achievement>

    /**
     * Inicializa los logros por defecto para un usuario nuevo.
     *
     * Crea todos los logros disponibles con progreso inicial de 0.
     * Este método debe llamarse al crear un nuevo usuario.
     */
    suspend fun initializeDefaultAchievements()

    /**
     * Desbloquea un logro específico.
     *
     * Marca el logro como completado y registra la fecha de desbloqueo.
     * Puede otorgar recompensas asociadas al logro.
     *
     * @param achievementId ID del logro a desbloquear.
     */
    suspend fun unlockAchievement(achievementId: String)

    /**
     * Actualiza el progreso de un logro.
     *
     * Incrementa el contador de progreso del logro. Si el progreso
     * alcanza el objetivo, el logro debe desbloquearse automáticamente.
     *
     * @param achievementId ID del logro a actualizar.
     * @param progress Nuevo valor de progreso.
     */
    suspend fun updateAchievementProgress(achievementId: String, progress: Int)

    /**
     * Verifica y desbloquea logros basándose en las estadísticas del usuario.
     *
     * Este método debe llamarse después de acciones que puedan
     * contribuir a desbloquear logros, como completar lecciones,
     * subir de nivel, etc.
     */
    suspend fun checkAchievements()
}