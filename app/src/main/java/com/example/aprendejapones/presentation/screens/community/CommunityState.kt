package com.example.aprendejapones.presentation.screens.community

import com.example.aprendejapones.domain.model.FirestoreComment
import com.example.aprendejapones.domain.model.FirestorePost

/**
 * Filter mode for community posts
 */
enum class PostFilterMode {
    ALL_POSTS,
    SAVED_POSTS
}

/**
 * Estado de la pantalla Community
 */
data class CommunityState(
    val posts: List<FirestorePost> = emptyList(),
    val savedPosts: List<FirestorePost> = emptyList(),
    val savedPostIds: Set<String> = emptySet(),
    val filterMode: PostFilterMode = PostFilterMode.ALL_POSTS,
    val isLoading: Boolean = true,
    val error: String? = null,
    // Post detail/comments state
    val selectedPost: FirestorePost? = null,
    val selectedPostComments: List<FirestoreComment> = emptyList(),
    val isRefreshing: Boolean = false,
    val isLoadingComments: Boolean = false,
    val showPostDetail: Boolean = false,
    val newCommentText: String = ""
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
    data class UnsavePost(val postId: String) : CommunityEvent()
    data class ToggleSavePost(val postId: String) : CommunityEvent()
    data class SelectPost(val post: FirestorePost) : CommunityEvent()
    object ClosePostDetail : CommunityEvent()
    data class UpdateNewCommentText(val text: String) : CommunityEvent()
    data class AddComment(val postId: String) : CommunityEvent()
    data class SetFilterMode(val mode: PostFilterMode) : CommunityEvent()
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