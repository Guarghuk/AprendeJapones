package com.example.aprendejapones.presentation.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aprendejapones.data.local.preferences.PreferencesManager
import com.example.aprendejapones.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para Onboarding
 * Maneja la navegación del onboarding después del registro
 * El nombre del usuario ya se obtuvo durante el registro
 */
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<OnboardingEffect>()
    val effects: SharedFlow<OnboardingEffect> = _effects.asSharedFlow()

    init {
        // Cargar el nombre del usuario desde las preferencias (guardado durante el registro)
        loadUserName()
    }

    private fun loadUserName() {
        viewModelScope.launch {
            val name = preferencesManager.userName.first()
            _state.update { it.copy(userName = name ?: "") }
        }
    }

    fun onEvent(event: OnboardingEvent) {
        when (event) {
            is OnboardingEvent.PageChanged -> {
                _state.update { it.copy(currentPage = event.page) }
            }
            OnboardingEvent.CompleteOnboarding -> completeOnboarding()
            OnboardingEvent.SkipOnboarding -> skipOnboarding()
        }
    }

    private fun completeOnboarding() {
        viewModelScope.launch {
            _state.update { it.copy(isCreatingProfile = true, error = null) }

            try {
                // Marcar onboarding como completado
                preferencesManager.setOnboardingCompleted()
                preferencesManager.setFirstLaunchComplete()

                // Navegar a Home
                _effects.emit(OnboardingEffect.NavigateToHome)

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isCreatingProfile = false,
                        error = "Error al completar: ${e.message}"
                    )
                }
                _effects.emit(OnboardingEffect.ShowToast("Error al completar"))
            }
        }
    }

    private fun skipOnboarding() {
        viewModelScope.launch {
            try {
                // Marcar onboarding como completado
                preferencesManager.setOnboardingCompleted()
                preferencesManager.setFirstLaunchComplete()

                // Navegar a Home
                _effects.emit(OnboardingEffect.NavigateToHome)

            } catch (e: Exception) {
                _effects.emit(OnboardingEffect.ShowToast("Error al iniciar"))
            }
        }
    }
}