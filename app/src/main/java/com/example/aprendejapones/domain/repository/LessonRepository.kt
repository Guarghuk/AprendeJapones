package com.example.aprendejapones.domain.repository

import com.example.aprendejapones.domain.model.DailyChallenge
import kotlinx.coroutines.flow.Flow

data class LessonStats(
    val totalLessonsCompleted: Int,
    val totalStudyTimeMinutes: Int,
    val totalXPEarned: Int
)

interface LessonRepository {

    /**
     * Save completed lesson
     */
    suspend fun saveLesson(
        lessonType: String,
        lessonName: String,
        totalQuestions: Int,
        correctAnswers: Int,
        xpEarned: Int,
        timeSpentSeconds: Int
    )

    /**
     * Get lesson statistics
     */
    suspend fun getLessonStats(): LessonStats

    /**
     * Get today's daily challenge
     */
    suspend fun getTodayChallenge(): DailyChallenge?

    /**
     * Get daily challenge as Flow
     */
    fun getTodayChallengeFlow(): Flow<DailyChallenge?>

    /**
     * Update daily challenge progress
     */
    suspend fun updateChallengeProgress()

    /**
     * Create today's challenge if doesn't exist
     */
    suspend fun initializeTodayChallenge()
}