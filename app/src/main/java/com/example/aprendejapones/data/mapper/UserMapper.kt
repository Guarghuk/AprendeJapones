package com.example.aprendejapones.data.mapper

import com.example.aprendejapones.domain.model.FirestoreUser
import com.example.aprendejapones.domain.model.User

/**
 * Extension function to convert domain User to FirestoreUser
 */
fun User.toFirestoreUser(): FirestoreUser {
    return FirestoreUser(
        id = id,
        username = username,
        email = "",
        photoUrl = null,
        rank = rank,
        level = level,
        xp = currentXP,
        streak = streak,
        drops = drops,
        bio = null,
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis()
    )
}
