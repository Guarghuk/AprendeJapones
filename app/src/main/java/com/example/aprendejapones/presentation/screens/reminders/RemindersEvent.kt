package com.example.aprendejapones.presentation.screens.reminders

sealed class RemindersEvent {
    data class ToggleMaster(val enabled: Boolean) : RemindersEvent()
    data class SetTime(val hour: Int, val minute: Int) : RemindersEvent()
    data class ToggleDay(val day: String) : RemindersEvent()
    data class ToggleMotivational(val enabled: Boolean) : RemindersEvent()
    object SaveSettings : RemindersEvent()
    object TestNotification : RemindersEvent()
}