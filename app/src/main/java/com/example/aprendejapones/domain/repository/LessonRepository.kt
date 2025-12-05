package com.example.aprendejapones.domain.repository

import com.example.aprendejapones.domain.model.DailyChallenge
import kotlinx.coroutines.flow.Flow

/**
 * Modelo de datos que representa las estadísticas de lecciones del usuario.
 *
 * Contiene información agregada sobre el progreso del usuario en todas
 * las lecciones completadas.
 *
 * @property totalLessonsCompleted Número total de lecciones completadas.
 * @property totalStudyTimeMinutes Tiempo total de estudio en minutos.
 * @property totalXPEarned Total de puntos de experiencia ganados.
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
data class LessonStats(
    val totalLessonsCompleted: Int,
    val totalStudyTimeMinutes: Int,
    val totalXPEarned: Int
)

/**
 * Repositorio para la gestión de lecciones y desafíos diarios.
 *
 * Esta interfaz define el contrato para las operaciones relacionadas con
 * las lecciones de aprendizaje de japonés y los desafíos diarios que
 * el usuario puede completar.
 *
 * ## Responsabilidades
 * - Guardar el progreso de lecciones completadas
 * - Obtener estadísticas de estudio
 * - Gestionar desafíos diarios (creación, actualización, progreso)
 *
 * ## Desafíos Diarios
 * Los desafíos se reinician cada día a medianoche y otorgan
 * recompensas de XP y monedas al completarse.
 *
 * @see LessonStats Estadísticas de lecciones.
 * @see DailyChallenge Modelo del desafío diario.
 * @see LessonContentRepository Para el contenido de las lecciones.
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
interface LessonRepository {

    /**
     * Guarda una lección completada en el historial.
     *
     * Registra los detalles de la lección para estadísticas y tracking
     * del progreso del usuario.
     *
     * @param lessonType Tipo de lección (ej: "Hiragana", "Kanji").
     * @param lessonName Nombre específico de la lección.
     * @param totalQuestions Número total de preguntas en la lección.
     * @param correctAnswers Número de respuestas correctas.
     * @param xpEarned Puntos de experiencia ganados.
     * @param timeSpentSeconds Tiempo empleado en segundos.
     */
    suspend fun saveLesson(
        lessonType: String,
        lessonName: String,
        totalQuestions: Int,
        correctAnswers: Int,
        xpEarned: Int,
        timeSpentSeconds: Int
    )

    /**
     * Obtiene las estadísticas agregadas de todas las lecciones.
     *
     * @return [LessonStats] con las estadísticas totales del usuario.
     */
    suspend fun getLessonStats(): LessonStats

    /**
     * Obtiene el desafío diario de hoy.
     *
     * @return El [DailyChallenge] de hoy o `null` si no existe.
     */
    suspend fun getTodayChallenge(): DailyChallenge?

    /**
     * Obtiene el desafío diario como un Flow reactivo.
     *
     * Permite observar cambios en tiempo real del desafío diario,
     * actualizándose automáticamente cuando hay progreso.
     *
     * @return Flow que emite el [DailyChallenge] actual o `null`.
     */
    fun getTodayChallengeFlow(): Flow<DailyChallenge?>

    /**
     * Actualiza el progreso del desafío diario.
     *
     * Incrementa el contador de tareas completadas del desafío.
     * Debe llamarse cada vez que el usuario complete una actividad
     * que contribuya al desafío.
     */
    suspend fun updateChallengeProgress()

    /**
     * Inicializa el desafío del día si no existe.
     *
     * Crea un nuevo desafío diario con valores aleatorios si aún
     * no se ha creado uno para el día actual. Si ya existe, no
     * hace nada.
     */
    suspend fun initializeTodayChallenge()
}