package com.example.aprendejapones.data.local.database.dao

import androidx.room.*
import com.example.aprendejapones.data.local.database.entity.DailyChallengeEntity
import com.example.aprendejapones.data.local.database.entity.LessonEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones de Lecciones
 */
@Dao
interface LessonDao {

    @Query("SELECT * FROM lessons WHERE userId = :userId ORDER BY completedAt DESC")
    fun getUserLessonsFlow(userId: String): Flow<List<LessonEntity>>

    @Query("SELECT * FROM lessons WHERE userId = :userId ORDER BY completedAt DESC LIMIT :limit")
    suspend fun getRecentLessons(userId: String, limit: Int = 10): List<LessonEntity>

    @Query("SELECT COUNT(*) FROM lessons WHERE userId = :userId")
    suspend fun getTotalLessonsCompleted(userId: String): Int

    @Query("SELECT SUM(timeSpentSeconds) FROM lessons WHERE userId = :userId")
    suspend fun getTotalStudyTime(userId: String): Int?

    @Query("SELECT SUM(xpEarned) FROM lessons WHERE userId = :userId")
    suspend fun getTotalXPEarned(userId: String): Int?

    @Insert
    suspend fun insertLesson(lesson: LessonEntity)

    @Query("DELETE FROM lessons WHERE userId = :userId")
    suspend fun deleteUserLessons(userId: String)

    // Daily Challenge
    @Query("SELECT * FROM daily_challenge WHERE userId = :userId AND date = :date")
    suspend fun getDailyChallenge(userId: String, date: String): DailyChallengeEntity?

    @Query("SELECT * FROM daily_challenge WHERE userId = :userId AND date = :date")
    fun getDailyChallengeFlow(userId: String, date: String): Flow<DailyChallengeEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyChallenge(challenge: DailyChallengeEntity)

    @Update
    suspend fun updateDailyChallenge(challenge: DailyChallengeEntity)

    @Query("""
        UPDATE daily_challenge 
        SET completed = :completed,
            isCompleted = CASE WHEN :completed >= total THEN 1 ELSE 0 END,
            completedAt = CASE WHEN :completed >= total THEN :timestamp ELSE NULL END
        WHERE userId = :userId AND date = :date
    """)
    suspend fun updateChallengeProgress(
        userId: String,
        date: String,
        completed: Int,
        timestamp: Long = System.currentTimeMillis()
    )
}