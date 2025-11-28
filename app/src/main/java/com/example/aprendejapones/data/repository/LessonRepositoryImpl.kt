package com.example.aprendejapones.data.repository

import com.example.aprendejapones.data.local.database.dao.HistorialLeccionesDao
import com.example.aprendejapones.data.local.database.dao.UsuariosLocalDao
import com.example.aprendejapones.data.local.database.entity.HistorialLeccionesEntity
import com.example.aprendejapones.domain.model.DailyChallenge
import com.example.aprendejapones.domain.repository.LessonRepository
import com.example.aprendejapones.domain.repository.LessonStats
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LessonRepositoryImpl @Inject constructor(
    private val historialLeccionesDao: HistorialLeccionesDao,
    private val usuariosLocalDao: UsuariosLocalDao
) : LessonRepository {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    
    // In-memory daily challenge state with thread safety
    private val mutex = Mutex()
    @Volatile
    private var dailyChallenge: DailyChallengeState? = null

    override suspend fun saveLesson(
        lessonType: String,
        lessonName: String,
        totalQuestions: Int,
        correctAnswers: Int,
        xpEarned: Int,
        timeSpentSeconds: Int
    ) {
        val usuario = usuariosLocalDao.getCurrentUsuario() ?: return

        val historial = HistorialLeccionesEntity(
            idUsuario = usuario.idUsuario,
            idLeccion = "$lessonType-$lessonName",
            fechaCompletado = System.currentTimeMillis(),
            respuestasCorrectas = correctAnswers,
            totalPreguntas = totalQuestions,
            xpGanada = xpEarned,
            tiempoTardadoSeg = timeSpentSeconds
        )

        historialLeccionesDao.insertHistorial(historial)

        // Update daily challenge progress
        updateChallengeProgress()
    }

    override suspend fun getLessonStats(): LessonStats {
        val usuario = usuariosLocalDao.getCurrentUsuario() ?: return LessonStats(0, 0, 0)

        return LessonStats(
            totalLessonsCompleted = historialLeccionesDao.getTotalLeccionesCompletadas(usuario.idUsuario),
            totalStudyTimeMinutes = (historialLeccionesDao.getTotalTiempoEstudio(usuario.idUsuario) ?: 0) / 60,
            totalXPEarned = historialLeccionesDao.getTotalXPGanada(usuario.idUsuario) ?: 0
        )
    }

    override suspend fun getTodayChallenge(): DailyChallenge? {
        val today = dateFormat.format(Date())
        
        return mutex.withLock {
            // Initialize if needed or if date changed
            if (dailyChallenge == null || dailyChallenge?.date != today) {
                dailyChallenge = DailyChallengeState(
                    date = today,
                    completed = 0,
                    total = 5
                )
            }
            dailyChallenge?.toDomain(calculateTimeRemaining())
        }
    }

    override fun getTodayChallengeFlow(): Flow<DailyChallenge?> {
        return usuariosLocalDao.getCurrentUsuarioFlow().map { usuario ->
            usuario?.let {
                getTodayChallenge()
            }
        }
    }

    override suspend fun updateChallengeProgress() {
        val today = dateFormat.format(Date())
        
        mutex.withLock {
            val currentChallenge = dailyChallenge
            if (currentChallenge != null && 
                currentChallenge.date == today && 
                currentChallenge.completed < currentChallenge.total) {
                dailyChallenge = currentChallenge.copy(
                    completed = currentChallenge.completed + 1
                )
            }
        }
    }

    override suspend fun initializeTodayChallenge() {
        val today = dateFormat.format(Date())
        
        mutex.withLock {
            if (dailyChallenge == null || dailyChallenge?.date != today) {
                dailyChallenge = DailyChallengeState(
                    date = today,
                    completed = 0,
                    total = 5
                )
            }
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
    
    // Internal data class for daily challenge state
    private data class DailyChallengeState(
        val date: String,
        val completed: Int,
        val total: Int
    ) {
        fun toDomain(timeRemaining: String): DailyChallenge {
            return DailyChallenge(
                id = date,
                completed = completed,
                total = total,
                timeRemaining = timeRemaining,
                rewardXP = 50
            )
        }
    }
}