package com.example.aprendejapones.domain.model

import com.google.firebase.firestore.DocumentId
import java.util.Date

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
    val content: String = "",
    val category: String = "General",
    val createdAt: Long = System. currentTimeMillis(),
    val likes: List<String> = emptyList(), // Lista de user IDs que han dado like
    val comments: List<String> = emptyList(), // Lista de comment IDs
    // Este campo no se almacena en Firestore, se calcula en el cliente
    val isLiked: Boolean = false,
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val savesCount: Int = 0
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