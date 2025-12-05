package com.example.aprendejapones.domain.model

/**
 * Modelo de usuario en Firestore con nomenclatura en español.
 *
 * Este modelo representa el perfil público del usuario almacenado en Firebase Firestore.
 * Utiliza convención de nombres en español (snake_case) para compatibilidad con
 * las reglas de seguridad de Firestore y el esquema de la base de datos.
 *
 * @property id_usuario Identificador único del usuario en Firestore.
 * @property nombre_usuario Nombre de usuario para mostrar públicamente.
 * @property foto_url URL de la foto de perfil del usuario (puede ser null).
 * @property rango Rango del usuario en japonés (por defecto "初心者" = Principiante).
 * @property nivel Nivel actual del usuario.
 * @property biografia Descripción o biografía del usuario (opcional).
 * @property fecha_registro Timestamp de cuando se registró el usuario.
 *
 * @see FirestoreUser Modelo legacy para compatibilidad durante la migración.
 *
 * @author Kotodama Team
 * @since 2.0.0
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
 * Modelo de publicación en Firestore con nomenclatura en español.
 *
 * Representa una publicación en la comunidad de la aplicación. Las publicaciones
 * pueden recibir likes y comentarios de otros usuarios.
 *
 * @property id_publicacion Identificador único de la publicación.
 * @property id_autor ID del usuario que creó la publicación.
 * @property nombre_autor Nombre del autor para mostrar.
 * @property contenido Texto de la publicación.
 * @property categoria Categoría de la publicación (ej: "General", "Preguntas").
 * @property fecha_publicacion Timestamp de creación de la publicación.
 * @property contador_likes Número total de likes en la publicación.
 * @property contador_comentarios Número total de comentarios en la publicación.
 *
 * @see Comentarios Para los comentarios de la publicación.
 * @see Likes Para los likes de la publicación.
 *
 * @author Kotodama Team
 * @since 2.0.0
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
 * Modelo de comentario en Firestore con nomenclatura en español.
 *
 * Representa un comentario realizado por un usuario en una publicación.
 *
 * @property id_comentario Identificador único del comentario.
 * @property id_publicacion ID de la publicación a la que pertenece el comentario.
 * @property id_autor ID del usuario que escribió el comentario.
 * @property nombre_autor Nombre del autor para mostrar.
 * @property contenido Texto del comentario.
 * @property fecha_publicacion Timestamp de cuando se creó el comentario.
 *
 * @see Publicaciones Para la publicación padre del comentario.
 *
 * @author Kotodama Team
 * @since 2.0.0
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
 * Modelo de like en Firestore con nomenclatura en español.
 *
 * Representa un "me gusta" dado por un usuario a una publicación.
 * Se almacena como subcolección dentro de cada publicación.
 *
 * @property id_usuario ID del usuario que dio el like.
 * @property fecha_reaccion Timestamp de cuando se dio el like.
 *
 * @see Publicaciones Para la publicación que recibió el like.
 *
 * @author Kotodama Team
 * @since 2.0.0
 */
data class Likes(
    val id_usuario: String = "",
    val fecha_reaccion: Long = System.currentTimeMillis()
)

// ============================================================
// Legacy models for backward compatibility during migration
// ============================================================

/**
 * Modelo de usuario legacy en Firestore para compatibilidad con versiones anteriores.
 *
 * **Nota:** Este modelo se mantiene por compatibilidad durante la migración de datos.
 * Para nuevas implementaciones, usar [UsuariosFirestore].
 *
 * Contiene información completa del perfil del usuario incluyendo datos de
 * autenticación, progreso y preferencias.
 *
 * @property id Identificador único del usuario (generalmente el UID de Firebase Auth).
 * @property username Nombre de usuario para mostrar.
 * @property email Correo electrónico del usuario.
 * @property photoUrl URL de la foto de perfil (opcional).
 * @property rank Rango del usuario en japonés.
 * @property level Nivel actual del usuario.
 * @property xp Puntos de experiencia totales.
 * @property streak Días consecutivos de estudio.
 * @property drops Monedas virtuales del usuario.
 * @property bio Biografía del usuario (opcional).
 * @property createdAt Timestamp de creación de la cuenta.
 * @property updatedAt Timestamp de la última actualización.
 *
 * @see UsuariosFirestore Nuevo modelo con nomenclatura en español.
 *
 * @author Kotodama Team
 * @since 1.0.0
 * @deprecated Usar [UsuariosFirestore] para nuevas implementaciones.
 */
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

/**
 * Modelo de publicación legacy en Firestore para compatibilidad.
 *
 * **Nota:** Este modelo se mantiene por compatibilidad durante la migración.
 * Para nuevas implementaciones, usar [Publicaciones].
 *
 * @property id Identificador único de la publicación.
 * @property authorId ID del autor de la publicación.
 * @property authorName Nombre del autor para mostrar.
 * @property content Contenido textual de la publicación.
 * @property category Categoría de la publicación.
 * @property createdAt Timestamp de creación.
 * @property likes Lista de IDs de usuarios que dieron like.
 * @property comments Lista de IDs de comentarios.
 * @property isLiked Indica si el usuario actual dio like (calculado en cliente, no se almacena).
 * @property likesCount Contador de likes.
 * @property commentsCount Contador de comentarios.
 * @property savesCount Contador de guardados/bookmarks.
 *
 * @see Publicaciones Nuevo modelo con nomenclatura en español.
 *
 * @author Kotodama Team
 * @since 1.0.0
 * @deprecated Usar [Publicaciones] para nuevas implementaciones.
 */
data class FirestorePost(
    val id: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val content: String = "",
    val category: String = "General",
    val createdAt: Long = System. currentTimeMillis(),
    val likes: List<String> = emptyList(),
    val comments: List<String> = emptyList(),
    val isLiked: Boolean = false,
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val savesCount: Int = 0
)

/**
 * Modelo de comentario legacy en Firestore para compatibilidad.
 *
 * **Nota:** Este modelo se mantiene por compatibilidad durante la migración.
 * Para nuevas implementaciones, usar [Comentarios].
 *
 * @property id Identificador único del comentario.
 * @property postId ID de la publicación a la que pertenece.
 * @property authorId ID del autor del comentario.
 * @property authorName Nombre del autor para mostrar.
 * @property authorPhotoUrl URL de la foto del autor (opcional).
 * @property content Contenido textual del comentario.
 * @property createdAt Timestamp de creación.
 * @property parentCommentId ID del comentario padre si es una respuesta (opcional).
 *
 * @see Comentarios Nuevo modelo con nomenclatura en español.
 *
 * @author Kotodama Team
 * @since 1.0.0
 * @deprecated Usar [Comentarios] para nuevas implementaciones.
 */
data class FirestoreComment(
    val id: String = "",
    val postId: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val authorPhotoUrl: String? = null,
    val content: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val parentCommentId: String? = null
)

/**
 * Modelo de like legacy en Firestore para compatibilidad.
 *
 * **Nota:** Este modelo se mantiene por compatibilidad durante la migración.
 * Para nuevas implementaciones, usar [Likes].
 *
 * @property userId ID del usuario que dio el like.
 * @property postId ID de la publicación que recibió el like.
 * @property createdAt Timestamp de cuando se dio el like.
 *
 * @see Likes Nuevo modelo con nomenclatura en español.
 *
 * @author Kotodama Team
 * @since 1.0.0
 * @deprecated Usar [Likes] para nuevas implementaciones.
 */
data class FirestoreLike(
    val userId: String = "",
    val postId: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Modelo de publicación guardada (bookmark) legacy en Firestore.
 *
 * Representa cuando un usuario guarda una publicación para ver más tarde.
 *
 * **Nota:** Este modelo se mantiene por compatibilidad durante la migración.
 *
 * @property userId ID del usuario que guardó la publicación.
 * @property postId ID de la publicación guardada.
 * @property createdAt Timestamp de cuando se guardó.
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
data class FirestoreSavedPost(
    val userId: String = "",
    val postId: String = "",
    val createdAt: Long = System.currentTimeMillis()
)