package com.example.aprendejapones.domain.repository

import com.example.aprendejapones.domain.model.FirestoreUser
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio de autenticación para gestionar el login, registro y sesión de usuarios.
 *
 * Esta interfaz define el contrato para las operaciones de autenticación,
 * abstrayendo la implementación concreta de Firebase Auth u otros proveedores.
 *
 * ## Responsabilidades
 * - Autenticación con email y contraseña
 * - Autenticación con Google Sign-In
 * - Gestión de la sesión del usuario
 * - Creación de cuentas nuevas
 *
 * ## Uso
 *
 * ```kotlin
 * // Observar cambios de autenticación
 * authRepository.currentUser.collect { user ->
 *     if (user != null) navigateToHome() else navigateToLogin()
 * }
 *
 * // Iniciar sesión
 * val result = authRepository.loginWithEmail(email, password)
 * result.fold(
 *     onSuccess = { user -> println("Bienvenido ${user.username}") },
 *     onFailure = { error -> showError(error.message) }
 * )
 * ```
 *
 * @see FirestoreUser Modelo de usuario autenticado.
 * @see FirestoreUserRepository Para operaciones de perfil de usuario.
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
interface AuthRepository {

    /**
     * Flow reactivo que emite el usuario actualmente autenticado.
     *
     * Emite `null` cuando no hay usuario autenticado, lo que permite
     * observar cambios en tiempo real del estado de autenticación.
     *
     * @return Flow que emite el [FirestoreUser] actual o `null`.
     */
    val currentUser: Flow<FirestoreUser?>

    /**
     * Inicia sesión con email y contraseña.
     *
     * Si el usuario no existe en Firestore, se crea un nuevo perfil
     * automáticamente.
     *
     * @param email Correo electrónico del usuario.
     * @param password Contraseña del usuario.
     * @return [Result] con el [FirestoreUser] si fue exitoso, o la excepción si falló.
     */
    suspend fun loginWithEmail(email: String, password: String): Result<FirestoreUser>

    /**
     * Inicia sesión con Google Sign-In.
     *
     * Utiliza el token de ID proporcionado por Google para autenticar
     * al usuario. Si es la primera vez, se crea un perfil en Firestore.
     *
     * @param idToken Token de ID de Google obtenido del proceso de Google Sign-In.
     * @return [Result] con el [FirestoreUser] si fue exitoso, o la excepción si falló.
     */
    suspend fun loginWithGoogle(idToken: String): Result<FirestoreUser>

    /**
     * Registra un nuevo usuario con email y contraseña.
     *
     * Crea una cuenta nueva en Firebase Auth y un perfil correspondiente
     * en Firestore con el nombre de usuario proporcionado.
     *
     * @param email Correo electrónico para la nueva cuenta.
     * @param password Contraseña para la nueva cuenta (mínimo 6 caracteres).
     * @param username Nombre de usuario para mostrar en la aplicación.
     * @return [Result] con el [FirestoreUser] creado si fue exitoso, o la excepción si falló.
     */
    suspend fun registerWithEmail(
        email: String,
        password: String,
        username: String
    ): Result<FirestoreUser>

    /**
     * Cierra la sesión del usuario actual.
     *
     * Después de llamar a este método, [currentUser] emitirá `null`.
     */
    suspend fun logout()

    /**
     * Verifica si hay un usuario con sesión activa.
     *
     * @return `true` si hay un usuario autenticado, `false` en caso contrario.
     */
    suspend fun isUserLoggedIn(): Boolean

    /**
     * Obtiene el ID del usuario actualmente autenticado.
     *
     * @return ID del usuario o `null` si no hay sesión activa.
     */
    suspend fun getCurrentUserId(): String?
}