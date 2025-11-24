package com.example.aprendejapones.presentation.screens.community

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class CommunityViewModel @Inject constructor(): ViewModel() {

    private val _state = MutableStateFlow(CommunityState())
    val state: StateFlow<CommunityState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<CommunityEffect>()
    val effects: SharedFlow<CommunityEffect> = _effects.asSharedFlow()

    init {
        loadPosts()
    }

    fun onEvent(event: CommunityEvent) {
        when (event) {
            is CommunityEvent.LoadPosts -> loadPosts()
            is CommunityEvent.RefreshPosts -> refreshPosts()
            is CommunityEvent.CreateNewPost -> createNewPost()
            is CommunityEvent.LikePost -> likePost(event.postId)
            is CommunityEvent.SavePost -> savePost(event.postId)
            is CommunityEvent.DismissError -> dismissError()
        }
    }

    private fun loadPosts() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                delay(300)
                val posts = MockData.getMockPosts()

                _state.update {
                    it.copy(
                        posts = posts,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al cargar publicaciones: ${e.message}"
                    )
                }
            }
        }
    }

    private fun refreshPosts() {
        viewModelScope.launch {
            try {
                val posts = MockData.getMockPosts()
                _state.update { it.copy(posts = posts) }
                _effects.emit(CommunityEffect.ShowToast("Publicaciones actualizadas"))
            } catch (e: Exception) {
                _effects.emit(CommunityEffect.ShowToast("Error al actualizar"))
            }
        }
    }

    private fun createNewPost() {
        viewModelScope.launch {
            _effects.emit(CommunityEffect.NavigateToNewPost)
        }
    }

    private fun likePost(postId: String) {
        viewModelScope.launch {
            // TODO: Implementar lógica de like
            _effects.emit(CommunityEffect.ShowToast("¡Te gusta esta publicación!"))
        }
    }

    private fun savePost(postId: String) {
        viewModelScope.launch {
            // TODO: Implementar lógica de guardar
            _effects.emit(CommunityEffect.ShowToast("Publicación guardada"))
        }
    }

    private fun dismissError() {
        _state.update { it.copy(error = null) }
    }
}