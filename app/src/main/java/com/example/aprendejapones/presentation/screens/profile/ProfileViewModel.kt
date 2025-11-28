package com.example.aprendejapones.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aprendejapones.domain.repository.AchievementRepository
import com.example.aprendejapones.domain.repository.LessonRepository
import com.example.aprendejapones.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val achievementRepository: AchievementRepository,
    private val lessonRepository: LessonRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<ProfileEffect>()
    val effects: SharedFlow<ProfileEffect> = _effects.asSharedFlow()

    init {
        loadInitialData()
        observeUserChanges()
    }
    
    /**
     * Observe user changes reactively to update UI when data changes
     */
    private fun observeUserChanges() {
        viewModelScope.launch {
            userRepository.getCurrentUserFlow().collect { user ->
                _state.update { it.copy(user = user) }
            }
        }
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
                // Get user
                val user = userRepository.getCurrentUser()

                // Get achievements
                val achievements = achievementRepository.getUserAchievements()

                // Get lesson stats
                val lessonStats = lessonRepository.getLessonStats()
                val stats = ProfileStats(
                    streak = user?.streak ?: 0,
                    lessonsCompleted = lessonStats.totalLessonsCompleted,
                    totalTimeHours = "${lessonStats.totalStudyTimeMinutes / 60}h"
                )

                // Mock activity for now
                val activity = com.example.aprendejapones.utils.MockData.getMockRecentActivity()

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
                        error = "Error loading profile: ${e.message}"
                    )
                }
            }
        }
    }

    private fun refreshData() {
        viewModelScope.launch {
            try {
                loadInitialData()
                _effects.emit(ProfileEffect.ShowToast("Profile refreshed"))
            } catch (e: Exception) {
                _effects.emit(ProfileEffect.ShowToast("Error refreshing"))
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