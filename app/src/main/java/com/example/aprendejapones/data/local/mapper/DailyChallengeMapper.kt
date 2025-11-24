package com.example.aprendejapones.data.mapper

import com.example.aprendejapones.data.local.database.entity.DailyChallengeEntity
import com.example.aprendejapones.domain.model.DailyChallenge

object DailyChallengeMapper {

    fun DailyChallengeEntity.toDomain(timeRemaining: String): DailyChallenge {
        return DailyChallenge(
            id = id.toString(),
            completed = completed,
            total = total,
            timeRemaining = timeRemaining,
            rewardXP = 50
        )
    }

    fun DailyChallenge.toEntity(userId: String, date: String): DailyChallengeEntity {
        return DailyChallengeEntity(
            userId = userId,
            date = date,
            completed = completed,
            total = total,
            isCompleted = completed >= total
        )
    }
}