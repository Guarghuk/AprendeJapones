package com.example.aprendejapones.data.local.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "progress",
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
data class ProgressEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: String,
    val category: String, // "hiragana", "katakana", "kanji", "grammar", "vocabulary"
    val progress: Int, // Porcentaje 0-100
    val itemsLearned: Int,
    val totalItems: Int,
    val lastStudiedAt: Long? = null,
    val updatedAt: Long = System.currentTimeMillis()
)