package com.example.aprendejapones.data.local.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "lessons",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["userId"])]
)
data class LessonEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: String,
    val lessonType: String,
    val lessonName: String,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val xpEarned: Int,
    val timeSpentSeconds: Int,
    val completedAt: Long = System.currentTimeMillis()
)
