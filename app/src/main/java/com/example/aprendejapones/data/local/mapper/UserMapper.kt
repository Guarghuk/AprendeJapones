package com.example.aprendejapones.data.mapper

import com.example.aprendejapones.data.local.database.entity.UserEntity
import com.example.aprendejapones.domain.model.FirestoreUser
import com.example.aprendejapones.domain.model.User
import java.text.SimpleDateFormat
import java.util.*

object UserMapper {

    private val dateFormat = SimpleDateFormat("MMMM yyyy", Locale("es", "ES"))

    fun UserEntity.toDomain(): User {
        return User(
            id = id,
            username = username,
            rank = rank,
            level = level,
            currentXP = currentXP,
            maxXP = maxXP,
            streak = streak,
            drops = drops,
            memberSince = dateFormat.format(Date(memberSince)),
            avatarLetter = avatarLetter
        )
    }

    fun User.toEntity(
        isActive: Boolean = true,
        memberSinceTimestamp: Long = System.currentTimeMillis()
    ): UserEntity {
        return UserEntity(
            id = id,
            username = username,
            rank = rank,
            level = level,
            currentXP = currentXP,
            maxXP = maxXP,
            streak = streak,
            drops = drops,
            memberSince = memberSinceTimestamp,
            avatarLetter = avatarLetter,
            isActive = isActive
        )
    }

    /**
     * Convert local User model to FirestoreUser for cloud sync
     */
    fun User.toFirestoreUser(): FirestoreUser {
        return FirestoreUser(
            id = id,
            username = username,
            rank = rank,
            level = level,
            xp = currentXP,
            streak = streak,
            drops = drops
        )
    }

    /**
     * Convert FirestoreUser to local User model
     */
    fun FirestoreUser.toUser(): User {
        return User(
            id = id,
            username = username,
            rank = rank,
            level = level,
            currentXP = xp,
            maxXP = level * 100, // Calculate maxXP based on level
            streak = streak,
            drops = drops,
            memberSince = dateFormat.format(Date(createdAt)),
            avatarLetter = username.firstOrNull()?.toString() ?: "K"
        )
    }
}
