package com.example.aprendejapones.presentation.screens.newpost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class NewPostViewModel : ViewModel() {

    private val _state = MutableStateFlow(NewPostState())
    val state: StateFlow<NewPostState> = _state

    private val _effects = MutableSharedFlow<NewPostEffect>()
    val effects: SharedFlow<NewPostEffect> = _effects

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
                // Aquí iría la lógica para guardar el post, por ejemplo usando un repositorio/local/remote.
                kotlinx.coroutines.delay(1200) // Simulación de publicando el post

                // Limpia el contenido, vuelve a la pantalla anterior y muestra mensaje
                _state.value = _state.value.copy(
                    content = "",
                    isPublishing = false
                )
                _effects.emit(NewPostEffect.ShowToast("¡Tu publicación ha sido publicada!"))
                _effects.emit(NewPostEffect.NavigateBack)
            } else {
                _effects.emit(NewPostEffect.ShowToast("El contenido no puede estar vacío"))
            }
        }
    }
}