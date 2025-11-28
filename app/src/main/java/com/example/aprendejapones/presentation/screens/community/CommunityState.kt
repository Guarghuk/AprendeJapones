package com.example.aprendejapones.presentation.screens.community

import com.example.aprendejapones.domain.model.FirestorePost

/**
 * Estado de la pantalla Community
 */
data class CommunityState(
    val posts: List<FirestorePost> = emptyList(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val likedPostIds: Set<String> = emptySet(),
    val savedPostIds: Set<String> = emptySet(),
    val selectedCategory: String = "Todos"
) {
    val filteredPosts: List<FirestorePost>
        get() = if (selectedCategory == "Todos") {
            posts
        } else {
            posts.filter { it.category == selectedCategory }
        }
}

/**
 * Eventos de Community
 */
sealed class CommunityEvent {
    object LoadPosts : CommunityEvent()
    object RefreshPosts : CommunityEvent()
    data class CreatePost(val content: String, val category: String) : CommunityEvent()
    data class LikePost(val postId: String) : CommunityEvent()
    data class SavePost(val postId: String) : CommunityEvent()
    data class FilterByCategory(val category: String) : CommunityEvent()
    object DismissError : CommunityEvent()
}

/**
 * Efectos secundarios
 */
sealed class CommunityEffect {
    object NavigateToNewPost : CommunityEffect()
    data class NavigateToPostDetail(val postId: String) : CommunityEffect()
    data class NavigateToUserProfile(val userId: String) : CommunityEffect()
    data class ShowToast(val message: String) : CommunityEffect()
}