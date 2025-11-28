package com.example.aprendejapones.presentation.screens.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aprendejapones.data.local.preferences.PreferencesManager
import com.example.aprendejapones.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para MenuScreen
 */
@HiltViewModel
class MenuViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferencesManager: PreferencesManager
): ViewModel() {

    private val _state = MutableStateFlow(MenuState())
    val state: StateFlow<MenuState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<MenuEffect>()
    val effects: SharedFlow<MenuEffect> = _effects.asSharedFlow()

    fun onEvent(event: MenuEvent) {
        when (event) {
            is MenuEvent.NavigateToScreen -> navigateToScreen(event.screen)
            is MenuEvent.ToggleDarkMode -> toggleDarkMode(event.enabled)
            is MenuEvent.ToggleSound -> toggleSound(event.enabled)
            is MenuEvent.Logout -> logout()
            is MenuEvent.DismissError -> dismissError()
        }
    }

    private fun navigateToScreen(screen: String) {
        viewModelScope.launch {
            // Handle toggle screens locally
            when (screen) {
                "darkMode" -> {
                    val newValue = !_state.value.isDarkMode
                    _state.update { it.copy(isDarkMode = newValue) }
                    _effects.emit(
                        MenuEffect.ShowToast(
                            if (newValue) "Modo oscuro activado" else "Modo oscuro desactivado"
                        )
                    )
                }
                "sound" -> {
                    val newValue = !_state.value.isSoundEnabled
                    _state.update { it.copy(isSoundEnabled = newValue) }
                    _effects.emit(
                        MenuEffect.ShowToast(
                            if (newValue) "Sonidos activados" else "Sonidos desactivados"
                        )
                    )
                }
                "rate" -> {
                    // TODO: Open Play Store
                    _effects.emit(MenuEffect.ShowToast("Próximamente disponible"))
                }
                else -> _effects.emit(MenuEffect.NavigateToScreen(screen))
            }
        }
    }

    private fun toggleDarkMode(enabled: Boolean) {
        _state.update { it.copy(isDarkMode = enabled) }
        viewModelScope.launch {
            _effects.emit(
                MenuEffect.ShowToast(
                    if (enabled) "Modo oscuro activado" else "Modo oscuro desactivado"
                )
            )
        }
    }

    private fun toggleSound(enabled: Boolean) {
        _state.update { it.copy(isSoundEnabled = enabled) }
        viewModelScope.launch {
            _effects.emit(
                MenuEffect.ShowToast(
                    if (enabled) "Sonidos activados" else "Sonidos desactivados"
                )
            )
        }
    }

    private fun logout() {
        viewModelScope.launch {
            try {
                // Logout from Firebase
                authRepository.logout()
                // Clear preferences
                preferencesManager.clearAll()
                // Navigate to login
                _effects.emit(MenuEffect.NavigateToLogin)
            } catch (e: Exception) {
                _effects.emit(MenuEffect.ShowToast("Error al cerrar sesión"))
            }
        }
    }

    private fun dismissError() {
        _state.update { it.copy(error = null) }
    }
}