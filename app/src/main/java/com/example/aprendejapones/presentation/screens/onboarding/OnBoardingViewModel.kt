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
 * Maneja la creación inicial del perfil y la navegación
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

    fun onEvent(event: OnboardingEvent) {
        when (event) {
            is OnboardingEvent.PageChanged -> {
                _state.update { it.copy(currentPage = event.page) }
            }
            is OnboardingEvent.UserNameChanged -> {
                _state.update { it.copy(userName = event.name) }
            }
            OnboardingEvent.CompleteOnboarding -> completeOnboarding()
            OnboardingEvent.SkipOnboarding -> skipOnboarding()
        }
    }

    private fun completeOnboarding() {
        viewModelScope.launch {
            val userName = _state.value.userName.trim()

            if (userName.isEmpty()) {
                _effects.emit(OnboardingEffect.ShowToast("Por favor ingresa tu nombre"))
                return@launch
            }

            _state.update { it.copy(isCreatingProfile = true, error = null) }

            try {
                // Crear o actualizar usuario
                val user = userRepository.getOrCreateUser()
                val updatedUser = user.copy(username = userName)
                userRepository.updateUser(updatedUser)

                // Guardar nombre en preferences
                preferencesManager.saveUserName(userName)

                // Marcar onboarding como completado
                preferencesManager.setOnboardingCompleted()
                preferencesManager.setFirstLaunchComplete()

                // Navegar a Home
                _effects.emit(OnboardingEffect.NavigateToHome)

            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isCreatingProfile = false,
                        error = "Error al crear perfil: ${e.message}"
                    )
                }
                _effects.emit(OnboardingEffect.ShowToast("Error al crear perfil"))
            }
        }
    }

    private fun skipOnboarding() {
        viewModelScope.launch {
            try {
                // Crear usuario con nombre por defecto
                val user = userRepository.getOrCreateUser()

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