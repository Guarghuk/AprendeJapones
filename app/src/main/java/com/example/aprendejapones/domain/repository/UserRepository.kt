package com.example.aprendejapones.domain.repository

import com.example.aprendejapones.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz que define las operaciones que necesita domain respecto a usuarios.
 * Implementada en data/repository/UserRepositoryImpl
 */
interface UserRepository {
    suspend fun getUserById(id: Int): User?
    suspend fun getUserByUsername(username: String): User?
    suspend fun insertOrUpdate(user: User): Long
    fun observeAllUsers(): Flow<List<User>>
}