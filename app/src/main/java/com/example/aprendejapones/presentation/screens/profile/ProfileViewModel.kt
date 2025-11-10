package com.example.aprendejapones.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aprendejapones.utils.MockData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel para ProfileScreen
 */
class ProfileViewModel : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<ProfileEffect>()
    val effects: SharedFlow<ProfileEffect> = _effects.asSharedFlow()

    init {
        loadInitialData()
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.LoadData -> loadInitialData()
            is ProfileEvent.RefreshData -> refreshData()
            is ProfileEvent.NavigateToAchievements -> navigateToAchievements()
            is ProfileEvent.NavigateToStats -> navigateToStats()
            is ProfileEvent.DismissError -> dismissError()
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                delay(300) // Simular carga

                val user = MockData.getMockUser()
                val mockStats = MockData.getMockStats()
                val stats = ProfileStats(
                    streak = mockStats["streak"] as? Int ?: 0,
                    lessonsCompleted = mockStats["lessonsCompleted"] as? Int ?: 0,
                    totalTimeHours = mockStats["totalTime"] as? String ?: "0h"
                )
                val achievements = MockData.getMockAchievements()
                val activity = MockData.getMockRecentActivity()

                _state.update {
                    it.copy(
                        user = user,
                        stats = stats,
                        achievements = achievements,
                        recentActivity = activity,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al cargar perfil: ${e.message}"
                    )
                }
            }
        }
    }

    private fun refreshData() {
        viewModelScope.launch {
            try {
                val user = MockData.getMockUser()
                _state.update { it.copy(user = user) }
                _effects.emit(ProfileEffect.ShowToast("Perfil actualizado"))
            } catch (e: Exception) {
                _effects.emit(ProfileEffect.ShowToast("Error al actualizar"))
            }
        }
    }

    private fun navigateToAchievements() {
        viewModelScope.launch {
            _effects.emit(ProfileEffect.NavigateToAchievements)
        }
    }

    private fun navigateToStats() {
        viewModelScope.launch {
            _effects.emit(ProfileEffect.NavigateToStats)
        }
    }

    private fun dismissError() {
        _state.update { it.copy(error = null) }
    }
}