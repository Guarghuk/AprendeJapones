package com.example.aprendejapones.presentation.screens.newpost

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle. ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aprendejapones.domain.repository.CommunityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewPostViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val communityRepository: CommunityRepository  // ⭐ INYECTAR CommunityRepository
) : ViewModel() {

    companion object {
        private const val KEY_CONTENT = "new_post_content"
        private const val KEY_CATEGORY = "new_post_category"
    }

    private val _state = MutableStateFlow(
        NewPostState(
            content = savedStateHandle.get<String>(KEY_CONTENT) ?: "",
            selectedCategory = savedStateHandle. get<String>(KEY_CATEGORY) ?: "General",
            isPublishing = false
        )
    )
    val state: StateFlow<NewPostState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<NewPostEffect>()
    val effects: SharedFlow<NewPostEffect> = _effects.asSharedFlow()

    init {
        viewModelScope.launch {
            _state
                .map { it. content }
                .distinctUntilChanged()
                .collect { content ->
                    savedStateHandle[KEY_CONTENT] = content
                }
        }

        viewModelScope.launch {
            _state
                .map { it.selectedCategory }
                .distinctUntilChanged()
                . collect { category ->
                    savedStateHandle[KEY_CATEGORY] = category
                }
        }
    }

    fun onEvent(event: NewPostEvent) {
        when (event) {
            is NewPostEvent.SelectCategory -> {
                _state.value = _state.value.copy(selectedCategory = event.category)
            }
            is NewPostEvent.UpdateContent -> {
                if (event.content.length <= 500) {
                    _state.value = _state.value. copy(content = event.content)
                }
            }
            NewPostEvent.PublishPost -> {
                publishPost()
            }
        }
    }

    private fun publishPost() {
        viewModelScope.launch {
            val content = _state.value.content.trim()
            if (content.isEmpty()) {
                _effects.emit(NewPostEffect.ShowToast("El contenido no puede estar vacío"))
                return@launch
            }

            _state.value = _state.value.copy(isPublishing = true)

            // ⭐ USAR CommunityRepository en lugar de simulación
            android.util.Log.d("NewPostViewModel", "Publishing post: $content, category: ${_state.value.selectedCategory}")

            val result = communityRepository.createPost(
                content = content,
                category = _state.value.selectedCategory
            )

            result.onSuccess {
                android.util.Log.d("NewPostViewModel", "Post published successfully")

                // Limpiar estado
                savedStateHandle[KEY_CONTENT] = ""
                savedStateHandle[KEY_CATEGORY] = "General"

                _state.value = _state.value.copy(
                    content = "",
                    selectedCategory = "General",
                    isPublishing = false
                )

                _effects.emit(NewPostEffect. ShowToast("¡Publicación creada exitosamente!"))
                _effects.emit(NewPostEffect.NavigateBack)

            }.onFailure { error ->
                android.util.Log.e("NewPostViewModel", "Error publishing post", error)

                _state.value = _state.value.copy(isPublishing = false)
                _effects.emit(NewPostEffect.ShowToast("Error al publicar: ${error. message}"))
            }
        }
    }
}