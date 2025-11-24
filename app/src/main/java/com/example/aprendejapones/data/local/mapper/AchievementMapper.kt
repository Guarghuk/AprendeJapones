package com.example.aprendejapones.data.mapper

import com.example.aprendejapones.data.local.database.entity.AchievementEntity
import com.example.aprendejapones.utils.Achievement

object AchievementMapper {

    fun AchievementEntity.toDomain(): Achievement {
        return Achievement(
            id = achievementId,
            icon = icon,
            title = title,
            description = description,
            isUnlocked = isUnlocked
        )
    }

    fun Achievement.toEntity(userId: String): AchievementEntity {
        return AchievementEntity(
            userId = userId,
            achievementId = id,
            icon = icon,
            title = title,
            description = description,
            isUnlocked = isUnlocked
        )
    }

    fun List<AchievementEntity>.toDomainList(): List<Achievement> {
        return map { it.toDomain() }
    }

    fun List<Achievement>.toEntityList(userId: String): List<AchievementEntity> {
        return map { it.toEntity(userId) }
    }
}