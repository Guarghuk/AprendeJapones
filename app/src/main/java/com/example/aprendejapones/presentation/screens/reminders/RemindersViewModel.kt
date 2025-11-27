package com.example.aprendejapones.presentation.screens.reminders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aprendejapones.data.local.preferences.PreferencesManager
import com.example.aprendejapones.workers.WorkManagerScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.first

@HiltViewModel
class RemindersViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val workManagerScheduler: WorkManagerScheduler
): ViewModel() {

    companion object {
        private val REMINDERS_ENABLED = booleanPreferencesKey("reminders_enabled")
        private val REMINDER_HOUR = intPreferencesKey("reminder_hour")
        private val REMINDER_MINUTE = intPreferencesKey("reminder_minute")
        private val REMINDER_DAYS = stringSetPreferencesKey("reminder_days")
        private val MOTIVATIONAL_MESSAGES = booleanPreferencesKey("motivational_messages")
    }

    private val _state = MutableStateFlow(RemindersState())
    val state: StateFlow<RemindersState> = _state

    private val _effects = MutableSharedFlow<RemindersEffect>()
    val effects: SharedFlow<RemindersEffect> = _effects

    init {
        loadSettings()
    }

    /**
     * Cargar configuración guardada
     */
    private fun loadSettings() {
        viewModelScope.launch {
            try {
                val prefs = preferencesManager.dataStore.data.first()

                _state.value = RemindersState(
                    remindersEnabled = prefs[REMINDERS_ENABLED] ?: false,
                    selectedHour = prefs[REMINDER_HOUR] ?: 9,
                    selectedMinute = prefs[REMINDER_MINUTE] ?: 0,
                    selectedDays = prefs[REMINDER_DAYS] ?: setOf(
                        "Lunes", "Martes", "Miércoles", "Jueves", "Viernes"
                    ),
                    motivationalMessages = prefs[MOTIVATIONAL_MESSAGES] ?: false
                )
            } catch (e: Exception) {
                // Usar valores por defecto
            }
        }
    }

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

    /**
     * Guardar configuración y programar notificaciones
     */
    private fun saveSettings() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isSaving = true)

            try {
                val currentState = _state.value

                // Guardar en DataStore
                preferencesManager.dataStore.edit { prefs ->
                    prefs[REMINDERS_ENABLED] = currentState.remindersEnabled
                    prefs[REMINDER_HOUR] = currentState.selectedHour
                    prefs[REMINDER_MINUTE] = currentState.selectedMinute
                    prefs[REMINDER_DAYS] = currentState.selectedDays
                    prefs[MOTIVATIONAL_MESSAGES] = currentState.motivationalMessages
                }

                // Programar o cancelar recordatorios
                if (currentState.remindersEnabled) {
                    workManagerScheduler.scheduleReminders(
                        hour = currentState.selectedHour,
                        minute = currentState.selectedMinute,
                        selectedDays = currentState.selectedDays,
                        motivationalMessages = currentState.motivationalMessages
                    )
                } else {
                    workManagerScheduler.cancelReminders()
                }

                _state.value = _state.value.copy(isSaving = false)
                _effects.emit(RemindersEffect.ShowToast("Recordatorios guardados correctamente"))

            } catch (e: Exception) {
                _state.value = _state.value.copy(isSaving = false)
                _effects.emit(RemindersEffect.ShowToast("Error al guardar: ${e.message}"))
            }
        }
    }
}