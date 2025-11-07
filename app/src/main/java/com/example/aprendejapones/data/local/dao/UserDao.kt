package com.example.aprendejapones.data.local.dao

import androidx.room.*
import com.example.aprendejapones.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones de base de datos de usuarios
 */
@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getById(userId: String): UserEntity?

    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getByUsername(username: String): UserEntity?

    @Query("SELECT * FROM users")
    fun observeAll(): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity): Long

    @Update
    suspend fun update(user: UserEntity)

    @Delete
    suspend fun delete(user: UserEntity)

    @Query("UPDATE users SET currentXP = :newXP, level = :newLevel WHERE id = :userId")
    suspend fun updateXP(userId: String, newXP: Int, newLevel: Int)

    @Query("UPDATE users SET streak = :newStreak WHERE id = :userId")
    suspend fun updateStreak(userId: String, newStreak: Int)

    @Query("UPDATE users SET drops = :newDrops WHERE id = :userId")
    suspend fun updateDrops(userId: String, newDrops: Int)
}