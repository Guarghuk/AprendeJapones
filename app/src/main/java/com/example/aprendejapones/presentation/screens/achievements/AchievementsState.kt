package com.example.aprendejapones.presentation.screens.achievements

import com.example.aprendejapones.utils.Achievement

data class AchievementsState(
    val isLoading: Boolean = false,
    val achievements: List<Achievement> = emptyList(),
    val unlockedCount: Int = 0,
    val totalCount: Int = 0,
)