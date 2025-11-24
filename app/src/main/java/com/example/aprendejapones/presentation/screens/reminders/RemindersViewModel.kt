package com.example.aprendejapones.presentation.screens.reminders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RemindersViewModel @Inject constructor(): ViewModel() {

    private val _state = MutableStateFlow(RemindersState())
    val state: StateFlow<RemindersState> = _state

    private val _effects = MutableSharedFlow<RemindersEffect>()
    val effects: SharedFlow<RemindersEffect> = _effects

    fun onEvent(event: RemindersEvent) {
        when (event) {
            is RemindersEvent.ToggleMaster -> {
                _state.value = _state.value.copy(remindersEnabled = event.enabled)
            }
            is RemindersEvent.SetTime -> {
                _state.value = _state.value.copy(
                    selectedHour = event.hour,
                    selectedMinute = event.minute
                )
            }
            is RemindersEvent.ToggleDay -> {
                val currentDays = _state.value.selectedDays.toMutableSet()
                if (currentDays.contains(event.day)) {
                    currentDays.remove(event.day)
                } else {
                    currentDays.add(event.day)
                }
                _state.value = _state.value.copy(selectedDays = currentDays)
            }
            is RemindersEvent.ToggleMotivational -> {
                _state.value = _state.value.copy(motivationalMessages = event.enabled)
            }
            RemindersEvent.SaveSettings -> {
                saveSettings()
            }
        }
    }

    private fun saveSettings() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isSaving = true)
            // Simula almacenamiento mientras conectas tu lógica real
            kotlinx.coroutines.delay(700)
            _state.value = _state.value.copy(isSaving = false)
            _effects.emit(RemindersEffect.ShowToast("Configuración de recordatorios guardada"))
        }
    }
}