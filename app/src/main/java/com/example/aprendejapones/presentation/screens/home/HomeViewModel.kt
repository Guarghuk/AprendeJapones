package com.example.aprendejapones.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aprendejapones.domain.repository.AchievementRepository
import com.example.aprendejapones.domain.repository.LessonRepository
import com.example.aprendejapones.domain.repository.ProgressRepository
import com.example.aprendejapones.domain.repository.UserRepository
import com.example.aprendejapones.utils.MockData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel for Home Screen
 * Now uses real repositories instead of mock data
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val lessonRepository: LessonRepository,
    private val achievementRepository: AchievementRepository,
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<HomeEffect>()
    val effects: SharedFlow<HomeEffect> = _effects.asSharedFlow()

    init {
        initializeUser()
        loadInitialData()
    }

    /**
     * Initialize or get existing user
     */
    private fun initializeUser() {
        viewModelScope.launch (Dispatchers.IO) {
            try {
                // Get or create user with UUID
                val user = userRepository.getOrCreateUser()

                // Initialize default data
                progressRepository.initializeDefaultProgress()
                achievementRepository.initializeDefaultAchievements()
                lessonRepository.initializeTodayChallenge()
                withContext(Dispatchers.Main) { /* Update state on main */ }

            } catch (e: Exception) {
                _state.update {
                    it.copy(error = "Error initializing user: ${e.message}")
                }
            }
        }
    }

    /**
     * Load all home screen data
     */
    private fun loadInitialData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                // Collect user flow with debounce
                userRepository.getCurrentUserFlow()
                    .debounce(300)  // Add this import: import kotlinx.coroutines.flow.debounce
                    .collect { user ->
                        _state.update { it.copy(user = user) }
                    }

                // Same for daily challenge
                lessonRepository.getTodayChallengeFlow()
                    .debounce(300)
                    .collect { challenge ->
                        _state.update { it.copy(dailyChallenge = challenge) }
                    }

                // Static data (unchanged)
                val kitsuneMessage = MockData.getMockKitsuneMessage()
                val lessonFunctions = MockData.getMockLessonFunctions()

                _state.update {
                    it.copy(
                        kitsuneMessage = kitsuneMessage,
                        lessonFunctions = lessonFunctions,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Error loading data: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * Handle UI events
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

    private fun refreshData() {
        viewModelScope.launch {
            try {
                lessonRepository.initializeTodayChallenge()
                _effects.emit(HomeEffect.ShowToast("Data refreshed"))
            } catch (e: Exception) {
                _effects.emit(HomeEffect.ShowToast("Error refreshing"))
            }
        }
    }

    private fun handleFunctionSelection(functionName: String) {
        viewModelScope.launch {
            _effects.emit(HomeEffect.NavigateToLesson(functionName))
        }
    }

    private fun updateChallengeProgress() {
        viewModelScope.launch {
            try {
                lessonRepository.updateChallengeProgress()

                val challenge = _state.value.dailyChallenge
                if (challenge?.isCompleted == true) {
                    // Award XP for completing daily challenge
                    userRepository.addXP(challenge.rewardXP)
                    _effects.emit(
                        HomeEffect.ShowToast("Challenge completed! +${challenge.rewardXP} XP")
                    )
                }
            } catch (e: Exception) {
                _effects.emit(HomeEffect.ShowToast("Error updating challenge"))
            }
        }
    }


    private fun dismissError() {
        _state.update { it.copy(error = null) }
    }
}