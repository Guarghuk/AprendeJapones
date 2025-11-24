package com.example.aprendejapones.data.repository

import com.benasher44.uuid.uuid4
import com.example.aprendejapones.data.local.database.dao.UserDao
import com.example.aprendejapones.data.local.database.entity.UserEntity
import com.example.aprendejapones.data.mapper.UserMapper.toDomain
import com.example.aprendejapones.data.mapper.UserMapper.toEntity
import com.example.aprendejapones.domain.model.User
import com.example.aprendejapones.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {

    override fun getCurrentUserFlow(): Flow<User?> {
        return userDao.getCurrentUserFlow().map { it?.toDomain() }
    }

    override suspend fun getCurrentUser(): User? {
        return userDao.getCurrentUser()?.toDomain()
    }

    override suspend fun getOrCreateUser(): User {
        val existing = userDao.getCurrentUser()

        return if (existing != null) {
            existing.toDomain()
        } else {
            // Create new local user with UUID
            val newUser = User(
                id = uuid4().toString(),
                username = "Usuario",
                rank = "初心者", // Beginner
                level = 1,
                currentXP = 0,
                maxXP = 100,
                streak = 0,
                drops = 0,
                memberSince = "Enero 2025"
            )

            userDao.insertUser(newUser.toEntity())
            newUser
        }
    }

    override suspend fun updateUser(user: User) {
        userDao.updateUser(user.toEntity())
    }

    override suspend fun addXP(xp: Int): User {
        val user = getCurrentUser() ?: throw IllegalStateException("No user found")

        var newXP = user.currentXP + xp
        var newLevel = user.level
        var newMaxXP = user.maxXP

        // Level up logic
        while (newXP >= newMaxXP) {
            newXP -= newMaxXP
            newLevel++
            newMaxXP = calculateMaxXP(newLevel)
        }

        userDao.updateXP(user.id, newXP, newLevel)

        return user.copy(
            currentXP = newXP,
            level = newLevel,
            maxXP = newMaxXP
        )
    }

    override suspend fun updateStreak(streak: Int) {
        val user = getCurrentUser() ?: return
        userDao.updateStreak(user.id, streak)
    }

    override suspend fun addDrops(amount: Int) {
        val user = getCurrentUser() ?: return
        userDao.addDrops(user.id, amount)
    }

    override suspend fun spendDrops(amount: Int): Boolean {
        val user = getCurrentUser() ?: return false
        val rowsAffected = userDao.spendDrops(user.id, amount)
        return rowsAffected > 0
    }

    private fun calculateMaxXP(level: Int): Int {
        return 100 + (level - 1) * 50 // Simple progression
    }
}