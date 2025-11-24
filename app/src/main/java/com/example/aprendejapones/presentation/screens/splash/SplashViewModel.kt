package com.example.aprendejapones.presentation.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aprendejapones.data.local.preferences.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para pantalla de Splash
 * Determina si mostrar Onboarding o Home
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _navigationDestination = MutableStateFlow<String?>(null)
    val navigationDestination: StateFlow<String?> = _navigationDestination.asStateFlow()

    init {
        checkFirstLaunch()
    }

    private fun checkFirstLaunch() {
        viewModelScope.launch {
            try {
                // Leer si ya vio el onboarding
                val hasSeenOnboarding = preferencesManager.hasSeenOnboarding.first()

                // Decidir destino
                _navigationDestination.value = if (hasSeenOnboarding) {
                    "home"
                } else {
                    "onboarding"
                }
            } catch (e: Exception) {
                // En caso de error, ir a onboarding por defecto
                _navigationDestination.value = "onboarding"
            }
        }
    }
}