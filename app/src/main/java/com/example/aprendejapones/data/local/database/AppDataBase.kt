package com.example.aprendejapones.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.aprendejapones.data.local.database.dao.*
import com.example.aprendejapones.data.local.database.entity.*

/**
 * Base de datos principal de la aplicación
 */
@Database(
    entities = [
        UserEntity::class,
        ProgressEntity::class,
        AchievementEntity::class,
        LessonEntity::class,
        DailyChallengeEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun progressDao(): ProgressDao
    abstract fun achievementDao(): AchievementDao
    abstract fun lessonDao(): LessonDao

    companion object {
        const val DATABASE_NAME = "kotodama_database"
    }
}