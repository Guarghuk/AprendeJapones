package com.example.aprendejapones.presentation.screens.reminders

sealed class RemindersEffect {
    data class ShowToast(val message: String) : RemindersEffect()
    data class TriggerTestNotification(val useMotivational: Boolean) : RemindersEffect()
}