package com.example.aprendejapones.data.repository

import com.example.aprendejapones.data.local.dao.UserDao
import com.example.aprendejapones.data.local.entities.toDomain
import com.example.aprendejapones.data.local.entities.toEntity
import com.example.aprendejapones.domain.model.User
import com.example.aprendejapones.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementación concreta del repositorio de usuarios.
 * Maneja la conversión entre Entity (Room) y Model (Domain)
 */
class UserRepositoryImpl(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun getUserById(id: Int): User? {
        return userDao.getById(userId)?.toDomain()
    }

    override suspend fun getUserByUsername(username: String): User? {
        return userDao.getByUsername(username)?.toDomain()
    }

    override suspend fun insertOrUpdate(user: User): Long {
        val entity = user.toEntity()
        return userDao.insert(entity)
    }

    override fun observeAllUsers(): Flow<List<User>> {
        return userDao.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun updateUserXP(userId: String, newXP: Int, newLevel: Int) {
        userDao.updateXP(userId, newXP, newLevel)
    }

    suspend fun updateStreak(userId: String, newStreak: Int) {
        userDao.updateStreak(userId, newStreak)
    }

    suspend fun updateDrops(userId: String, newDrops: Int) {
        userDao.updateDrops(userId, newDrops)
    }
}