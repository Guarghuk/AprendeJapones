package com.example.aprendejapones.presentation.screens.sync

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aprendejapones.data.mapper.UserMapper.toFirestoreUser
import com.example.aprendejapones.domain.repository.AchievementRepository
import com.example.aprendejapones.domain.repository.FirestoreUserRepository
import com.example.aprendejapones.domain.repository.LessonRepository
import com.example.aprendejapones.domain.repository.ProgressRepository
import com.example.aprendejapones.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SyncViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val firestoreUserRepository: FirestoreUserRepository,
    private val progressRepository: ProgressRepository,
    private val achievementRepository: AchievementRepository,
    private val lessonRepository: LessonRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SyncState())
    val state: StateFlow<SyncState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<SyncEffect>()
    val effects: SharedFlow<SyncEffect> = _effects.asSharedFlow()

    init {
        loadInitialData()
    }

    fun onEvent(event: SyncEvent) {
        when (event) {
            is SyncEvent.StartSync -> startSync()
            is SyncEvent.CancelSync -> cancelSync()
            is SyncEvent.DismissError -> dismissError()
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                // Count local data
                val localCount = countLocalData()
                _state.update { it.copy(localDataCount = localCount) }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            }
        }
    }

    private suspend fun countLocalData(): Int {
        var count = 0
        // Count user
        userRepository.getCurrentUser()?.let { count++ }
        // Count achievements
        count += achievementRepository.getUserAchievements().size
        return count
    }

    private fun startSync() {
        viewModelScope.launch {
            _state.update { it.copy(isSyncing = true, syncProgress = 0f, error = null) }

            try {
                // 1. Sync user
                syncUser()
                _state.update { it.copy(syncProgress = 0.25f) }

                // 2. Sync progress
                syncProgress()
                _state.update { it.copy(syncProgress = 0.5f) }

                // 3. Sync achievements
                syncAchievements()
                _state.update { it.copy(syncProgress = 0.75f) }

                // 4. Sync lessons
                syncLessons()
                _state.update { it.copy(syncProgress = 1f) }

                // Update last sync time
                val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                val lastSyncTime = dateFormat.format(Date())

                _state.update { 
                    it.copy(
                        isSyncing = false,
                        lastSyncTime = lastSyncTime
                    ) 
                }
                _effects.emit(SyncEffect.ShowToast("Sincronización completada"))
                _effects.emit(SyncEffect.SyncCompleted)

            } catch (e: Exception) {
                _state.update { 
                    it.copy(
                        isSyncing = false,
                        error = e.message
                    ) 
                }
                _effects.emit(SyncEffect.SyncFailed(e.message ?: "Error desconocido"))
            }
        }
    }

    private fun cancelSync() {
        _state.update { it.copy(isSyncing = false, syncProgress = 0f) }
    }

    private fun dismissError() {
        _state.update { it.copy(error = null) }
    }

    private suspend fun syncUser() {
        val localUser = userRepository.getCurrentUser() ?: return
        firestoreUserRepository.updateUserProfile(localUser.toFirestoreUser())
    }

    private suspend fun syncProgress() {
        // TODO: Implement progress sync to Firestore
        // Current implementation fetches data but doesn't upload to Firestore
        // Future implementation should:
        // 1. Fetch local progress: val progressList = progressRepository.getAllProgress()
        // 2. Upload to Firestore: firestoreUserRepository.syncProgressData(userId, progressList)
        @Suppress("UNUSED_VARIABLE")
        val progressList = progressRepository.getAllProgress()
    }

    private suspend fun syncAchievements() {
        // TODO: Implement achievements sync to Firestore
        // Current implementation fetches data but doesn't upload to Firestore
        // Future implementation should:
        // 1. Fetch local achievements: val achievements = achievementRepository.getUserAchievements()
        // 2. Upload to Firestore: firestoreUserRepository.syncAchievementsData(userId, achievements)
        @Suppress("UNUSED_VARIABLE")
        val achievements = achievementRepository.getUserAchievements()
    }

    private suspend fun syncLessons() {
        // TODO: Implement lessons sync to Firestore
        // Current implementation fetches data but doesn't upload to Firestore
        // Future implementation should:
        // 1. Fetch local stats: val lessonStats = lessonRepository.getLessonStats()
        // 2. Upload to Firestore: firestoreUserRepository.syncLessonStats(userId, lessonStats)
        @Suppress("UNUSED_VARIABLE")
        val lessonStats = lessonRepository.getLessonStats()
    }
}