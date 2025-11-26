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
 * Manager para gestionar la racha del usuario
 * Detecta si estudió hoy, actualiza racha automáticamente
 */
@Singleton
class StreakManager @Inject constructor(
    private val userRepository: UserRepository,
    private val lessonRepository: LessonRepository,
    private val preferencesManager: PreferencesManager
) {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    companion object {
        private const val PREF_LAST_STUDY_DATE = "last_study_date"
        private const val PREF_CURRENT_STREAK = "current_streak"
        private const val PREF_LONGEST_STREAK = "longest_streak"
    }

    /**
     * Verifica y actualiza la racha cuando el usuario estudia
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
     * Verifica si el usuario ha perdido su racha (llamado por Worker)
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
     * Obtiene la racha actual del usuario
     */
    suspend fun getCurrentStreak(): Int {
        return userRepository.getCurrentUser()?.streak ?: 0
    }

    /**
     * Obtiene la racha más larga del usuario
     */
    suspend fun getLongestStreak(): Int {
        // TODO: Guardar en DataStore o Room
        return getCurrentStreak()
    }

    /**
     * Verifica si el usuario estudió hoy
     */
    suspend fun hasStudiedToday(): Boolean {
        val today = getTodayDate()
        val lastStudyDate = getLastStudyDate()
        return lastStudyDate == today
    }

    // ========== Funciones Privadas ==========

    private fun getTodayDate(): String {
        return dateFormat.format(Date())
    }

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

    private suspend fun saveLastStudyDate(date: String) {
        preferencesManager.dataStore.edit { preferences ->
            preferences[androidx.datastore.preferences.core.stringPreferencesKey(PREF_LAST_STUDY_DATE)] = date
        }
    }

    private suspend fun setStreak(streak: Int) {
        userRepository.updateStreak(streak)
    }

    private suspend fun updateLongestStreak(currentStreak: Int) {
        val longestStreak = getLongestStreak()
        if (currentStreak > longestStreak) {
            // TODO: Guardar en DataStore
            preferencesManager.dataStore.edit { preferences ->
                preferences[androidx.datastore.preferences.core.intPreferencesKey(PREF_LONGEST_STREAK)] = currentStreak
            }
        }
    }

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