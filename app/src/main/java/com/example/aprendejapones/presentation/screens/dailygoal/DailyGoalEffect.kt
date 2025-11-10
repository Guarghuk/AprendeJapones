package com.example.aprendejapones.presentation.screens.dailygoal

sealed class DailyGoalEffect {
    data class ShowToast(val message: String) : DailyGoalEffect()
}