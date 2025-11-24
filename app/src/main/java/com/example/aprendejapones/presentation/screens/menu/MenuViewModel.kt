package com.example.aprendejapones.presentation.screens.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para MenuScreen
 */
@HiltViewModel
class MenuViewModel @Inject constructor(): ViewModel() {

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
            _effects.emit(MenuEffect.NavigateToScreen(screen))
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
            // TODO: Implementar lógica de logout
            _effects.emit(MenuEffect.NavigateToLogin)
        }
    }

    private fun dismissError() {
        _state.update { it.copy(error = null) }
    }
}