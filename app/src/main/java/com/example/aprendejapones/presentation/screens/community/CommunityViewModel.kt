package com.example.aprendejapones.presentation.screens.community

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aprendejapones.domain.repository.AuthRepository
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
            is CommunityEvent.CreatePost -> createPost(event.content, event.category)
            is CommunityEvent.LikePost -> likePost(event.postId)
            // ... otros eventos
        }
    }

    private fun createPost(content: String, category: String) {
        viewModelScope.launch {
            val result = communityRepository.createPost(content, category)
            result.onFailure { error ->
                _effects.emit(CommunityEffect.ShowToast("Error: ${error.message}"))
            }
        }
    }
}