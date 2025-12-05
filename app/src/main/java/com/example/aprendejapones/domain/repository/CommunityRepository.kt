package com.example.aprendejapones.domain.repository

import com.example.aprendejapones.domain.model.FirestoreComment
import com.example.aprendejapones.domain.model.FirestorePost
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio para las funcionalidades de comunidad.
 *
 * Esta interfaz define el contrato completo para las operaciones de la
 * comunidad de la aplicación, incluyendo publicaciones, comentarios,
 * likes y guardados (bookmarks).
 *
 * ## Funcionalidades
 * - **Publicaciones:** Crear, listar, eliminar
 * - **Likes:** Dar/quitar like, verificar estado
 * - **Comentarios:** Añadir, listar
 * - **Guardados:** Guardar/quitar publicaciones para ver después
 *
 * ## Flujos Reactivos
 * Varios métodos devuelven [Flow] para actualizaciones en tiempo real,
 * especialmente útil cuando se usan con Firestore listeners.
 *
 * @see FirestorePost Modelo de publicación.
 * @see FirestoreComment Modelo de comentario.
 * @see PostRepository Para obtención simple de publicaciones.
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
interface CommunityRepository {

    /**
     * Obtiene el stream de publicaciones en tiempo real.
     *
     * Las publicaciones se ordenan por fecha de creación (más recientes primero).
     *
     * @return Flow que emite la lista actualizada de [FirestorePost].
     */
    fun getPostsFlow(): Flow<List<FirestorePost>>

    /**
     * Crea una nueva publicación.
     *
     * El autor se asigna automáticamente basándose en el usuario autenticado.
     *
     * @param content Contenido textual de la publicación.
     * @param category Categoría de la publicación (ej: "General", "Preguntas").
     * @return [Result] con éxito o la excepción si falló.
     */
    suspend fun createPost(content: String, category: String): Result<Unit>

    /**
     * Da "me gusta" a una publicación.
     *
     * Crea un registro de like en la subcolección de la publicación.
     * Si el usuario ya dio like, la operación no tiene efecto adicional.
     *
     * @param postId ID de la publicación.
     * @return [Result] con éxito o la excepción si falló.
     */
    suspend fun likePost(postId: String): Result<Unit>

    /**
     * Quita el "me gusta" de una publicación.
     *
     * Elimina el registro de like del usuario actual de la publicación.
     *
     * @param postId ID de la publicación.
     * @return [Result] con éxito o la excepción si falló.
     */
    suspend fun unlikePost(postId: String): Result<Unit>

    /**
     * Añade un comentario a una publicación.
     *
     * El autor del comentario se asigna automáticamente.
     *
     * @param postId ID de la publicación a comentar.
     * @param content Contenido del comentario.
     * @return [Result] con éxito o la excepción si falló.
     */
    suspend fun addComment(postId: String, content: String): Result<Unit>

    /**
     * Obtiene los comentarios de una publicación en tiempo real.
     *
     * Los comentarios se ordenan por fecha de creación.
     *
     * @param postId ID de la publicación.
     * @return Flow que emite la lista actualizada de [FirestoreComment].
     */
    fun getCommentsFlow(postId: String): Flow<List<FirestoreComment>>

    /**
     * Verifica si el usuario actual ha dado like a una publicación.
     *
     * @param postId ID de la publicación a verificar.
     * @return `true` si el usuario dio like, `false` en caso contrario.
     */
    suspend fun hasUserLikedPost(postId: String): Boolean

    /**
     * Elimina una publicación.
     *
     * Solo el autor de la publicación puede eliminarla. Esta operación
     * también elimina los likes y comentarios asociados.
     *
     * @param postId ID de la publicación a eliminar.
     * @return [Result] con éxito o la excepción si falló.
     */
    suspend fun deletePost(postId: String): Result<Unit>

    /**
     * Guarda una publicación (bookmark).
     *
     * Añade la publicación a la lista de guardados del usuario
     * para verla después.
     *
     * @param postId ID de la publicación a guardar.
     * @return [Result] con éxito o la excepción si falló.
     */
    suspend fun savePost(postId: String): Result<Unit>

    /**
     * Quita una publicación de guardados.
     *
     * Remueve la publicación de la lista de guardados del usuario.
     *
     * @param postId ID de la publicación a quitar de guardados.
     * @return [Result] con éxito o la excepción si falló.
     */
    suspend fun unsavePost(postId: String): Result<Unit>

    /**
     * Verifica si el usuario actual ha guardado una publicación.
     *
     * @param postId ID de la publicación a verificar.
     * @return `true` si está guardada, `false` en caso contrario.
     */
    suspend fun hasUserSavedPost(postId: String): Boolean

    /**
     * Obtiene las publicaciones guardadas del usuario actual.
     *
     * @return Flow que emite la lista actualizada de publicaciones guardadas.
     */
    fun getSavedPostsFlow(): Flow<List<FirestorePost>>

    /**
     * Obtiene los IDs de las publicaciones guardadas.
     *
     * Útil para verificar rápidamente el estado de guardado
     * sin cargar las publicaciones completas.
     *
     * @return Flow que emite el Set de IDs de publicaciones guardadas.
     */
    fun getSavedPostIdsFlow(): Flow<Set<String>>

    /**
     * Obtiene los IDs de las publicaciones con like del usuario.
     *
     * Útil para mostrar el estado de like en listas de publicaciones
     * sin consultar cada una individualmente.
     *
     * @return Flow que emite el Set de IDs de publicaciones con like.
     */
    fun getLikedPostIdsFlow(): Flow<Set<String>>

    /**
     * Obtiene las publicaciones de un usuario específico.
     *
     * Útil para mostrar las publicaciones en el perfil de usuario.
     *
     * @param userId ID del usuario cuyas publicaciones se quieren obtener.
     * @return Flow que emite la lista de publicaciones del usuario.
     */
    fun getUserPostsFlow(userId: String): Flow<List<FirestorePost>>
}
