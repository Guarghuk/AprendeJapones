package com.example.aprendejapones.presentation.screens.reminders

data class RemindersState(
    val remindersEnabled: Boolean = false,
    val selectedHour: Int = 9,
    val selectedMinute: Int = 0,
    val selectedDays: Set<String> = setOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes"),
    val motivationalMessages: Boolean = false,
    val isSaving: Boolean = false
)