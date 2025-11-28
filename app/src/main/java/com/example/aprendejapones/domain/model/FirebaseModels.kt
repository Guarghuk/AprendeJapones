package com.example.aprendejapones.domain.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

// Usuario en Firestore
data class FirestoreUser(
    val id: String = "",
    val username: String = "",
    val email: String = "",
    val photoUrl: String? = null,
    val rank: String = "初心者",
    val level: Int = 1,
    val xp: Int = 0,
    val streak: Int = 0,
    val drops: Int = 0,
    val bio: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// Publicación
data class FirestorePost(
    val id: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val authorPhotoUrl: String? = null,
    val content: String = "",
    val category: String = "General",
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val savesCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

// Comentario
data class FirestoreComment(
    val id: String = "",
    val postId: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val authorPhotoUrl: String? = null,
    val content: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val parentCommentId: String? = null  // Para respuestas
)

// Like
data class FirestoreLike(
    val userId: String = "",
    val postId: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

// Saved Post (Bookmark)
data class FirestoreSavedPost(
    val userId: String = "",
    val postId: String = "",
    val createdAt: Long = System.currentTimeMillis()
)