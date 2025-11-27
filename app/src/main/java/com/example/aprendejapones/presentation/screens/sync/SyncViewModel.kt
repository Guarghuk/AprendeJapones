package com.example.aprendejapones.presentation.screens.sync

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aprendejapones.domain.repository.ProgressRepository
import com.example.aprendejapones.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class SyncViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val firestoreUserRepository: FirestoreUserRepository,
    private val progressRepository: ProgressRepository,
    // ... otros repositorios
) : ViewModel() {

    fun onEvent(event: SyncEvent) {
        when (event) {
            is SyncEvent.StartSync -> startSync()
        }
    }

    private fun startSync() {
        viewModelScope.launch {
            _state.update { it.copy(isSyncing = true, syncProgress = 0f) }

            try {
                // 1. Sincronizar usuario
                syncUser()
                _state.update { it.copy(syncProgress = 0.25f) }

                // 2. Sincronizar progreso
                syncProgress()
                _state.update { it.copy(syncProgress = 0.5f) }

                // 3. Sincronizar logros
                syncAchievements()
                _state.update { it.copy(syncProgress = 0.75f) }

                // 4. Sincronizar lecciones
                syncLessons()
                _state.update { it.copy(syncProgress = 1f) }

                _state.update { it.copy(isSyncing = false) }
                _effects.emit(SyncEffect.ShowToast("Sincronización completada"))

            } catch (e: Exception) {
                _state.update { it.copy(isSyncing = false) }
                _effects.emit(SyncEffect.ShowToast("Error: ${e.message}"))
            }
        }
    }

    private suspend fun syncUser() {
        val localUser = userRepository.getCurrentUser() ?: return
        firestoreUserRepository.updateUserProfile(localUser.toFirestoreUser())
    }

    // ... otras funciones de sincronización
}