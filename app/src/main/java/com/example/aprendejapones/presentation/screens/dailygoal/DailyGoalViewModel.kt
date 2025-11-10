package com.example.aprendejapones.presentation.screens.dailygoal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class DailyGoalViewModel : ViewModel() {

    private val _state = MutableStateFlow(DailyGoalState())
    val state: StateFlow<DailyGoalState> = _state

    // Usado para efectos “únicos”, como mostrar Toasts
    private val _effects = MutableSharedFlow<DailyGoalEffect>()
    val effects: SharedFlow<DailyGoalEffect> = _effects

    fun onEvent(event: DailyGoalEvent) {
        when (event) {
            is DailyGoalEvent.SelectGoal -> {
                _state.value = _state.value.copy(selectedGoal = event.minutes)
            }
            DailyGoalEvent.SaveGoal -> {
                saveGoal()
            }
        }
    }

    private fun saveGoal() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isSaving = true)
            // Simula guardar la meta, conecta aquí tu repositorio/datasource real
            kotlinx.coroutines.delay(800)
            _state.value = _state.value.copy(isSaving = false)
            _effects.emit(DailyGoalEffect.ShowToast("Meta diaria guardada correctamente"))
        }
    }
}