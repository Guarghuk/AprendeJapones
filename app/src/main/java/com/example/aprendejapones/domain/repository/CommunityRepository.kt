package com.example.aprendejapones.domain.repository

import com.example.aprendejapones.domain.model.FirestoreComment
import com.example.aprendejapones.domain.model.FirestorePost
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for community features (posts, comments, likes)
 */
interface CommunityRepository {
    /**
     * Get real-time stream of posts
     */
    fun getPostsFlow(): Flow<List<FirestorePost>>

    /**
     * Create a new post
     */
    suspend fun createPost(content: String, category: String): Result<Unit>

    /**
     * Like a post
     */
    suspend fun likePost(postId: String): Result<Unit>

    /**
     * Unlike a post
     */
    suspend fun unlikePost(postId: String): Result<Unit>

    /**
     * Add a comment to a post
     */
    suspend fun addComment(postId: String, content: String): Result<Unit>

    /**
     * Get comments for a post
     */
    fun getCommentsFlow(postId: String): Flow<List<FirestoreComment>>

    /**
     * Check if user has liked a post
     */
    suspend fun hasUserLikedPost(postId: String): Boolean

    /**
     * Delete a post (only owner can delete)
     */
    suspend fun deletePost(postId: String): Result<Unit>

    /**
     * Save/bookmark a post
     */
    suspend fun savePost(postId: String): Result<Unit>

    /**
     * Unsave/remove bookmark from a post
     */
    suspend fun unsavePost(postId: String): Result<Unit>

    /**
     * Check if user has saved a post
     */
    suspend fun hasUserSavedPost(postId: String): Boolean

    /**
     * Get saved posts for current user
     */
    fun getSavedPostsFlow(): Flow<List<FirestorePost>>

    /**
     * Get set of saved post IDs for current user
     */
    fun getSavedPostIdsFlow(): Flow<Set<String>>

    /**
     * Get set of post IDs that the user has liked
     */
    fun getLikedPostIdsFlow(): Flow<Set<String>>

    fun getUserPostsFlow(userId: String): Flow<List<FirestorePost>>
}
