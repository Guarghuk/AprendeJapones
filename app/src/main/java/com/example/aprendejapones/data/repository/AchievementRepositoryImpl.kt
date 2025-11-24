package com.example.aprendejapones.data.repository

import com.example.aprendejapones.data.local.database.dao.AchievementDao
import com.example.aprendejapones.data.local.database.dao.UserDao
import com.example.aprendejapones.data.mapper.AchievementMapper.toDomainList
import com.example.aprendejapones.data.mapper.AchievementMapper.toEntity
import com.example.aprendejapones.domain.repository.AchievementRepository
import com.example.aprendejapones.utils.Achievement
import com.example.aprendejapones.utils.MockData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AchievementRepositoryImpl @Inject constructor(
    private val achievementDao: AchievementDao,
    private val userDao: UserDao
) : AchievementRepository {

    override fun getUserAchievementsFlow(): Flow<List<Achievement>> {
        return userDao.getCurrentUserFlow().map { user ->
            user?.let {
                achievementDao.getUserAchievements(it.id).toDomainList()
            } ?: emptyList()
        }
    }

    override suspend fun getUserAchievements(): List<Achievement> {
        val user = userDao.getCurrentUser() ?: return emptyList()
        return achievementDao.getUserAchievements(user.id).toDomainList()
    }

    override suspend fun initializeDefaultAchievements() {
        val user = userDao.getCurrentUser() ?: return

        // Check if already initialized
        val existing = achievementDao.getUserAchievements(user.id)
        if (existing.isNotEmpty()) return

        // Get default achievements from MockData
        val defaultAchievements = MockData.getMockAchievements()
        val entities = defaultAchievements.map { it.toEntity(user.id) }

        achievementDao.insertAchievements(entities)
    }

    override suspend fun unlockAchievement(achievementId: String) {
        val user = userDao.getCurrentUser() ?: return
        achievementDao.unlockAchievement(user.id, achievementId)
    }

    override suspend fun updateAchievementProgress(achievementId: String, progress: Int) {
        val user = userDao.getCurrentUser() ?: return
        achievementDao.updateAchievementProgress(user.id, achievementId, progress)

        // Auto-unlock if progress reaches 100%
        if (progress >= 100) {
            achievementDao.unlockAchievement(user.id, achievementId)
        }
    }

    override suspend fun checkAchievements() {
        val user = userDao.getCurrentUser() ?: return

        // Check "First Step" achievement (complete first lesson)
        // Check "Dedicated Student" achievement (7 day streak)
        // etc... implement achievement checking logic here

        if (user.streak >= 7) {
            unlockAchievement("2") // Dedicated Student
        }
    }
}
