package com.example.aprendejapones.presentation.screens.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(): ViewModel() {

    private val _state = MutableStateFlow(StatsState(isLoading = true))
    val state: StateFlow<StatsState> = _state

    init {
        loadStats()
    }

    private fun loadStats() {
        viewModelScope.launch {
            // Simulación de carga asíncrona
            kotlinx.coroutines.delay(900)
            _state.value = StatsState(
                isLoading = false,
                totalXP = 4260,
                currentLevel = 8,
                rank = "Plata",

                lessonsCompleted = 72,
                studyTimeMinutes = 970,
                wordsLearned = 430,
                kanjiLearned = 112,

                currentStreak = 14,
                longestStreak = 32,
                totalDaysStudied = 98,

                hiraganaProgress = 100,
                katakanaProgress = 100,
                kanjiProgress = 44,
                grammarProgress = 57,
                vocabularyProgress = 86,

                weeklyMinutes = listOf(45, 62, 73, 40, 80, 20, 67)
            )
        }
    }
}