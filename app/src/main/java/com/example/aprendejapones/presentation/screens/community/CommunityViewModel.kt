package com.example.aprendejapones.presentation.screens.community

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aprendejapones.domain.repository.AuthRepository
import com.example.aprendejapones.domain.repository.CommunityRepository
import com.example.aprendejapones.utils.MockData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para CommunityScreen
 */
@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val communityRepository: CommunityRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CommunityState())
    val state: StateFlow<CommunityState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<CommunityEffect>()
    val effects: SharedFlow<CommunityEffect> = _effects.asSharedFlow()

    init {
        // ✅ Escuchar posts en tiempo real
        viewModelScope.launch {
            communityRepository.getPostsFlow()
                .catch { error ->
                    _state.update { it.copy(error = error.message) }
                }
                .collect { posts ->
                    _state.update { it.copy(posts = posts, isLoading = false) }
                }
        }
    }

    fun onEvent(event: CommunityEvent) {
        when (event) {
            is CommunityEvent.LoadPosts -> { /* Posts are loaded automatically via Flow */ }
            is CommunityEvent.CreatePost -> createPost(event.content, event.category)
            is CommunityEvent.LikePost -> likePost(event.postId)
            is CommunityEvent.SavePost -> savePost(event.postId)
            is CommunityEvent.RefreshPosts -> refreshPosts()
            is CommunityEvent.DismissError -> dismissError()
        }
    }

    private fun createPost(content: String, category: String) {
        viewModelScope.launch {
            val result = communityRepository.createPost(content, category)
            result.onFailure { error ->
                _effects.emit(CommunityEffect.ShowToast("Error: ${error.message}"))
            }
            result.onSuccess {
                _effects.emit(CommunityEffect.ShowToast("Post creado exitosamente"))
            }
        }
    }

    private fun likePost(postId: String) {
        viewModelScope.launch {
            val result = communityRepository.likePost(postId)
            result.onFailure { error ->
                _effects.emit(CommunityEffect.ShowToast("Error: ${error.message}"))
            }
        }
    }

    private fun refreshPosts() {
        // Posts are already refreshed automatically via Flow
        // This can be used for pull-to-refresh functionality
    }

    private fun savePost(postId: String) {
        // TODO: Implement save/bookmark functionality with Firestore
        // Future implementation:
        // 1. Add postId to user's saved posts collection in Firestore
        // 2. Update local state to reflect saved status
        viewModelScope.launch {
            _effects.emit(CommunityEffect.ShowToast("📌 Función en desarrollo - Próximamente"))
        }
    }

    private fun dismissError() {
        _state.update { it.copy(error = null) }
    }
}