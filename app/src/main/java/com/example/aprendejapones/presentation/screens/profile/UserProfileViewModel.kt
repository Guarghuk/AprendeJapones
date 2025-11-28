package com.example.aprendejapones.presentation.screens.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.aprendejapones.domain.model.FirestorePost
import com.example.aprendejapones.domain.model.FirestoreUser
import com.example.aprendejapones.domain.repository.CommunityRepository
import com.example.aprendejapones.domain.repository.FirestoreUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.aprendejapones.presentation.screens.profile.UserProfileState

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val firestoreUserRepository: FirestoreUserRepository,
    private val communityRepository: CommunityRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val userId: String = savedStateHandle.get<String>("userId") ?: ""

    private val _state = MutableStateFlow(UserProfileState())
    val state: StateFlow<UserProfileState> = _state.asStateFlow()

    init {
        loadUserProfile(userId)
        loadUserPosts()
    }

    fun loadUserProfile(userId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            firestoreUserRepository.getUserProfileFlow(userId)
                .catch { error ->
                    android.util.Log.e("UserProfileVM", "Error loading user", error)
                    _state.update { it.copy(error = error.message, isLoading = false) }
                }
                .collect { user ->
                    _state.update { it.copy(user = user, isLoading = false) }
                }
        }
    }

    private fun loadUserPosts() {
        viewModelScope.launch {
            communityRepository.getUserPostsFlow(userId)
                .catch { error ->
                    android.util.Log.e("UserProfileVM", "Error loading posts", error)
                }
                .collect { posts ->
                    _state.update { it.copy(userPosts = posts) }
                }
        }
    }
}
