package com.example.aprendejapones.presentation.screens.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aprendejapones.domain.model.DailyChallenge
import com.example.aprendejapones.domain.model.User
import com.example.aprendejapones.utils.MockData
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

/**
 * ViewModel para la pantalla Home
 * Usa datos mock para el desarrollo de UI
 */
class HomeViewModel(
    private val savedStateHandle: SavedStateHandle  // Inyectado para persistencia granular
) : ViewModel() {

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
     * Carga los datos iniciales (simulado), restaurando de SavedStateHandle si existe
     */
    private fun loadInitialData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                // Simular carga de red
                delay(500)

                // Cargar datos mock base
                val user = MockData.getMockUser()
                val challenge = MockData.getMockDailyChallenge()
                val message = MockData.getMockKitsuneMessage()
                val functions = MockData.getMockLessonFunctions()

                // Restaurar estados mutables de SavedStateHandle (si existen, override los mock)
                val restoredRank: String? = savedStateHandle["user_rank"]
                val restoredStreak: Int? = savedStateHandle["user_streak"]
                val restoredDrops: Int? = savedStateHandle["user_drops"]
                val restoredChallengeCompleted: Int? = savedStateHandle["challenge_completed"]
                val restoredChallengeTotal: Int? = savedStateHandle["challenge_total"]
                val restoredChallengeRewardXP: Int? = savedStateHandle["challenge_reward_xp"]

                val restoredUser = user.copy(
                    rank = restoredRank ?: user.rank,
                    streak = restoredStreak ?: user.streak,
                    drops = restoredDrops ?: user.drops
                )

                val restoredChallenge = challenge.copy(
                    completed = restoredChallengeCompleted ?: challenge.completed,
                    total = restoredChallengeTotal ?: challenge.total,
                    rewardXP = restoredChallengeRewardXP ?: challenge.rewardXP
                )

                _state.update {
                    it.copy(
                        user = restoredUser,
                        dailyChallenge = restoredChallenge,
                        kitsuneMessage = message,
                        lessonFunctions = functions,
                        isLoading = false
                    )
                }

                // Guardar los valores iniciales/restaurados en SavedStateHandle por si hay cambios futuros
                saveUserState(restoredUser)
                saveChallengeState(restoredChallenge)

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
     * Refresca los datos y guarda cambios en SavedStateHandle
     */
    private fun refreshData() {
        viewModelScope.launch {
            try {
                val user = MockData.getMockUser()  // En real, de repo
                val challenge = MockData.getMockDailyChallenge()

                _state.update { it.copy(user = user, dailyChallenge = challenge) }

                // Guardar los nuevos valores
                saveUserState(user)
                saveChallengeState(challenge)

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
     * Actualiza el progreso del desafío diario y guarda en SavedStateHandle
     */
    private fun updateChallengeProgress() {
        viewModelScope.launch {
            val currentChallenge = _state.value.dailyChallenge ?: return@launch
            if (currentChallenge.completed < currentChallenge.total) {
                val updatedChallenge = currentChallenge.copy(
                    completed = currentChallenge.completed + 1
                )
                _state.update { it.copy(dailyChallenge = updatedChallenge) }

                // Guardar el progreso actualizado
                saveChallengeState(updatedChallenge)

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

    // Helpers privados para guardar estados granularmente
    private fun saveUserState(user: User) {
        savedStateHandle["user_rank"] = user.rank
        savedStateHandle["user_streak"] = user.streak
        savedStateHandle["user_drops"] = user.drops
    }

    private fun saveChallengeState(challenge: DailyChallenge) {
        savedStateHandle["challenge_completed"] = challenge.completed
        savedStateHandle["challenge_total"] = challenge.total
        savedStateHandle["challenge_reward_xp"] = challenge.rewardXP
    }
}