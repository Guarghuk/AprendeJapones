package com.example.aprendejapones.presentation.screens.stats

import com.example.aprendejapones.domain.repository.CategoryProgress

data class StatsState(
    val isLoading: Boolean = true,
    val totalXP: Int = 0,
    val currentLevel: Int = 1,
    val rank: String = "Bronce",

    val lessonsCompleted: Int = 0,
    val studyTimeMinutes: Int = 0,
    val wordsLearned: Int = 0,
    val kanjiLearned: Int = 0,

    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val totalDaysStudied: Int = 0,

    val progressList: List<CategoryProgress> = emptyList(),

    val weeklyMinutes: List<Int> = List(7) { 0 } // [L, M, X, J, V, S, D]
)