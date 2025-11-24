package com.example.aprendejapones.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad Room para Usuario
 * Representa la tabla de usuarios en la base de datos local
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String, // UUID generado
    val username: String,
    val rank: String,
    val level: Int,
    val currentXP: Int,
    val maxXP: Int,
    val streak: Int,
    val drops: Int,
    val memberSince: Long, // Timestamp
    val avatarLetter: String,
    val isActive: Boolean = true, // Usuario actual
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)