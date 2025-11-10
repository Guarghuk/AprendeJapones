package com.example.aprendejapones.presentation.screens.newpost

data class NewPostState(
    val selectedCategory: String = "General",
    val content: String = "",
    val isPublishing: Boolean = false
)