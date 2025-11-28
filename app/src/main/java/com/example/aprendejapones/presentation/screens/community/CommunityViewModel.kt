package com.example.aprendejapones.presentation.screens.community

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aprendejapones.domain.model.FirestorePost
import com.example.aprendejapones.domain.repository.AuthRepository
import com.example.aprendejapones.domain.repository.CommunityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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

    private var commentsJob: Job? = null

    init {
        // Listen to posts in real-time
        viewModelScope.launch {
            communityRepository.getPostsFlow()
                .catch { error ->
                    _state.update { it.copy(error = error.message) }
                }
                .collect { posts ->
                    _state.update { it.copy(posts = posts, isLoading = false) }
                }
        }

        // Listen to saved posts in real-time
        viewModelScope.launch {
            communityRepository.getSavedPostsFlow()
                .catch { /* Ignore errors for saved posts */ }
                .collect { savedPosts ->
                    _state.update { it.copy(savedPosts = savedPosts) }
                }
        }

        // Listen to saved post IDs in real-time
        viewModelScope.launch {
            communityRepository.getSavedPostIdsFlow()
                .catch { /* Ignore errors */ }
                .collect { savedPostIds ->
                    _state.update { it.copy(savedPostIds = savedPostIds) }
                }
        }
    }

    fun onEvent(event: CommunityEvent) {
        when (event) {
            is CommunityEvent.LoadPosts -> { /* Posts are loaded automatically via Flow */ }
            is CommunityEvent.CreatePost -> createPost(event.content, event.category)
            is CommunityEvent.LikePost -> likePost(event.postId)
            is CommunityEvent.SavePost -> savePost(event.postId)
            is CommunityEvent.UnsavePost -> unsavePost(event.postId)
            is CommunityEvent.ToggleSavePost -> toggleSavePost(event.postId)
            is CommunityEvent.RefreshPosts -> refreshPosts()
            is CommunityEvent.DismissError -> dismissError()
            is CommunityEvent.SelectPost -> selectPost(event.post)
            is CommunityEvent.ClosePostDetail -> closePostDetail()
            is CommunityEvent.UpdateNewCommentText -> updateNewCommentText(event.text)
            is CommunityEvent.AddComment -> addComment(event.postId)
            is CommunityEvent.SetFilterMode -> setFilterMode(event.mode)
        }
    }

    private fun createPost(content: String, category: String) {
        viewModelScope.launch {
            android.util.Log.d("CommunityViewModel", "Creating post: content=$content, category=$category")
            
            val result = communityRepository.createPost(content, category)
            result.onFailure { error ->
                android.util.Log.e("CommunityViewModel", "Failed to create post", error)
                _effects.emit(CommunityEffect.ShowToast("Error: ${error.message}"))
            }
            result.onSuccess {
                android.util.Log.d("CommunityViewModel", "Post created successfully")
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
    }

    private fun savePost(postId: String) {
        viewModelScope.launch {
            val result = communityRepository.savePost(postId)
            result.onSuccess {
                _effects.emit(CommunityEffect.ShowToast("📌 Post guardado"))
            }
            result.onFailure { error ->
                _effects.emit(CommunityEffect.ShowToast("Error: ${error.message}"))
            }
        }
    }

    private fun unsavePost(postId: String) {
        viewModelScope.launch {
            val result = communityRepository.unsavePost(postId)
            result.onSuccess {
                _effects.emit(CommunityEffect.ShowToast("Post eliminado de guardados"))
            }
            result.onFailure { error ->
                _effects.emit(CommunityEffect.ShowToast("Error: ${error.message}"))
            }
        }
    }

    private fun toggleSavePost(postId: String) {
        viewModelScope.launch {
            val isSaved = _state.value.savedPostIds.contains(postId)
            if (isSaved) {
                unsavePost(postId)
            } else {
                savePost(postId)
            }
        }
    }

    private fun selectPost(post: FirestorePost) {
        _state.update { 
            it.copy(
                selectedPost = post, 
                showPostDetail = true,
                isLoadingComments = true,
                newCommentText = ""
            ) 
        }
        
        // Cancel previous comments listener
        commentsJob?.cancel()
        
        // Load comments for this post
        commentsJob = viewModelScope.launch {
            communityRepository.getCommentsFlow(post.id)
                .catch { error ->
                    _state.update { it.copy(isLoadingComments = false) }
                }
                .collect { comments ->
                    _state.update { 
                        it.copy(
                            selectedPostComments = comments,
                            isLoadingComments = false
                        ) 
                    }
                }
        }
    }

    private fun closePostDetail() {
        commentsJob?.cancel()
        _state.update { 
            it.copy(
                selectedPost = null, 
                showPostDetail = false,
                selectedPostComments = emptyList(),
                newCommentText = ""
            ) 
        }
    }

    private fun updateNewCommentText(text: String) {
        _state.update { it.copy(newCommentText = text) }
    }

    private fun addComment(postId: String) {
        val commentText = _state.value.newCommentText.trim()
        if (commentText.isEmpty()) return

        viewModelScope.launch {
            val result = communityRepository.addComment(postId, commentText)
            result.onSuccess {
                _state.update { it.copy(newCommentText = "") }
                _effects.emit(CommunityEffect.ShowToast("Comentario añadido"))
            }
            result.onFailure { error ->
                _effects.emit(CommunityEffect.ShowToast("Error: ${error.message}"))
            }
        }
    }

    private fun setFilterMode(mode: PostFilterMode) {
        _state.update { it.copy(filterMode = mode) }
    }

    private fun dismissError() {
        _state.update { it.copy(error = null) }
    }
}