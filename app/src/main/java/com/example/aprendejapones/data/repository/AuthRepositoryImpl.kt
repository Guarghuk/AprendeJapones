package com.example.aprendejapones.data.repository

import com.example.aprendejapones.domain.model.FirestoreUser
import com.example.aprendejapones.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Función de extensión para convertir [FirebaseUser] a [FirestoreUser].
 *
 * Extrae la información disponible del usuario de Firebase Auth y la
 * mapea al modelo de dominio [FirestoreUser].
 *
 * @receiver Usuario de Firebase Authentication.
 * @return Modelo [FirestoreUser] con los datos mapeados.
 */
private fun FirebaseUser.toFirestoreUser(): FirestoreUser {
    return FirestoreUser(
        id = uid,
        username = displayName ?: email?.substringBefore("@") ?: "Usuario",
        email = email ?: "",
        photoUrl = photoUrl?.toString(),
        createdAt = metadata?.creationTimestamp ?: System.currentTimeMillis()
    )
}

/**
 * Implementación del repositorio de autenticación usando Firebase.
 *
 * Esta clase implementa [AuthRepository] utilizando Firebase Authentication
 * para la gestión de usuarios y Firebase Firestore para almacenar los
 * perfiles extendidos de los usuarios.
 *
 * ## Flujo de Autenticación
 * 1. El usuario se autentica con Firebase Auth (email/password o Google)
 * 2. Se verifica si existe un perfil en Firestore
 * 3. Si no existe, se crea uno nuevo con los datos de Auth
 * 4. Se retorna el [FirestoreUser] completo
 *
 * ## Thread Safety
 * Todas las operaciones suspending se ejecutan en [Dispatchers.IO]
 * para evitar bloquear el hilo principal.
 *
 * @property firebaseAuth Instancia de Firebase Authentication.
 * @property firestore Instancia de Firebase Firestore.
 *
 * @see AuthRepository Interfaz que implementa esta clase.
 * @see FirestoreUser Modelo de usuario retornado.
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    /**
     * Flow reactivo que emite el estado actual de autenticación.
     *
     * Utiliza [callbackFlow] para convertir el listener de Firebase Auth
     * en un Flow de Kotlin. Emite automáticamente cuando el usuario
     * inicia o cierra sesión.
     */
    override val currentUser: Flow<FirestoreUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            val user = auth.currentUser
            trySend(user?.toFirestoreUser())
        }

        firebaseAuth.addAuthStateListener(listener)

        awaitClose {
            firebaseAuth.removeAuthStateListener(listener)
        }
    }

    /**
     * Inicia sesión con email y contraseña.
     *
     * Si el usuario existe en Auth pero no en Firestore, crea el perfil
     * automáticamente.
     *
     * @param email Correo electrónico del usuario.
     * @param password Contraseña del usuario.
     * @return [Result.success] con el usuario o [Result.failure] con la excepción.
     */
    override suspend fun loginWithEmail(
        email: String,
        password: String
    ): Result<FirestoreUser> = withContext(Dispatchers.IO) {
        try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user ?: return@withContext Result.failure(
                Exception("User is null")
            )

            // Check if user exists in Firestore, if not create profile
            val existingUser = try {
                getUserFromFirestore(user.uid)
            } catch (e: Exception) {
                null
            }

            val firestoreUser = existingUser ?: run {
                val newUser = user.toFirestoreUser()
                firestore.collection("users")
                    .document(user.uid)
                    .set(newUser)
                    .await()
                newUser
            }

            Result.success(firestoreUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Inicia sesión con Google Sign-In.
     *
     * Utiliza el token de ID de Google para crear credenciales de Firebase
     * y autenticar al usuario. Si es nuevo, crea su perfil en Firestore.
     *
     * @param idToken Token de ID obtenido del proceso de Google Sign-In.
     * @return [Result.success] con el usuario o [Result.failure] con la excepción.
     */
    override suspend fun loginWithGoogle(idToken: String): Result<FirestoreUser> = withContext(Dispatchers.IO) {
        try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = firebaseAuth.signInWithCredential(credential).await()
            val user = result.user ?: return@withContext Result.failure(
                Exception("User is null")
            )

            // Check if user exists in Firestore, if not create profile
            val existingUser = try {
                getUserFromFirestore(user.uid)
            } catch (e: Exception) {
                null
            }

            val firestoreUser = existingUser ?: run {
                val newUser = user.toFirestoreUser()
                firestore.collection("users")
                    .document(user.uid)
                    .set(newUser)
                    .await()
                newUser
            }

            Result.success(firestoreUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Registra un nuevo usuario con email, contraseña y nombre de usuario.
     *
     * Crea la cuenta en Firebase Auth y el perfil correspondiente en Firestore.
     *
     * @param email Correo electrónico para la nueva cuenta.
     * @param password Contraseña (mínimo 6 caracteres por requisito de Firebase).
     * @param username Nombre de usuario para mostrar en la aplicación.
     * @return [Result.success] con el usuario creado o [Result.failure] con la excepción.
     */
    override suspend fun registerWithEmail(
        email: String,
        password: String,
        username: String
    ): Result<FirestoreUser> = withContext(Dispatchers.IO) {
        try {
            // Crear usuario en Firebase Auth
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user ?: return@withContext Result.failure(
                Exception("User is null")
            )

            // Crear perfil en Firestore
            val firestoreUser = FirestoreUser(
                id = user.uid,
                username = username,
                email = email,
                createdAt = System.currentTimeMillis()
            )

            firestore.collection("users")
                .document(user.uid)
                .set(firestoreUser)
                .await()

            Result.success(firestoreUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Cierra la sesión del usuario actual.
     */
    override suspend fun logout() {
        firebaseAuth.signOut()
    }

    /**
     * Verifica si hay un usuario con sesión activa.
     *
     * @return `true` si hay usuario autenticado, `false` en caso contrario.
     */
    override suspend fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    /**
     * Obtiene el ID del usuario actualmente autenticado.
     *
     * @return UID del usuario o `null` si no hay sesión.
     */
    override suspend fun getCurrentUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }

    /**
     * Obtiene el perfil del usuario desde Firestore.
     *
     * @param userId ID del usuario a buscar.
     * @return [FirestoreUser] con los datos del perfil.
     * @throws Exception si no se puede obtener el documento.
     */
    private suspend fun getUserFromFirestore(userId: String): FirestoreUser {
        val doc = firestore.collection("users").document(userId).get().await()
        return doc.toObject(FirestoreUser::class.java) ?: FirestoreUser(id = userId)
    }
}