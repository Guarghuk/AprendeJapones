package com.example.aprendejapones.data.repository

import android.net.Uri
import com.example.aprendejapones.domain.model.FirestoreUser
import com.example.aprendejapones.domain.repository.AuthRepository
import com.example.aprendejapones.domain.repository.FirestoreUserRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreUserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val authRepository: AuthRepository
) : FirestoreUserRepository {

    private val usersCollection = firestore.collection("users")

    override fun getUserProfileFlow(userId: String): Flow<FirestoreUser?> = callbackFlow {
        val listener = usersCollection.document(userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val user = snapshot?.toObject(FirestoreUser::class.java)
                trySend(user)
            }

        awaitClose { listener.remove() }
    }

    override suspend fun getUserProfile(userId: String): FirestoreUser? = withContext(Dispatchers.IO) {
        try {
            val doc = usersCollection.document(userId).get().await()
            doc.toObject(FirestoreUser::class.java)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun updateUserProfile(user: FirestoreUser): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val updatedUser = user.copy(updatedAt = System.currentTimeMillis())
            usersCollection.document(user.id)
                .set(updatedUser, SetOptions.merge())
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun uploadProfilePhoto(userId: String, uri: Uri): Result<String> = withContext(Dispatchers.IO) {
        try {
            val ref = storage.reference.child("profile_photos/$userId.jpg")
            ref.putFile(uri).await()
            val downloadUrl = ref.downloadUrl.await()
            
            // Update user profile with new photo URL
            usersCollection.document(userId)
                .update("photoUrl", downloadUrl.toString())
                .await()
            
            Result.success(downloadUrl.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUserFields(userId: String, fields: Map<String, Any>): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val fieldsWithTimestamp = fields.toMutableMap().apply {
                put("updatedAt", System.currentTimeMillis())
            }
            usersCollection.document(userId)
                .update(fieldsWithTimestamp)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun syncUserToFirestore(user: FirestoreUser): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            usersCollection.document(user.id)
                .set(user, SetOptions.merge())
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUserProfile(): FirestoreUser? = withContext(Dispatchers.IO) {
        val userId = authRepository.getCurrentUserId() ?: return@withContext null
        getUserProfile(userId)
    }

    override suspend fun addXpToUser(userId: String, xp: Int): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            firestore.runTransaction { transaction ->
                val docRef = usersCollection.document(userId)
                val snapshot = transaction.get(docRef)
                val currentUser = snapshot.toObject(FirestoreUser::class.java) ?: return@runTransaction

                val newXp = currentUser.xp + xp
                var newLevel = currentUser.level
                var remainingXp = newXp

                // Level up logic (100 XP per level)
                while (remainingXp >= 100) {
                    newLevel++
                    remainingXp -= 100
                }

                transaction.update(docRef, mapOf(
                    "xp" to remainingXp,
                    "level" to newLevel,
                    "updatedAt" to System.currentTimeMillis()
                ))
            }.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateStreak(userId: String, streak: Int): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            usersCollection.document(userId)
                .update(mapOf(
                    "streak" to streak,
                    "updatedAt" to System.currentTimeMillis()
                ))
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
