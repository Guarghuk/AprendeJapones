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
 * Extension function to convert FirebaseUser to FirestoreUser
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

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

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

    override suspend fun loginWithEmail(
        email: String,
        password: String
    ): Result<FirestoreUser> = withContext(Dispatchers.IO) {
        try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user ?: return@withContext Result.failure(
                Exception("User is null")
            )

            // Obtener datos de Firestore
            val firestoreUser = getUserFromFirestore(user.uid)
            Result.success(firestoreUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

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

    override suspend fun logout() {
        firebaseAuth.signOut()
    }

    override suspend fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override suspend fun getCurrentUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }

    private suspend fun getUserFromFirestore(userId: String): FirestoreUser {
        val doc = firestore.collection("users").document(userId).get().await()
        return doc.toObject(FirestoreUser::class.java) ?: FirestoreUser(id = userId)
    }
}