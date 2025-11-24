package com.example.aprendejapones.domain.repository

import com.example.aprendejapones.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    /**
     * Get current user as Flow (reactive)
     */
    fun getCurrentUserFlow(): Flow<User?>

    /**
     * Get current user (one-time)
     */
    suspend fun getCurrentUser(): User?

    /**
     * Create or get the local user with UUID
     */
    suspend fun getOrCreateUser(): User

    /**
     * Update user profile
     */
    suspend fun updateUser(user: User)

    /**
     * Add XP and handle level ups
     */
    suspend fun addXP(xp: Int): User

    /**
     * Update streak
     */
    suspend fun updateStreak(streak: Int)

    /**
     * Add drops (currency)
     */
    suspend fun addDrops(amount: Int)

    /**
     * Spend drops
     */
    suspend fun spendDrops(amount: Int): Boolean
}
