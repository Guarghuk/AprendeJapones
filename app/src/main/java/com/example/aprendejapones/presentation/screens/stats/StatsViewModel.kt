package com.example.aprendejapones.presentation.screens.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aprendejapones.domain.repository.LessonRepository
import com.example.aprendejapones.domain.repository.ProgressRepository
import com.example.aprendejapones.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val lessonRepository: LessonRepository,
    private val progressRepository: ProgressRepository
): ViewModel() {

    private val _state = MutableStateFlow(StatsState(isLoading = true))
    val state: StateFlow<StatsState> = _state

    init {
        loadStats()
    }

    private fun loadStats() {
        viewModelScope.launch {
            try {
                // Obtener usuario actual
                val user = userRepository.getCurrentUser()

                // Obtener estadísticas de lecciones
                val lessonStats = lessonRepository.getLessonStats()

                // Obtener progreso por categoría
                val progressList = progressRepository.getUserProgressFlow().first()

                // Calcular totales
                val totalWordsLearned = progressList
                    .filter { it.category == "vocabulary" }
                    .sumOf { it.itemsLearned }

                val totalKanjiLearned = progressList
                    .filter { it.category == "kanji" }
                    .sumOf { it.itemsLearned }

                _state.value = StatsState(
                    isLoading = false,
                    totalXP = user?.currentXP ?: 0,
                    currentLevel = user?.level ?: 1,
                    rank = user?.rank ?: "初心者",

                    lessonsCompleted = lessonStats.totalLessonsCompleted,
                    studyTimeMinutes = lessonStats.totalStudyTimeMinutes,
                    wordsLearned = totalWordsLearned,
                    kanjiLearned = totalKanjiLearned,

                    currentStreak = user?.streak ?: 0,
                    longestStreak = user?.streak ?: 0, // TODO: Implementar longest streak
                    totalDaysStudied = lessonStats.totalLessonsCompleted, // Aproximación

                    progressList = progressList,

                    weeklyMinutes = listOf(45, 62, 73, 40, 80, 20, 67) // TODO: Datos reales
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }
}