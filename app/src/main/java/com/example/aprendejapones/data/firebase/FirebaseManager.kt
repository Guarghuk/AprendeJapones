package com.example.aprendejapones.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manager centralizado para la configuración y acceso a servicios de Firebase.
 *
 * Este singleton proporciona acceso fácil a las instancias de Firebase Auth,
 * Firestore y Storage, además de métodos helper para operaciones comunes.
 * Centraliza la configuración de Firebase para mantener consistencia en toda
 * la aplicación.
 *
 * ## Servicios Proporcionados
 * - **Firebase Auth:** Autenticación de usuarios
 * - **Firestore:** Base de datos NoSQL en tiempo real
 * - **Firebase Storage:** Almacenamiento de archivos (fotos de perfil, imágenes)
 *
 * ## Colecciones de Firestore
 * - `users`: Perfiles de usuario
 * - `posts`: Publicaciones de la comunidad
 * - `comments`: Comentarios en publicaciones
 * - `likes`: Reacciones a publicaciones
 *
 * ## Paths de Storage
 * - `profile_photos/`: Fotos de perfil de usuarios
 * - `post_images/`: Imágenes adjuntas a publicaciones
 *
 * ## Uso
 *
 * ```kotlin
 * class MyRepository @Inject constructor(
 *     private val firebaseManager: FirebaseManager
 * ) {
 *     suspend fun savePost(content: String) {
 *         val userId = firebaseManager.getCurrentUserId() ?: return
 *         firebaseManager.postsCollection().add(mapOf(
 *             "authorId" to userId,
 *             "content" to content
 *         ))
 *     }
 * }
 * ```
 *
 * @property auth Instancia de Firebase Authentication.
 * @property firestore Instancia de Firebase Firestore.
 * @property storage Instancia de Firebase Storage.
 *
 * @see com.example.aprendejapones.di.FirebaseModule Para la configuración de Hilt.
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
@Singleton
class FirebaseManager @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {
    companion object {
        // ============ Collection Names ============

        /** Nombre de la colección de usuarios en Firestore */
        const val USERS_COLLECTION = "users"

        /** Nombre de la colección de publicaciones en Firestore */
        const val POSTS_COLLECTION = "posts"

        /** Nombre de la colección de comentarios en Firestore */
        const val COMMENTS_COLLECTION = "comments"

        /** Nombre de la colección de likes en Firestore */
        const val LIKES_COLLECTION = "likes"
        
        // ============ Storage Paths ============

        /** Path en Storage para fotos de perfil */
        const val PROFILE_PHOTOS_PATH = "profile_photos"

        /** Path en Storage para imágenes de publicaciones */
        const val POST_IMAGES_PATH = "post_images"
    }

    /**
     * Obtiene el ID del usuario actualmente autenticado.
     *
     * @return UID del usuario actual o `null` si no hay sesión activa.
     */
    fun getCurrentUserId(): String? = auth.currentUser?.uid

    /**
     * Verifica si hay un usuario con sesión activa.
     *
     * @return `true` si hay un usuario autenticado, `false` en caso contrario.
     */
    fun isUserLoggedIn(): Boolean = auth.currentUser != null

    /**
     * Obtiene la referencia a la colección de usuarios.
     *
     * @return [CollectionReference] a la colección "users".
     */
    fun usersCollection() = firestore.collection(USERS_COLLECTION)

    /**
     * Obtiene la referencia a la colección de publicaciones.
     *
     * @return [CollectionReference] a la colección "posts".
     */
    fun postsCollection() = firestore.collection(POSTS_COLLECTION)

    /**
     * Obtiene la referencia a la colección de comentarios.
     *
     * @return [CollectionReference] a la colección "comments".
     */
    fun commentsCollection() = firestore.collection(COMMENTS_COLLECTION)

    /**
     * Obtiene la referencia a la colección de likes.
     *
     * @return [CollectionReference] a la colección "likes".
     */
    fun likesCollection() = firestore.collection(LIKES_COLLECTION)

    /**
     * Obtiene la referencia al directorio de fotos de perfil en Storage.
     *
     * @return [StorageReference] al directorio "profile_photos".
     */
    fun profilePhotosStorage() = storage.reference.child(PROFILE_PHOTOS_PATH)

    /**
     * Obtiene la referencia al directorio de imágenes de posts en Storage.
     *
     * @return [StorageReference] al directorio "post_images".
     */
    fun postImagesStorage() = storage.reference.child(POST_IMAGES_PATH)

    /**
     * Cierra la sesión del usuario actual.
     *
     * Después de llamar este método, [getCurrentUserId] retornará `null`
     * y [isUserLoggedIn] retornará `false`.
     */
    fun signOut() = auth.signOut()
}
