package com.example.aprendejapones.presentation.screens.community

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aprendejapones.domain.repository.AuthRepository
import com.example.aprendejapones.domain.repository.CommunityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
        loadPosts()
        loadUserInteractions()
    }

    private fun loadPosts() {
        viewModelScope.launch {
            communityRepository.getPostsFlow()
                .catch { error ->
                    android.util.Log.e("CommunityViewModel", "Error loading posts", error)
                    _state.update { it.copy(error = error.message, isLoading = false) }
                }
                .collect { posts ->
                    android.util.Log.d("CommunityViewModel", "Loaded ${posts.size} posts")
                    _state.update { it. copy(posts = posts, isLoading = false, isRefreshing = false) }
                }
        }
    }

    private fun loadUserInteractions() {
        viewModelScope.launch {
            val userId = authRepository.getCurrentUserId()
            if (userId != null) {
                // Load liked posts
                launch {
                    communityRepository. getUserLikedPostsFlow(userId)
                        .collect { likedIds ->
                            _state.update { it.copy(likedPostIds = likedIds. toSet()) }
                        }
                }

                // Load saved posts
                launch {
                    communityRepository.getUserSavedPostsFlow(userId)
                        .collect { savedIds ->
                            _state.update { it.copy(savedPostIds = savedIds.toSet()) }
                        }
                }
            }
        }
    }

    fun onEvent(event: CommunityEvent) {
        when (event) {
            is CommunityEvent.LoadPosts -> loadPosts()
            is CommunityEvent.RefreshPosts -> refreshPosts()
            is CommunityEvent.CreatePost -> createPost(event.content, event.category)
            is CommunityEvent. LikePost -> toggleLike(event.postId)
            is CommunityEvent. SavePost -> toggleSave(event.postId)
            is CommunityEvent. FilterByCategory -> filterByCategory(event.category)
            is CommunityEvent.DismissError -> dismissError()
        }
    }

    private fun refreshPosts() {
        _state.update { it.copy(isRefreshing = true) }
        loadPosts()
    }

    private fun createPost(content: String, category: String) {
        viewModelScope.launch {
            android.util.Log.d("CommunityViewModel", "Creating post: content=$content, category=$category")

            val result = communityRepository.createPost(content, category)
            result.onFailure { error ->
                android. util.Log.e("CommunityViewModel", "Failed to create post", error)
                _effects.emit(CommunityEffect. ShowToast("Error: ${error.message}"))
            }
            result.onSuccess {
                android.util.Log. d("CommunityViewModel", "Post created successfully")
                _effects. emit(CommunityEffect.ShowToast("Post creado exitosamente"))
            }
        }
    }

    private fun toggleLike(postId: String) {
        viewModelScope.launch {
            val isLiked = postId in _state.value.likedPostIds

            android.util.Log.d("CommunityViewModel", "Toggle like for post: $postId, currently liked: $isLiked")

            val result = if (isLiked) {
                communityRepository.unlikePost(postId)
            } else {
                communityRepository.likePost(postId)
            }

            result.onFailure { error ->
                android.util.Log.e("CommunityViewModel", "Error toggling like", error)
                _effects.emit(CommunityEffect.ShowToast("Error: ${error.message}"))
            }
        }
    }

    private fun toggleSave(postId: String) {
        viewModelScope. launch {
            val isSaved = postId in _state.value.savedPostIds

            android.util.Log.d("CommunityViewModel", "Toggle save for post: $postId, currently saved: $isSaved")

            val result = if (isSaved) {
                communityRepository.unsavePost(postId)
            } else {
                communityRepository.savePost(postId)
            }

            result.onFailure { error ->
                android.util. Log.e("CommunityViewModel", "Error toggling save", error)
                _effects.emit(CommunityEffect.ShowToast("Error: ${error.message}"))
            }
            result.onSuccess {
                val message = if (isSaved) "Eliminado de guardados" else "Guardado exitosamente"
                _effects.emit(CommunityEffect.ShowToast(message))
            }
        }
    }

    private fun filterByCategory(category: String) {
        _state.update { it.copy(selectedCategory = category) }
    }

    private fun dismissError() {
        _state.update { it.copy(error = null) }
    }
}