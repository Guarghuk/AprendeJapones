package com.example.aprendejapones.presentation.screens.newpost

sealed class NewPostEvent {
    data class SelectCategory(val category: String) : NewPostEvent()
    data class UpdateContent(val content: String) : NewPostEvent()
    object PublishPost : NewPostEvent()
}