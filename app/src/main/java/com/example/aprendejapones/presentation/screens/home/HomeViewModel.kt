package com.example.aprendejapones.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aprendejapones.utils.MockData
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

/**
 * ViewModel para la pantalla Home
 * Usa datos mock para el desarrollo de UI
 */
class HomeViewModel : ViewModel() {

    // Estado privado mutable
    private val _state = MutableStateFlow(HomeState())
    // Estado público inmutable
    val state: StateFlow<HomeState> = _state.asStateFlow()

    // Canal para efectos secundarios (one-time events)
    private val _effects = MutableSharedFlow<HomeEffect>()
    val effects: SharedFlow<HomeEffect> = _effects.asSharedFlow()

    init {
        loadInitialData()
    }

    /**
     * Maneja todos los eventos de la UI
     */
    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.LoadData -> loadInitialData()
            is HomeEvent.RefreshData -> refreshData()
            is HomeEvent.SelectFunction -> handleFunctionSelection(event.functionName)
            is HomeEvent.DismissError -> dismissError()
            is HomeEvent.MarkChallengeProgress -> updateChallengeProgress()
        }
    }

    /**
     * Carga los datos iniciales (simulado)
     */
    private fun loadInitialData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                // Simular carga de red
                delay(500)

                val user = MockData.getMockUser()
                val challenge = MockData.getMockDailyChallenge()
                val message = MockData.getMockKitsuneMessage()
                val functions = MockData.getMockLessonFunctions()

                _state.update {
                    it.copy(
                        user = user,
                        dailyChallenge = challenge,
                        kitsuneMessage = message,
                        lessonFunctions = functions,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al cargar los datos: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * Refresca los datos
     */
    private fun refreshData() {
        viewModelScope.launch {
            try {
                val user = MockData.getMockUser()
                val challenge = MockData.getMockDailyChallenge()

                _state.update {
                    it.copy(
                        user = user,
                        dailyChallenge = challenge
                    )
                }

                _effects.emit(HomeEffect.ShowToast("Datos actualizados"))
            } catch (e: Exception) {
                _effects.emit(HomeEffect.ShowToast("Error al actualizar"))
            }
        }
    }

    /**
     * Maneja la selección de una función
     */
    private fun handleFunctionSelection(functionName: String) {
        viewModelScope.launch {
            _effects.emit(HomeEffect.NavigateToLesson(functionName))
        }
    }

    /**
     * Actualiza el progreso del desafío diario
     */
    private fun updateChallengeProgress() {
        viewModelScope.launch {
            val currentChallenge = _state.value.dailyChallenge ?: return@launch

            if (currentChallenge.completed < currentChallenge.total) {
                val updatedChallenge = currentChallenge.copy(
                    completed = currentChallenge.completed + 1
                )

                _state.update {
                    it.copy(dailyChallenge = updatedChallenge)
                }

                if (updatedChallenge.isCompleted) {
                    _effects.emit(
                        HomeEffect.ShowToast("¡Desafío completado! +${updatedChallenge.rewardXP} XP")
                    )
                }
            }
        }
    }

    /**
     * Descarta el error actual
     */
    private fun dismissError() {
        _state.update { it.copy(error = null) }
    }
}