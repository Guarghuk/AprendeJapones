package com.example.aprendejapones.data.mapper

import com.example.aprendejapones.data.local.database.entity.UserEntity
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
}
