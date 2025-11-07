package com.example.aprendejapones.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.aprendejapones.domain.model.User

/**
 * Entidad de Room para la tabla de usuarios
 * Mapeada directamente a la tabla SQLite
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val username: String,
    val rank: String,
    val level: Int,
    val currentXP: Int,
    val maxXP: Int,
    val streak: Int,
    val drops: Int,
    val memberSince: String
)

/**
 * Mappers entre Entity y Domain Model
 */
fun UserEntity.toDomain() = User(
    id = id,
    username = username,
    rank = rank,
    level = level,
    currentXP = currentXP,
    maxXP = maxXP,
    streak = streak,
    drops = drops,
    memberSince = memberSince
)

fun User.toEntity() = UserEntity(
    id = id,
    username = username,
    rank = rank,
    level = level,
    currentXP = currentXP,
    maxXP = maxXP,
    streak = streak,
    drops = drops,
    memberSince = memberSince
)