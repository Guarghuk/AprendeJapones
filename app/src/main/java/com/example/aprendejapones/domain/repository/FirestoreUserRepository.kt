package com.example.aprendejapones.domain.repository

import android.net.Uri
import com.example.aprendejapones.domain.model.FirestoreUser
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio para operaciones de usuario en Firestore.
 *
 * Esta interfaz define el contrato para las operaciones del perfil
 * de usuario almacenado en Firebase Firestore, incluyendo actualización
 * de perfil, subida de fotos, sincronización y gestión de XP.
 *
 * ## Diferencia con [UserRepository]
 * - Este repositorio maneja datos en la nube (Firestore)
 * - [UserRepository] maneja datos locales (Room Database)
 *
 * ## Funcionalidades
 * - Lectura y escritura del perfil de usuario
 * - Subida de fotos de perfil a Firebase Storage
 * - Sincronización de datos locales con la nube
 * - Gestión de XP y niveles
 *
 * @see FirestoreUser Modelo de usuario en Firestore.
 * @see UserRepository Para operaciones con datos locales.
 * @see AuthRepository Para autenticación.
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
interface FirestoreUserRepository {

    /**
     * Obtiene el perfil de usuario como Flow para actualizaciones en tiempo real.
     *
     * Utiliza un listener de Firestore para recibir cambios automáticamente.
     *
     * @param userId ID del usuario a observar.
     * @return Flow que emite el [FirestoreUser] o `null` si no existe.
     */
    fun getUserProfileFlow(userId: String): Flow<FirestoreUser?>

    /**
     * Obtiene el perfil de usuario de forma única.
     *
     * Realiza una consulta puntual a Firestore.
     *
     * @param userId ID del usuario a obtener.
     * @return [FirestoreUser] o `null` si no existe.
     */
    suspend fun getUserProfile(userId: String): FirestoreUser?

    /**
     * Actualiza el perfil de usuario en Firestore.
     *
     * Sobrescribe el documento completo del usuario.
     *
     * @param user El [FirestoreUser] con los datos actualizados.
     * @return [Result] con éxito o la excepción si falló.
     */
    suspend fun updateUserProfile(user: FirestoreUser): Result<Unit>

    /**
     * Sube una foto de perfil a Firebase Storage.
     *
     * La imagen se almacena en el path `profile_photos/{userId}/profile.jpg`.
     *
     * @param userId ID del usuario dueño de la foto.
     * @param uri URI local de la imagen a subir.
     * @return [Result] con la URL de descarga de la imagen o error.
     */
    suspend fun uploadProfilePhoto(userId: String, uri: Uri): Result<String>

    /**
     * Actualiza campos específicos del perfil del usuario.
     *
     * Más eficiente que [updateUserProfile] cuando solo se necesita
     * modificar algunos campos.
     *
     * @param userId ID del usuario a actualizar.
     * @param fields Mapa de campos a actualizar con sus nuevos valores.
     * @return [Result] con éxito o la excepción si falló.
     */
    suspend fun updateUserFields(userId: String, fields: Map<String, Any>): Result<Unit>

    /**
     * Sincroniza datos locales del usuario con Firestore.
     *
     * Útil para hacer backup de datos locales o al vincular
     * una cuenta local con una cuenta de Firebase.
     *
     * @param user El [FirestoreUser] con los datos a sincronizar.
     * @return [Result] con éxito o la excepción si falló.
     */
    suspend fun syncUserToFirestore(user: FirestoreUser): Result<Unit>

    /**
     * Obtiene el perfil del usuario actualmente autenticado.
     *
     * Combina la obtención del ID de Firebase Auth con la consulta
     * del perfil en Firestore.
     *
     * @return [FirestoreUser] del usuario actual o `null` si no hay sesión.
     */
    suspend fun getCurrentUserProfile(): FirestoreUser?

    /**
     * Añade XP al usuario y actualiza el nivel si es necesario.
     *
     * Implementa la lógica de subida de nivel cuando los XP
     * superan el umbral del nivel actual.
     *
     * @param userId ID del usuario a actualizar.
     * @param xp Cantidad de XP a añadir.
     * @return [Result] con éxito o la excepción si falló.
     */
    suspend fun addXpToUser(userId: String, xp: Int): Result<Unit>

    /**
     * Actualiza la racha de estudio del usuario.
     *
     * @param userId ID del usuario a actualizar.
     * @param streak Nueva cantidad de días de racha.
     * @return [Result] con éxito o la excepción si falló.
     */
    suspend fun updateStreak(userId: String, streak: Int): Result<Unit>
}
