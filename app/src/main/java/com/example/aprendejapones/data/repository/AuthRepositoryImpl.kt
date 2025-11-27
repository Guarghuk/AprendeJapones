package com.example.aprendejapones.data.repository

import com.example.aprendejapones.domain.model.FirestoreUser
import com.example.aprendejapones.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

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

    private suspend fun getUserFromFirestore(userId: String): FirestoreUser {
        val doc = firestore.collection("users").document(userId).get().await()
        return doc.toObject(FirestoreUser::class.java) ?: FirestoreUser(id = userId)
    }
}