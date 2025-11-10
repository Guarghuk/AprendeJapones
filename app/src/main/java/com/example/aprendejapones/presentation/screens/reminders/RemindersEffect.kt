package com.example.aprendejapones.presentation.screens.reminders

sealed class RemindersEffect {
    data class ShowToast(val message: String) : RemindersEffect()
    // Puedes agregar más efectos si necesitas
}