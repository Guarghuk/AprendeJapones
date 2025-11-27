package com.example.aprendejapones.data.repository

import com.example.aprendejapones.data.local.database.dao.ProgressDao
import com.example.aprendejapones.data.local.database.dao.UserDao
import com.example.aprendejapones.data.local.database.entity.ProgressEntity
import com.example.aprendejapones.domain.repository.CategoryProgress
import com.example.aprendejapones.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProgressRepositoryImpl @Inject constructor(
    private val progressDao: ProgressDao,
    private val userDao: UserDao
) : ProgressRepository {

    override fun getUserProgressFlow(): Flow<List<CategoryProgress>> {
        return userDao.getCurrentUserFlow().map { user ->
            user?.let {
                progressDao.getAllProgress(it.id).map { entity ->
                    CategoryProgress(
                        category = entity.category,
                        progress = entity.progress,
                        itemsLearned = entity.itemsLearned,
                        totalItems = entity.totalItems
                    )
                }
            } ?: emptyList()
        }
    }

    override suspend fun getAllProgress(): List<CategoryProgress> {
        val user = userDao.getCurrentUser() ?: return emptyList()
        return progressDao.getAllProgress(user.id).map { entity ->
            CategoryProgress(
                category = entity.category,
                progress = entity.progress,
                itemsLearned = entity.itemsLearned,
                totalItems = entity.totalItems
            )
        }
    }

    override suspend fun getProgressByCategory(category: String): CategoryProgress? {
        val user = userDao.getCurrentUser() ?: return null
        val entity = progressDao.getProgressByCategory(user.id, category) ?: return null

        return CategoryProgress(
            category = entity.category,
            progress = entity.progress,
            itemsLearned = entity.itemsLearned,
            totalItems = entity.totalItems
        )
    }

    override suspend fun updateProgress(
        category: String,
        progressPercent: Int,
        itemsLearned: Int
    ) {
        val user = userDao.getCurrentUser() ?: return

        val existing = progressDao.getProgressByCategory(user.id, category)

        if (existing != null) {
            progressDao.updateProgressByCategory(
                userId = user.id,
                category = category,
                progressPercent = progressPercent,
                itemsLearned = itemsLearned
            )
        } else {
            progressDao.insertProgress(
                ProgressEntity(
                    userId = user.id,
                    category = category,
                    progress = progressPercent,
                    itemsLearned = itemsLearned,
                    totalItems = getTotalItemsForCategory(category),
                    lastStudiedAt = System.currentTimeMillis()
                )
            )
        }
    }

    override suspend fun initializeDefaultProgress() {
        val user = userDao.getCurrentUser() ?: return

        val categories = listOf(
            Triple("hiragana", 0, 46),
            Triple("katakana", 0, 46),
            Triple("kanji", 0, 2136),
            Triple("grammar", 0, 100),
            Triple("vocabulary", 0, 1000)
        )

        categories.forEach { (category, learned, total) ->
            val existing = progressDao.getProgressByCategory(user.id, category)
            if (existing == null) {
                progressDao.insertProgress(
                    ProgressEntity(
                        userId = user.id,
                        category = category,
                        progress = 0,
                        itemsLearned = learned,
                        totalItems = total
                    )
                )
            }
        }
    }

    private fun getTotalItemsForCategory(category: String): Int {
        return when (category) {
            "hiragana" -> 46
            "katakana" -> 46
            "kanji" -> 2136
            "grammar" -> 100
            "vocabulary" -> 1000
            else -> 100
        }
    }
}
