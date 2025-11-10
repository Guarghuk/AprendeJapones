package com.example.aprendejapones.presentation.screens.dailygoal

sealed class DailyGoalEvent {
    data class SelectGoal(val minutes: Int) : DailyGoalEvent()
    object SaveGoal : DailyGoalEvent()
}