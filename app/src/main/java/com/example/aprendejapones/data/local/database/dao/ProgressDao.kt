package com.example.aprendejapones.data.local.database.dao

import androidx.room.*
import com.example.aprendejapones.data.local.database.entity.ProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressDao {

    @Query("SELECT * FROM progress WHERE userId = :userId")
    fun getUserProgressFlow(userId: String): Flow<List<ProgressEntity>>

    @Query("SELECT * FROM progress WHERE userId = :userId AND category = :category")
    suspend fun getProgressByCategory(userId: String, category: String): ProgressEntity?

    @Query("SELECT * FROM progress WHERE userId = :userId")
    suspend fun getAllProgress(userId: String): List<ProgressEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: ProgressEntity)

    @Update
    suspend fun updateProgress(progress: ProgressEntity)

    @Query("""
        UPDATE progress 
        SET progress = :progressPercent, 
            itemsLearned = :itemsLearned,
            lastStudiedAt = :timestamp,
            updatedAt = :timestamp
        WHERE userId = :userId AND category = :category
    """)
    suspend fun updateProgressByCategory(
        userId: String,
        category: String,
        progressPercent: Int,
        itemsLearned: Int,
        timestamp: Long = System.currentTimeMillis()
    )

    @Query("DELETE FROM progress WHERE userId = :userId")
    suspend fun deleteUserProgress(userId: String)

    @Transaction
    suspend fun upsertProgress(progress: ProgressEntity) {
        val existing = getProgressByCategory(progress.userId, progress.category)
        if (existing != null) {
            updateProgress(progress.copy(id = existing.id))
        } else {
            insertProgress(progress)
        }
    }
}