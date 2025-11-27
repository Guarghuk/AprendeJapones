package com.example.aprendejapones.data.repository

import com.example.aprendejapones.domain.model.FirestorePost
import com.example.aprendejapones.domain.repository.AuthRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
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

                val user = getCurrentUserProfile(userId)

                val post = FirestorePost(
                    authorId = userId,
                    authorName = user.username,
                    authorPhotoUrl = user.photoUrl,
                    content = content,
                    category = category,
                    createdAt = System.currentTimeMillis()
                )

                firestore.collection("posts").add(post).await()
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun likePost(postId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val userId = authRepository.getCurrentUserId()
                    ?: return@withContext Result.failure(Exception("Not logged in"))

                val likeId = "${userId}_${postId}"

                // Crear like
                val like = FirestoreLike(userId, postId, System.currentTimeMillis())
                firestore.collection("likes").document(likeId).set(like).await()

                // Incrementar contador
                firestore.collection("posts").document(postId)
                    .update("likesCount", FieldValue.increment(1))
                    .await()

                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}