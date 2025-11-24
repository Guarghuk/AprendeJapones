package com.example.aprendejapones.data.local.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
        tableName = "achievements",
        foreignKeys = [
                ForeignKey(
                        entity = UserEntity::class,
                        parentColumns = ["id"],
                        childColumns = ["userId"],
                        onDelete = ForeignKey.CASCADE
                )
        ],
        indices = [Index(value = ["userId", "achievementId"], unique = true)]
)
data class AchievementEntity(
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0,
        val userId: String,
        val achievementId: String, // ID único del logro
        val icon: String,
        val title: String,
        val description: String,
        val isUnlocked: Boolean = false,
        val unlockedAt: Long? = null,
        val progress: Int = 0,
        val target: Int = 100
)