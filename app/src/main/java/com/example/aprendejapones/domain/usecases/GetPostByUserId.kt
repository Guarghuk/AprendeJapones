package com.example.aprendejapones.domain.use_case

import com.example.aprendejapones.domain.model.FirestorePost
import com.example.aprendejapones.domain.model.Response
import com.example.aprendejapones.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Caso de uso para obtener las publicaciones de un usuario específico.
 *
 * Este caso de uso encapsula la lógica de negocio para obtener
 * las publicaciones creadas por un usuario particular, útil para
 * mostrar en la pantalla de perfil.
 *
 * ## Uso
 *
 * ```kotlin
 * class UserProfileViewModel @Inject constructor(
 *     private val getPostsByUserId: GetPostsByUserId
 * ) : ViewModel() {
 *
 *     fun loadUserPosts(userId: String) {
 *         getPostsByUserId(userId).collect { response ->
 *             when (response) {
 *                 is Response.Loading -> showLoading()
 *                 is Response.Success -> displayPosts(response.data)
 *                 is Response.Failure -> showError(response.e)
 *             }
 *         }
 *     }
 * }
 * ```
 *
 * @property repo Repositorio de publicaciones inyectado.
 *
 * @see PostRepository Repositorio utilizado internamente.
 * @see Response Estados posibles de la respuesta.
 * @see FirestorePost Modelo de publicación.
 * @see GetPostsUseCase Para obtener todas las publicaciones.
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
class GetPostsByUserId @Inject constructor(
    private val repo: PostRepository
) {
    /**
     * Ejecuta el caso de uso para obtener las publicaciones de un usuario.
     *
     * @param userId ID del usuario cuyas publicaciones se quieren obtener.
     * @return Flow que emite el [Response] con la lista de publicaciones del usuario.
     */
    operator fun invoke(userId: String): Flow<Response<List<FirestorePost>>> = 
        repo.getPostsByUserId(userId)
}