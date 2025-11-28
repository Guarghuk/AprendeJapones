package com.example.aprendejapones.data.repository

import com.example.aprendejapones.domain.model.FirestoreComment
import com.example.aprendejapones.domain.model.FirestorePost
import com.example.aprendejapones.domain.model.FirestoreSavedPost
import com.example.aprendejapones.domain.model.FirestoreUser
import com.example.aprendejapones.domain.repository.AuthRepository
import com.example.aprendejapones.domain.repository.CommunityRepository
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreCommunityRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val authRepository: AuthRepository
) : CommunityRepository {

    override fun getPostsFlow(): Flow<List<FirestorePost>> = callbackFlow {
        val listener = firestore.collection("posts")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val posts = snapshot?.documents?.mapNotNull {
                    it.toObject(FirestorePost::class.java)?.copy(id = it.id)
                } ?: emptyList()

                trySend(posts)
            }

        awaitClose { listener.remove() }
    }

    override suspend fun createPost(content: String, category: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val userId = authRepository.getCurrentUserId()
                    ?: return@withContext Result.failure(Exception("Not logged in"))

                val user = getUserProfile(userId)
                    ?: return@withContext Result.failure(Exception("User profile not found"))

                val post = FirestorePost(
                    authorId = userId,
                    authorName = user.username,
                    //authorPhotoUrl = user.photoUrl,
                    content = content,
                    category = category,
                    //createdAt = System.currentTimeMillis()
                )

                firestore.collection("posts").add(post).await()

                android.util.Log.d("FirestoreCommunity", "Post created successfully: $content")

                Result.success(Unit)
            } catch (e: Exception) {
                android.util.Log.e("FirestoreCommunity", "Error creating post", e)
                Result.failure(e)
            }
        }
    }

    override suspend fun likePost(postId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val userId = authRepository.getCurrentUserId()
                    ?: return@withContext Result.failure(Exception("Not logged in"))

                // Verificar si el usuario ya dio like
                val alreadyLiked = hasUserLikedPost(postId)
                if (alreadyLiked) {
                    android.util.Log.d("FirestoreCommunity", "User already liked this post: $postId")
                    return@withContext Result.success(Unit)
                }

                // Create like in subcollection: /posts/{postId}/likes/{userId}
                // Document ID is the user's UID for easy lookup and security rules
                val likeData = mapOf(
                    "id_usuario" to userId,
                    "fecha_reaccion" to com.google.firebase.Timestamp.now()
                )
                firestore.collection("posts").document(postId)
                    .collection("likes").document(userId)
                    .set(likeData).await()

                // TODO: Remove client-side counter update once Cloud Functions are deployed
                // and verified to be working. Currently kept for backward compatibility
                // during migration. Cloud Functions will handle contador_likes automatically.
                firestore.collection("posts").document(postId)
                    .update("likesCount", FieldValue.increment(1))
                    .await()

                android.util.Log.d("FirestoreCommunity", "Post liked: $postId")
                Result.success(Unit)
            } catch (e: Exception) {
                android.util.Log.e("FirestoreCommunity", "Error liking post", e)
                Result.failure(e)
            }
        }
    }

    override suspend fun unlikePost(postId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val userId = authRepository.getCurrentUserId()
                    ?: return@withContext Result.failure(Exception("Not logged in"))

                // Delete like from subcollection: /posts/{postId}/likes/{userId}
                firestore.collection("posts").document(postId)
                    .collection("likes").document(userId)
                    .delete().await()

                // TODO: Remove client-side counter update once Cloud Functions are deployed
                // and verified to be working. Currently kept for backward compatibility
                // during migration. Cloud Functions will handle contador_likes automatically.
                firestore.collection("posts").document(postId)
                    .update("likesCount", FieldValue.increment(-1))
                    .await()

                android.util.Log.d("FirestoreCommunity", "Post unliked: $postId")
                Result.success(Unit)
            } catch (e: Exception) {
                android.util.Log.e("FirestoreCommunity", "Error unliking post", e)
                Result.failure(e)
            }
        }
    }

    override suspend fun addComment(postId: String, content: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val userId = authRepository.getCurrentUserId()
                    ?: return@withContext Result.failure(Exception("Not logged in"))

                val user = getUserProfile(userId)
                    ?: return@withContext Result.failure(Exception("User profile not found"))

                // Create comment in subcollection: /posts/{postId}/comments/{commentId}
                val commentData = mapOf(
                    "id_autor" to userId,
                    "nombre_autor" to user.username,
                    "foto_autor_url" to user.photoUrl,
                    "contenido" to content,
                    "fecha_publicacion" to com.google.firebase.Timestamp.now()
                )

                firestore.collection("posts").document(postId)
                    .collection("comments").add(commentData).await()

                // TODO: Remove client-side counter update once Cloud Functions are deployed
                // and verified to be working. Currently kept for backward compatibility
                // during migration. Cloud Functions will handle contador_comentarios automatically.
                firestore.collection("posts").document(postId)
                    .update("commentsCount", FieldValue.increment(1))
                    .await()

                android.util.Log.d("FirestoreCommunity", "Comment added to post: $postId")
                Result.success(Unit)
            } catch (e: Exception) {
                android.util.Log.e("FirestoreCommunity", "Error adding comment", e)
                Result.failure(e)
            }
        }
    }

    override fun getCommentsFlow(postId: String): Flow<List<FirestoreComment>> = callbackFlow {
        // Read comments from subcollection: /posts/{postId}/comments
        val listener = firestore.collection("posts").document(postId)
            .collection("comments")
            .orderBy("fecha_publicacion", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val comments = snapshot?.documents?.mapNotNull { doc ->
                    // Map from new schema to FirestoreComment model
                    val data = doc.data ?: return@mapNotNull null
                    FirestoreComment(
                        id = doc.id,
                        postId = postId,
                        authorId = data["id_autor"] as? String ?: "",
                        authorName = data["nombre_autor"] as? String ?: "",
                        authorPhotoUrl = data["foto_autor_url"] as? String,
                        content = data["contenido"] as? String ?: "",
                        createdAt = (data["fecha_publicacion"] as? com.google.firebase.Timestamp)?.toDate()?.time 
                            ?: System.currentTimeMillis()
                    )
                } ?: emptyList()

                trySend(comments)
            }

        awaitClose { listener.remove() }
    }

    override suspend fun hasUserLikedPost(postId: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val userId = authRepository.getCurrentUserId() ?: return@withContext false
            // Check like in subcollection: /posts/{postId}/likes/{userId}
            val doc = firestore.collection("posts").document(postId)
                .collection("likes").document(userId)
                .get().await()
            doc.exists()
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun deletePost(postId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val userId = authRepository.getCurrentUserId()
                    ?: return@withContext Result.failure(Exception("Not logged in"))

                // Verify ownership
                val post = firestore.collection("posts").document(postId).get().await()
                val postAuthorId = post.getString("authorId") ?: post.getString("id_autor")

                if (postAuthorId != userId) {
                    return@withContext Result.failure(Exception("Not authorized"))
                }

                // Delete likes from subcollection: /posts/{postId}/likes
                val likesSubcollection = firestore.collection("posts").document(postId)
                    .collection("likes").get().await()
                for (like in likesSubcollection.documents) {
                    like.reference.delete().await()
                }

                // Delete comments from subcollection: /posts/{postId}/comments
                val commentsSubcollection = firestore.collection("posts").document(postId)
                    .collection("comments").get().await()
                for (comment in commentsSubcollection.documents) {
                    comment.reference.delete().await()
                }

                // Delete post
                firestore.collection("posts").document(postId).delete().await()

                // Delete associated saved posts
                val savedPosts = firestore.collection("saved_posts")
                    .whereEqualTo("postId", postId)
                    .get()
                    .await()
                
                for (savedPost in savedPosts.documents) {
                    savedPost.reference.delete().await()
                }

                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun savePost(postId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val userId = authRepository.getCurrentUserId()
                    ?: return@withContext Result.failure(Exception("Not logged in"))

                val savedPostId = "${userId}_${postId}"

                // Create saved post record
                val savedPost = FirestoreSavedPost(userId, postId, System.currentTimeMillis())
                firestore.collection("saved_posts").document(savedPostId).set(savedPost).await()

                // Increment saves counter on post
                firestore.collection("posts").document(postId)
                    .update("savesCount", FieldValue.increment(1))
                    .await()

                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun unsavePost(postId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val userId = authRepository.getCurrentUserId()
                    ?: return@withContext Result.failure(Exception("Not logged in"))

                val savedPostId = "${userId}_${postId}"

                // Check if the save record exists before deleting
                val savedPostDoc = firestore.collection("saved_posts").document(savedPostId).get().await()
                if (!savedPostDoc.exists()) {
                    return@withContext Result.success(Unit) // Already unsaved
                }

                // Delete saved post record
                firestore.collection("saved_posts").document(savedPostId).delete().await()

                // Decrement saves counter on post (only if current count > 0)
                val postDoc = firestore.collection("posts").document(postId).get().await()
                val currentSavesCount = postDoc.getLong("savesCount") ?: 0
                if (currentSavesCount > 0) {
                    firestore.collection("posts").document(postId)
                        .update("savesCount", FieldValue.increment(-1))
                        .await()
                }

                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun hasUserSavedPost(postId: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val userId = authRepository.getCurrentUserId() ?: return@withContext false
            val savedPostId = "${userId}_${postId}"
            val doc = firestore.collection("saved_posts").document(savedPostId).get().await()
            doc.exists()
        } catch (e: Exception) {
            false
        }
    }

    override fun getSavedPostsFlow(): Flow<List<FirestorePost>> = callbackFlow {
        val userId = authRepository.getCurrentUserId()
        if (userId == null) {
            trySend(emptyList())
            awaitClose { }
            return@callbackFlow
        }

        val listener = firestore.collection("saved_posts")
            .whereEqualTo("userId", userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                val postIds = snapshot?.documents?.mapNotNull {
                    it.getString("postId")
                } ?: emptyList()

                if (postIds.isEmpty()) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                // Fetch posts in batches of 10 (Firestore whereIn limit)
                val allPosts = mutableListOf<FirestorePost>()
                val batches = postIds.chunked(10)
                var completedBatches = 0

                for (batch in batches) {
                    firestore.collection("posts")
                        .whereIn("__name__", batch)
                        .get()
                        .addOnSuccessListener { postsSnapshot ->
                            val posts = postsSnapshot.documents.mapNotNull {
                                it.toObject(FirestorePost::class.java)?.copy(id = it.id)
                            }
                            allPosts.addAll(posts)
                            completedBatches++

                            // When all batches are done, sort and send
                            if (completedBatches == batches.size) {
                                // Sort by saved order (original postIds order)
                                val sortedPosts = postIds.mapNotNull { postId ->
                                    allPosts.find { it.id == postId }
                                }
                                trySend(sortedPosts)
                            }
                        }
                        .addOnFailureListener {
                            completedBatches++
                            if (completedBatches == batches.size) {
                                // Sort by saved order even with partial results
                                val sortedPosts = postIds.mapNotNull { postId ->
                                    allPosts.find { it.id == postId }
                                }
                                trySend(sortedPosts)
                            }
                        }
                }
            }

        awaitClose { listener.remove() }
    }

    override fun getSavedPostIdsFlow(): Flow<Set<String>> = callbackFlow {
        val userId = authRepository.getCurrentUserId()
        if (userId == null) {
            trySend(emptySet())
            awaitClose { }
            return@callbackFlow
        }

        val listener = firestore.collection("saved_posts")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptySet())
                    return@addSnapshotListener
                }

                val postIds = snapshot?.documents?.mapNotNull {
                    it.getString("postId")
                }?.toSet() ?: emptySet()

                trySend(postIds)
            }

        awaitClose { listener.remove() }
    }

    override fun getLikedPostIdsFlow(): Flow<Set<String>> = callbackFlow {
        val userId = authRepository.getCurrentUserId()
        if (userId == null) {
            trySend(emptySet())
            awaitClose { }
            return@callbackFlow
        }

        // Use collection group query to find all likes by this user across all posts
        // Each like doc is at /posts/{postId}/likes/{userId} with id_usuario field
        val listener = firestore.collectionGroup("likes")
            .whereEqualTo("id_usuario", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    android.util.Log.e("FirestoreCommunity", "Error loading liked post IDs", error)
                    trySend(emptySet())
                    return@addSnapshotListener
                }

                // Extract postId from the document path: /posts/{postId}/likes/{userId}
                val postIds = snapshot?.documents?.mapNotNull { doc ->
                    // Path is: posts/{postId}/likes/{likeDocId}
                    val pathSegments = doc.reference.path.split("/")
                    if (pathSegments.size >= 2 && pathSegments[0] == "posts") {
                        pathSegments[1] // This is the postId
                    } else {
                        null
                    }
                }?.toSet() ?: emptySet()

                trySend(postIds)
            }

        awaitClose { listener.remove() }
    }

    /**
     * Helper method to get user profile
     */
    private suspend fun getUserProfile(userId: String): FirestoreUser? {
        return try {
            val doc = firestore.collection("users").document(userId).get().await()
            doc.toObject(FirestoreUser::class.java)
        } catch (e: Exception) {
            null
        }
    }
    override fun getUserPostsFlow(userId: String): Flow<List<FirestorePost>> = callbackFlow {
        val listener = firestore. collection("posts")
            .whereEqualTo("authorId", userId)
            .orderBy("createdAt", Query.Direction. DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val posts = snapshot?. documents?.mapNotNull {
                    it.toObject(FirestorePost::class.java)?. copy(id = it.id)
                } ?: emptyList()

                trySend(posts)
            }

        awaitClose { listener.remove() }
    }
}