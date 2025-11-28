package com.example.aprendejapones.domain.model

/**
 * Usuario en Firestore (perfil público)
 */
data class UsuariosFirestore(
    val id_usuario: String = "",
    val nombre_usuario: String = "",
    val foto_url: String? = null,
    val rango: String = "初心者",
    val nivel: Int = 1,
    val biografia: String? = null,
    val fecha_registro: Long = System.currentTimeMillis()
)

/**
 * Publicación en Firestore
 */
data class Publicaciones(
    val id_publicacion: String = "",
    val id_autor: String = "",
    val nombre_autor: String = "",
    val contenido: String = "",
    val categoria: String = "General",
    val fecha_publicacion: Long = System.currentTimeMillis(),
    val contador_likes: Int = 0,
    val contador_comentarios: Int = 0
)

/**
 * Comentario en Firestore
 */
data class Comentarios(
    val id_comentario: String = "",
    val id_publicacion: String = "",
    val id_autor: String = "",
    val nombre_autor: String = "",
    val contenido: String = "",
    val fecha_publicacion: Long = System.currentTimeMillis()
)

/**
 * Like en Firestore
 */
data class Likes(
    val id_usuario: String = "",
    val fecha_reaccion: Long = System.currentTimeMillis()
)

// ============================================================
// Legacy models for backward compatibility during migration
// ============================================================

// Usuario en Firestore (legacy - para compatibilidad)
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

// Publicación (legacy - para compatibilidad)
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

// Comentario (legacy - para compatibilidad)
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

// Like (legacy - para compatibilidad)
data class FirestoreLike(
    val userId: String = "",
    val postId: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

// Saved Post (Bookmark) (legacy - para compatibilidad)
data class FirestoreSavedPost(
    val userId: String = "",
    val postId: String = "",
    val createdAt: Long = System.currentTimeMillis()
)