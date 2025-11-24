package com.example.aprendejapones.domain.repository

import com.example.aprendejapones.utils.Achievement
import kotlinx.coroutines.flow.Flow

interface AchievementRepository {

    /**
     * Get all achievements as Flow
     */
    fun getUserAchievementsFlow(): Flow<List<Achievement>>

    /**
     * Get achievements (one-time)
     */
    suspend fun getUserAchievements(): List<Achievement>

    /**
     * Initialize default achievements for new user
     */
    suspend fun initializeDefaultAchievements()

    /**
     * Unlock an achievement
     */
    suspend fun unlockAchievement(achievementId: String)

    /**
     * Update achievement progress
     */
    suspend fun updateAchievementProgress(achievementId: String, progress: Int)

    /**
     * Check and unlock achievements based on user stats
     */
    suspend fun checkAchievements()
}