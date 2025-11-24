package com.example.aprendejapones.data.local.database.dao

import androidx.room.*
import com.example.aprendejapones.data.local.database.entity.AchievementEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AchievementDao {

    @Query("SELECT * FROM achievements WHERE userId = :userId ORDER BY isUnlocked DESC, title ASC")
    fun getUserAchievementsFlow(userId: String): Flow<List<AchievementEntity>>

    @Query("SELECT * FROM achievements WHERE userId = :userId")
    suspend fun getUserAchievements(userId: String): List<AchievementEntity>

    @Query("SELECT * FROM achievements WHERE userId = :userId AND isUnlocked = 1")
    suspend fun getUnlockedAchievements(userId: String): List<AchievementEntity>

    @Query("SELECT * FROM achievements WHERE userId = :userId AND achievementId = :achievementId")
    suspend fun getAchievement(userId: String, achievementId: String): AchievementEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAchievement(achievement: AchievementEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAchievements(achievements: List<AchievementEntity>)

    @Update
    suspend fun updateAchievement(achievement: AchievementEntity)

    @Query("""
        UPDATE achievements 
        SET isUnlocked = 1, 
            unlockedAt = :timestamp
        WHERE userId = :userId AND achievementId = :achievementId
    """)
    suspend fun unlockAchievement(
        userId: String,
        achievementId: String,
        timestamp: Long = System.currentTimeMillis()
    )

    @Query("""
        UPDATE achievements 
        SET progress = :progress
        WHERE userId = :userId AND achievementId = :achievementId
    """)
    suspend fun updateAchievementProgress(
        userId: String,
        achievementId: String,
        progress: Int
    )

    @Query("DELETE FROM achievements WHERE userId = :userId")
    suspend fun deleteUserAchievements(userId: String)

    @Query("SELECT COUNT(*) FROM achievements WHERE userId = :userId AND isUnlocked = 1")
    suspend fun getUnlockedCount(userId: String): Int
}
