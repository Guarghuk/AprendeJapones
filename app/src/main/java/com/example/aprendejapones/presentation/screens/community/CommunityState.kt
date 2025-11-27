package com.example.aprendejapones.presentation.screens.community

import com.example.aprendejapones.domain.model.FirestorePost

/**
 * Estado de la pantalla Community
 */
data class CommunityState(
    val posts: List<FirestorePost> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

/**
 * Eventos de Community
 */
sealed class CommunityEvent {
    object LoadPosts : CommunityEvent()
    object RefreshPosts : CommunityEvent()
    data class CreatePost(val content: String, val category: String) : CommunityEvent()
    data class LikePost(val postId: String) : CommunityEvent()
    data class SavePost(val postId: String) : CommunityEvent()
    object DismissError : CommunityEvent()
}

/**
 * Efectos secundarios
 */
sealed class CommunityEffect {
    object NavigateToNewPost : CommunityEffect()
    data class NavigateToPostDetail(val postId: String) : CommunityEffect()
    data class ShowToast(val message: String) : CommunityEffect()
}