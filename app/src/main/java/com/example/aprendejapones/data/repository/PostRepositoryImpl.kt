package com.example.aprendejapones.data.repository

import com.example.aprendejapones.domain.model.FirestorePost
import com.example.aprendejapones.domain.model.Response
import com.example.aprendejapones.domain.repository.PostRepository
import com.example.aprendejapones.domain.repository.Posts
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : PostRepository {

    override fun getPosts(): Flow<Response<Posts>> = callbackFlow {
        val snapshotListener = firestore.collection("posts")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                val response = if (snapshot != null) {
                    val posts = snapshot.toObjects(FirestorePost::class.java)
                    Response.Success(posts)
                } else {
                    Response.Failure(e)
                }
                trySend(response)
            }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override fun getPostsByUserId(userId: String): Flow<Response<Posts>> = callbackFlow {
        val snapshotListener = firestore.collection("posts")
            .whereEqualTo("authorId", userId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                val response = if (snapshot != null) {
                    val posts = snapshot.toObjects(FirestorePost::class.java)
                    Response.Success(posts)
                } else {
                    Response.Failure(e)
                }
                trySend(response)
            }
        awaitClose {
            snapshotListener.remove()
        }
    }
}