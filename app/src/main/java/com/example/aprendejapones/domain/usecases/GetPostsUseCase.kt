package com.example.aprendejapones.domain.use_case

import com.example.aprendejapones.domain.model.FirestorePost
import com.example.aprendejapones.domain.model.Response
import com.example.aprendejapones.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Caso de uso para obtener todas las publicaciones de la comunidad.
 *
 * Este caso de uso encapsula la lógica de negocio para la obtención
 * de publicaciones, siguiendo el principio de responsabilidad única
 * de Clean Architecture.
 *
 * ## Uso
 *
 * ```kotlin
 * class CommunityViewModel @Inject constructor(
 *     private val getPostsUseCase: GetPostsUseCase
 * ) : ViewModel() {
 *
 *     fun loadPosts() {
 *         getPostsUseCase().collect { response ->
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
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
class GetPostsUseCase @Inject constructor(
    private val repo: PostRepository
) {
    /**
     * Ejecuta el caso de uso para obtener las publicaciones.
     *
     * Se utiliza el patrón de operador `invoke` para una sintaxis más limpia.
     *
     * @return Flow que emite el [Response] con la lista de publicaciones.
     */
    operator fun invoke(): Flow<Response<List<FirestorePost>>> = repo.getPosts()
}