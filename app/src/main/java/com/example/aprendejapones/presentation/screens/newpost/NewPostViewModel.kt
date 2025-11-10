package com.example.aprendejapones.presentation.screens.newpost

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NewPostViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Keys para SavedStateHandle
    companion object {
        private const val KEY_CONTENT = "new_post_content"
        private const val KEY_CATEGORY = "new_post_category"
    }

    // State restaurado desde SavedStateHandle
    private val _state = MutableStateFlow(
        NewPostState(
            content = savedStateHandle.get<String>(KEY_CONTENT) ?: "",
            selectedCategory = savedStateHandle.get<String>(KEY_CATEGORY) ?: "General",
            isPublishing = false
        )
    )
    val state: StateFlow<NewPostState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<NewPostEffect>()
    val effects: SharedFlow<NewPostEffect> = _effects.asSharedFlow()

    init {
        // Observa cambios en el estado y guárdalos automáticamente
        viewModelScope.launch {
            _state
                .map { it.content }
                .distinctUntilChanged()
                .collect { content ->
                    savedStateHandle[KEY_CONTENT] = content
                }
        }

        viewModelScope.launch {
            _state
                .map { it.selectedCategory }
                .distinctUntilChanged()
                .collect { category ->
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
                    _state.value = _state.value.copy(content = event.content)
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
            if (content.isNotEmpty()) {
                _state.value = _state.value.copy(isPublishing = true)

                try {
                    // Simulación de publicación
                    kotlinx.coroutines.delay(1200)

                    // Limpia el contenido guardado en SavedStateHandle
                    savedStateHandle[KEY_CONTENT] = ""
                    savedStateHandle[KEY_CATEGORY] = "General"

                    // Actualiza el estado
                    _state.value = _state.value.copy(
                        content = "",
                        selectedCategory = "General",
                        isPublishing = false
                    )

                    _effects.emit(NewPostEffect.ShowToast("¡Tu publicación ha sido publicada!"))
                    _effects.emit(NewPostEffect.NavigateBack)
                } catch (e: Exception) {
                    _state.value = _state.value.copy(isPublishing = false)
                    _effects.emit(NewPostEffect.ShowToast("Error al publicar: ${e.message}"))
                }
            } else {
                _effects.emit(NewPostEffect.ShowToast("El contenido no puede estar vacío"))
            }
        }
    }
}