package com.example.aprendejapones.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.aprendejapones.domain.manager.StreakManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * Worker que verifica diariamente si el usuario ha perdido su racha
 * Se ejecuta una vez al día a medianoche
 */
@HiltWorker
class StreakWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val streakManager: StreakManager
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            // Verificar si perdió la racha
            streakManager.checkStreakExpiration()

            Result.success()
        } catch (e: Exception) {
            // Si falla, reintentar
            Result.retry()
        }
    }
}