package com.example.aprendejapones.domain.repository

import kotlinx.coroutines.flow.Flow

data class CategoryProgress(
    val category: String,
    val progress: Int,
    val itemsLearned: Int,
    val totalItems: Int
)

interface ProgressRepository {

    /**
     * Get all progress categories as Flow
     */
    fun getUserProgressFlow(): Flow<List<CategoryProgress>>

    /**
     * Get all progress (one-time)
     */
    suspend fun getAllProgress(): List<CategoryProgress>

    /**
     * Get progress for specific category
     */
    suspend fun getProgressByCategory(category: String): CategoryProgress?

    /**
     * Update progress for a category
     */
    suspend fun updateProgress(
        category: String,
        progressPercent: Int,
        itemsLearned: Int
    )

    /**
     * Initialize default categories if not exist
     */
    suspend fun initializeDefaultProgress()
}