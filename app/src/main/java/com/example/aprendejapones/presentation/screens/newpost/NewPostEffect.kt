package com.example.aprendejapones.presentation.screens.newpost

sealed class NewPostEffect {
    object NavigateBack : NewPostEffect()
    data class ShowToast(val message: String) : NewPostEffect()
}