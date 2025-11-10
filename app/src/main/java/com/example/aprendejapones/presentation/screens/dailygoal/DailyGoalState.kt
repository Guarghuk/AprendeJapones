package com.example.aprendejapones.presentation.screens.dailygoal

data class DailyGoalState(
    val selectedGoal: Int = 10,
    val daysCompleted: Int = 0,
    val currentStreak: Int = 0,
    val isSaving: Boolean = false,
)