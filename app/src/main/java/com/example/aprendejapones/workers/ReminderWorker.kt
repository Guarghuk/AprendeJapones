package com.example.aprendejapones.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.aprendejapones.domain.manager.StreakManager
import com.example.aprendejapones.notification.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * Worker que envía recordatorios diarios para estudiar
 */
@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val notificationHelper: NotificationHelper,
    private val streakManager: StreakManager
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            // Obtener si debe usar mensajes motivacionales
            val useMotivational = inputData.getBoolean(KEY_MOTIVATIONAL, false)

            // Verificar si ya estudió hoy
            val hasStudiedToday = streakManager.hasStudiedToday()

            if (!hasStudiedToday) {
                // No ha estudiado hoy, enviar recordatorio
                notificationHelper.showDailyReminder(useMotivational)

                // Si tiene racha, enviar también alerta de racha
                val currentStreak = streakManager.getCurrentStreak()
                if (currentStreak > 0) {
                    notificationHelper.showStreakWarning(currentStreak)
                }
            }

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        const val KEY_MOTIVATIONAL = "motivational_messages"
    }
}