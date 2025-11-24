package com.example.aprendejapones.data.repository

import com.example.aprendejapones.data.local.database.dao.LessonDao
import com.example.aprendejapones.data.local.database.dao.UserDao
import com.example.aprendejapones.data.local.database.entity.DailyChallengeEntity
import com.example.aprendejapones.data.local.database.entity.LessonEntity
import com.example.aprendejapones.data.mapper.DailyChallengeMapper.toDomain
import com.example.aprendejapones.domain.model.DailyChallenge
import com.example.aprendejapones.domain.repository.LessonRepository
import com.example.aprendejapones.domain.repository.LessonStats
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LessonRepositoryImpl @Inject constructor(
    private val lessonDao: LessonDao,
    private val userDao: UserDao
) : LessonRepository {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override suspend fun saveLesson(
        lessonType: String,
        lessonName: String,
        totalQuestions: Int,
        correctAnswers: Int,
        xpEarned: Int,
        timeSpentSeconds: Int
    ) {
        val user = userDao.getCurrentUser() ?: return

        val lesson = LessonEntity(
            userId = user.id,
            lessonType = lessonType,
            lessonName = lessonName,
            totalQuestions = totalQuestions,
            correctAnswers = correctAnswers,
            xpEarned = xpEarned,
            timeSpentSeconds = timeSpentSeconds
        )

        lessonDao.insertLesson(lesson)

        // Update daily challenge progress
        updateChallengeProgress()
    }

    override suspend fun getLessonStats(): LessonStats {
        val user = userDao.getCurrentUser() ?: return LessonStats(0, 0, 0)

        return LessonStats(
            totalLessonsCompleted = lessonDao.getTotalLessonsCompleted(user.id),
            totalStudyTimeMinutes = (lessonDao.getTotalStudyTime(user.id) ?: 0) / 60,
            totalXPEarned = lessonDao.getTotalXPEarned(user.id) ?: 0
        )
    }

    override suspend fun getTodayChallenge(): DailyChallenge? {
        val user = userDao.getCurrentUser() ?: return null
        val today = dateFormat.format(Date())

        val entity = lessonDao.getDailyChallenge(user.id, today) ?: return null

        return entity.toDomain(calculateTimeRemaining())
    }

    override fun getTodayChallengeFlow(): Flow<DailyChallenge?> {
        val today = dateFormat.format(Date())

        return userDao.getCurrentUserFlow().map { user ->
            user?.let {
                lessonDao.getDailyChallenge(it.id, today)?.toDomain(calculateTimeRemaining())
            }
        }
    }

    override suspend fun updateChallengeProgress() {
        val user = userDao.getCurrentUser() ?: return
        val today = dateFormat.format(Date())

        val challenge = lessonDao.getDailyChallenge(user.id, today) ?: return

        if (!challenge.isCompleted) {
            val newCompleted = (challenge.completed + 1).coerceAtMost(challenge.total)
            lessonDao.updateChallengeProgress(user.id, today, newCompleted)
        }
    }

    override suspend fun initializeTodayChallenge() {
        val user = userDao.getCurrentUser() ?: return
        val today = dateFormat.format(Date())

        val existing = lessonDao.getDailyChallenge(user.id, today)

        if (existing == null) {
            lessonDao.insertDailyChallenge(
                DailyChallengeEntity(
                    userId = user.id,
                    date = today,
                    completed = 0,
                    total = 5,
                    isCompleted = false
                )
            )
        }
    }

    private fun calculateTimeRemaining(): String {
        val calendar = Calendar.getInstance()
        val endOfDay = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }

        val diff = endOfDay.timeInMillis - calendar.timeInMillis
        val hours = TimeUnit.MILLISECONDS.toHours(diff)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diff) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(diff) % 60

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}