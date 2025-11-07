package com.example.aprendejapones.domain.repository

import com.example.aprendejapones.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Interface del repositorio de usuarios
 * Define el contrato para acceder a datos de usuario
 */
interface UserRepository {

    /**
     * Obtiene un usuario por su ID
     */
    suspend fun getUserById(userId: String): User?

    /**
     * Obtiene un usuario por su username
     */
    suspend fun getUserByUsername(username: String): User?

    /**
     * Inserta o actualiza un usuario
     * @return ID del usuario insertado/actualizado
     */
    suspend fun insertOrUpdate(user: User): Long

    /**
     * Observa todos los usuarios (Flow reactivo)
     */
    fun observeAllUsers(): Flow<List<User>>

    /**
     * Actualiza el XP de un usuario
     */
    suspend fun updateUserXP(userId: String, newXP: Int, newLevel: Int)

    /**
     * Actualiza la racha de un usuario
     */
    suspend fun updateStreak(userId: String, newStreak: Int)

    /**
     * Actualiza los drops de un usuario
     */
    suspend fun updateDrops(userId: String, newDrops: Int)
}