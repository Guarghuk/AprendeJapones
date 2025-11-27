package com.example.aprendejapones.domain.repository

import android.net.Uri
import com.example.aprendejapones.domain.model.FirestoreUser
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Firestore user operations
 */
interface FirestoreUserRepository {
    /**
     * Get user profile as a Flow (real-time updates)
     */
    fun getUserProfileFlow(userId: String): Flow<FirestoreUser?>

    /**
     * Get user profile (one-time fetch)
     */
    suspend fun getUserProfile(userId: String): FirestoreUser?

    /**
     * Update user profile in Firestore
     */
    suspend fun updateUserProfile(user: FirestoreUser): Result<Unit>

    /**
     * Upload profile photo to Firebase Storage and return the URL
     */
    suspend fun uploadProfilePhoto(userId: String, uri: Uri): Result<String>

    /**
     * Update specific user fields
     */
    suspend fun updateUserFields(userId: String, fields: Map<String, Any>): Result<Unit>

    /**
     * Sync local user data to Firestore
     */
    suspend fun syncUserToFirestore(user: FirestoreUser): Result<Unit>

    /**
     * Get current user's Firestore profile
     */
    suspend fun getCurrentUserProfile(): FirestoreUser?

    /**
     * Add XP to user and update level if necessary
     */
    suspend fun addXpToUser(userId: String, xp: Int): Result<Unit>

    /**
     * Update user streak
     */
    suspend fun updateStreak(userId: String, streak: Int): Result<Unit>
}
