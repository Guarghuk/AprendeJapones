package com.example.aprendejapones.data.local.database.dao

import androidx.room.*
import com.example.aprendejapones.data.local.database.entity.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones de Usuario
 */
@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE isActive = 1 LIMIT 1")
    fun getCurrentUserFlow(): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE isActive = 1 LIMIT 1")
    suspend fun getCurrentUser(): UserEntity?

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("UPDATE users SET currentXP = :xp, level = :level, updatedAt = :timestamp WHERE id = :userId")
    suspend fun updateXP(userId: String, xp: Int, level: Int, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE users SET streak = :streak, updatedAt = :timestamp WHERE id = :userId")
    suspend fun updateStreak(userId: String, streak: Int, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE users SET drops = drops + :amount, updatedAt = :timestamp WHERE id = :userId")
    suspend fun addDrops(userId: String, amount: Int, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE users SET drops = drops - :amount, updatedAt = :timestamp WHERE id = :userId AND drops >= :amount")
    suspend fun spendDrops(userId: String, amount: Int, timestamp: Long = System.currentTimeMillis()): Int

    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()
}