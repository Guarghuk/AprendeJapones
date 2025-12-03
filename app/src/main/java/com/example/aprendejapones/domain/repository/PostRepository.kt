package com.example.aprendejapones.domain.repository

import com.example.aprendejapones.domain.model.FirestorePost
import com.example.aprendejapones.domain.model.Response
import kotlinx.coroutines.flow.Flow

/**
 * Alias de tipo para una lista de publicaciones de Firestore.
 *
 * @see FirestorePost Modelo individual de publicación.
 */
typealias Posts = List<FirestorePost>

/**
 * Repositorio para la obtención de publicaciones.
 *
 * Esta interfaz proporciona métodos para obtener publicaciones de la
 * comunidad, envueltas en el tipo [Response] que indica el estado de
 * la operación (cargando, éxito, error).
 *
 * ## Diferencia con [CommunityRepository]
 * - Este repositorio solo lee publicaciones con estado de respuesta
 * - [CommunityRepository] proporciona operaciones CRUD completas
 *
 * @see Response Para los estados de la respuesta.
 * @see FirestorePost Modelo de publicación.
 * @see CommunityRepository Para operaciones CRUD de comunidad.
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
interface PostRepository {

    /**
     * Obtiene todas las publicaciones como un Flow reactivo.
     *
     * El Flow emite [Response.Loading] inicialmente, seguido de
     * [Response.Success] con las publicaciones o [Response.Failure]
     * si ocurre un error.
     *
     * @return Flow que emite el estado [Response] con la lista de [FirestorePost].
     */
    fun getPosts(): Flow<Response<Posts>>

    /**
     * Obtiene las publicaciones de un usuario específico.
     *
     * Útil para mostrar las publicaciones en el perfil de un usuario.
     *
     * @param userId ID del usuario cuyas publicaciones se quieren obtener.
     * @return Flow que emite el estado [Response] con las publicaciones del usuario.
     */
    fun getPostsByUserId(userId: String): Flow<Response<Posts>>
}