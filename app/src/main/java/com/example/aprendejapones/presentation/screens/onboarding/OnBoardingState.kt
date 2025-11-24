package com.example.aprendejapones.presentation.screens.onboarding

/**
 * Estado del Onboarding
 */
data class OnboardingState(
    val currentPage: Int = 0,
    val userName: String = "",
    val isCreatingProfile: Boolean = false,
    val error: String? = null
)

/**
 * Eventos del Onboarding
 */
sealed class OnboardingEvent {
    data class PageChanged(val page: Int) : OnboardingEvent()
    data class UserNameChanged(val name: String) : OnboardingEvent()
    object CompleteOnboarding : OnboardingEvent()
    object SkipOnboarding : OnboardingEvent()
}

/**
 * Efectos secundarios
 */
sealed class OnboardingEffect {
    object NavigateToHome : OnboardingEffect()
    data class ShowToast(val message: String) : OnboardingEffect()
}