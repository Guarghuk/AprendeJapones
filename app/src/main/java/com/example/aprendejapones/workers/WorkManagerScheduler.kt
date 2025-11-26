package com.example.aprendejapones.workers

import android.content.Context
import androidx.work.*
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Scheduler para programar Workers periódicos
 */
@Singleton
class WorkManagerScheduler @Inject constructor(
        @ApplicationContext private val context: Context
) {

    /**
     * Programa la verificación diaria de racha a medianoche
     */
    fun scheduleStreakCheck() {
        val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(false) // Ejecutar aunque batería esté baja
                .build()

        // Calcular delay hasta medianoche
        val currentTime = Calendar.getInstance()
        val midnight = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)

            // Si ya pasó medianoche hoy, programar para mañana
            if (before(currentTime)) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        val delayMillis = midnight.timeInMillis - currentTime.timeInMillis

        val streakRequest = PeriodicWorkRequestBuilder<StreakWorker>(
                repeatInterval = 1,
                repeatIntervalTimeUnit = TimeUnit.DAYS
        )
            .setConstraints(constraints)
                .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
                .addTag(STREAK_WORK_TAG)
                .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                STREAK_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP, // No reemplazar si ya existe
                streakRequest
        )
    }

    /**
     * Cancela la verificación de racha
     */
    fun cancelStreakCheck() {
        WorkManager.getInstance(context).cancelUniqueWork(STREAK_WORK_NAME)
    }

    companion object {
        private const val STREAK_WORK_NAME = "streak_check_work"
        private const val STREAK_WORK_TAG = "streak_check"
    }
}