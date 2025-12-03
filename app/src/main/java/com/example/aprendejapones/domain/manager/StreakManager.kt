package com.example.aprendejapones.domain.manager

import androidx.datastore.preferences.core.edit
import com.example.aprendejapones.data.local.preferences.PreferencesManager
import com.example.aprendejapones.domain.repository.LessonRepository
import com.example.aprendejapones.domain.repository.UserRepository
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manager para la gestión de la racha de estudio del usuario.
 *
 * Este componente gestiona la lógica de negocio relacionada con las rachas
 * de estudio consecutivas. Una racha se mantiene estudiando al menos una
 * vez al día y se pierde si pasa más de un día sin estudiar.
 *
 * ## Funcionamiento de la Racha
 * - La racha comienza en 1 cuando el usuario estudia por primera vez
 * - Se incrementa cada día que el usuario estudia (basado en día calendario)
 * - Se reinicia a 1 si el usuario se salta un día
 * - Se considera "estudiar" cuando se completa cualquier actividad de aprendizaje
 *
 * ## Verificación Automática
 * Este manager es utilizado por [StreakWorker] para verificar diariamente
 * si la racha debe reiniciarse cuando el usuario no ha estudiado.
 *
 * ## Uso
 *
 * ```kotlin
 * // Al completar una lección
 * streakManager.checkAndUpdateStreak()
 *
 * // Verificar si estudió hoy
 * val studiedToday = streakManager.hasStudiedToday()
 *
 * // Obtener racha actual
 * val currentStreak = streakManager.getCurrentStreak()
 * ```
 *
 * @property userRepository Repositorio para actualizar datos del usuario.
 * @property lessonRepository Repositorio para datos de lecciones.
 * @property preferencesManager Manager de preferencias para persistir fechas.
 *
 * @see com.example.aprendejapones.workers.StreakWorker Worker que verifica rachas.
 * @see UserRepository Para la gestión del usuario.
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
@Singleton
class StreakManager @Inject constructor(
    private val userRepository: UserRepository,
    private val lessonRepository: LessonRepository,
    private val preferencesManager: PreferencesManager
) {
    /** Formato de fecha para almacenar y comparar días: "yyyy-MM-dd" */
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    companion object {
        /** Clave para almacenar la última fecha de estudio en DataStore */
        private const val PREF_LAST_STUDY_DATE = "last_study_date"

        /** Clave para almacenar la racha actual en DataStore */
        private const val PREF_CURRENT_STREAK = "current_streak"

        /** Clave para almacenar la racha más larga en DataStore */
        private const val PREF_LONGEST_STREAK = "longest_streak"
    }

    /**
     * Verifica y actualiza la racha cuando el usuario estudia.
     *
     * Esta función debe llamarse cada vez que el usuario complete
     * una actividad de aprendizaje. Maneja los siguientes escenarios:
     *
     * 1. **Primera vez estudiando:** Establece racha en 1
     * 2. **Ya estudió hoy:** No incrementa la racha
     * 3. **Estudió ayer:** Incrementa la racha en 1
     * 4. **Más de un día sin estudiar:** Reinicia la racha a 1
     */
    suspend fun checkAndUpdateStreak() {
        val today = getTodayDate()
        val lastStudyDate = getLastStudyDate()
        val currentStreak = getCurrentStreak()

        when {
            // Primera vez estudiando
            lastStudyDate == null -> {
                setStreak(1)
                saveLastStudyDate(today)
            }

            // Ya estudió hoy (no hacer nada)
            lastStudyDate == today -> {
                // No incrementar racha, ya estudió hoy
            }

            // Estudió ayer (continuar racha)
            isYesterday(lastStudyDate) -> {
                val newStreak = currentStreak + 1
                setStreak(newStreak)
                saveLastStudyDate(today)
                updateLongestStreak(newStreak)
            }

            // Rompió la racha (más de 1 día sin estudiar)
            else -> {
                setStreak(1) // Reiniciar racha
                saveLastStudyDate(today)
            }
        }
    }

    /**
     * Verifica si el usuario ha perdido su racha.
     *
     * Esta función es llamada por el [StreakWorker] a medianoche para
     * detectar si el usuario no estudió el día anterior y debe perder
     * su racha.
     *
     * Solo reinicia la racha si la última fecha de estudio no es
     * ni hoy ni ayer.
     */
    suspend fun checkStreakExpiration() {
        val today = getTodayDate()
        val lastStudyDate = getLastStudyDate()

        if (lastStudyDate != null && !isYesterday(lastStudyDate) && lastStudyDate != today) {
            // Si no es hoy ni ayer, perdió la racha
            setStreak(0)
        }
    }

    /**
     * Obtiene la racha actual del usuario.
     *
     * @return Número de días de la racha actual, o 0 si no hay racha.
     */
    suspend fun getCurrentStreak(): Int {
        return userRepository.getCurrentUser()?.streak ?: 0
    }

    /**
     * Obtiene la racha más larga alcanzada por el usuario.
     *
     * @return Número de días de la racha más larga.
     */
    suspend fun getLongestStreak(): Int {
        // TODO: Guardar en DataStore o Room
        return getCurrentStreak()
    }

    /**
     * Verifica si el usuario estudió hoy.
     *
     * @return `true` si la última fecha de estudio es hoy, `false` en caso contrario.
     */
    suspend fun hasStudiedToday(): Boolean {
        val today = getTodayDate()
        val lastStudyDate = getLastStudyDate()
        return lastStudyDate == today
    }

    // ========== Funciones Privadas ==========

    /**
     * Obtiene la fecha de hoy en formato "yyyy-MM-dd".
     *
     * @return String con la fecha de hoy formateada.
     */
    private fun getTodayDate(): String {
        return dateFormat.format(Date())
    }

    /**
     * Obtiene la última fecha de estudio almacenada.
     *
     * @return String con la fecha o `null` si nunca ha estudiado.
     */
    private suspend fun getLastStudyDate(): String? {
        // Guardado en DataStore
        return try {
            preferencesManager.dataStore.data.first()[
                androidx.datastore.preferences.core.stringPreferencesKey(PREF_LAST_STUDY_DATE)
            ]
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Guarda la última fecha de estudio.
     *
     * @param date Fecha a guardar en formato "yyyy-MM-dd".
     */
    private suspend fun saveLastStudyDate(date: String) {
        preferencesManager.dataStore.edit { preferences ->
            preferences[androidx.datastore.preferences.core.stringPreferencesKey(PREF_LAST_STUDY_DATE)] = date
        }
    }

    /**
     * Actualiza la racha del usuario.
     *
     * @param streak Nueva cantidad de días de racha.
     */
    private suspend fun setStreak(streak: Int) {
        userRepository.updateStreak(streak)
    }

    /**
     * Actualiza la racha más larga si la actual es mayor.
     *
     * @param currentStreak Racha actual para comparar.
     */
    private suspend fun updateLongestStreak(currentStreak: Int) {
        val longestStreak = getLongestStreak()
        if (currentStreak > longestStreak) {
            // TODO: Guardar en DataStore
            preferencesManager.dataStore.edit { preferences ->
                preferences[androidx.datastore.preferences.core.intPreferencesKey(PREF_LONGEST_STREAK)] = currentStreak
            }
        }
    }

    /**
     * Verifica si una fecha corresponde al día de ayer.
     *
     * @param dateString Fecha a verificar en formato "yyyy-MM-dd".
     * @return `true` si la fecha es ayer, `false` en caso contrario.
     */
    private fun isYesterday(dateString: String): Boolean {
        return try {
            val lastDate = dateFormat.parse(dateString)
            val yesterday = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, -1)
            }.time

            val lastCal = Calendar.getInstance().apply {
                time = lastDate ?: return false
            }
            val yesterdayCal = Calendar.getInstance().apply {
                time = yesterday
            }

            lastCal.get(Calendar.YEAR) == yesterdayCal.get(Calendar.YEAR) &&
                    lastCal.get(Calendar.DAY_OF_YEAR) == yesterdayCal.get(Calendar.DAY_OF_YEAR)
        } catch (e: Exception) {
            false
        }
    }
}