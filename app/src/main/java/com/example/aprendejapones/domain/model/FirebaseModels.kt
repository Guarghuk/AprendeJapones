package com.example.aprendejapones.domain.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
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
    @ServerTimestamp
    val createdAt: Date = Date(),

    // Listas para manejar likes, comentarios y guardados
    val likes: List<String> = emptyList(),      // Lista de user IDs que han dado like
    val comments: List<String> = emptyList(),   // Lista de comment IDs
    val saves: List<String> = emptyList(),      // Lista de user IDs que han guardado el post

    // Estos campos no se almacenan en Firestore, se calculan en el cliente
    val isLiked: Boolean = false,
    val isSaved: Boolean = false
) {
    // Propiedades computadas para obtener los conteos directamente
    val likesCount: Int
        get() = likes.size

    val commentsCount: Int
        get() = comments.size

    val savesCount: Int
        get() = saves.size
}

// Comentario
data class FirestoreComment(
    val id: String = "",
    val postId: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val authorPhotoUrl: String? = null,
    val content: String = "",
    val createdAt: Date,
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