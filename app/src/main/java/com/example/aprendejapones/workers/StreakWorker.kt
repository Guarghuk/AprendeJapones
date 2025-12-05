package com.example.aprendejapones.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.aprendejapones.domain.manager.StreakManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * Worker que verifica diariamente si el usuario ha perdido su racha de estudio.
 *
 * Este worker se ejecuta una vez al día (generalmente a medianoche) para
 * verificar si el usuario perdió su racha por no estudiar. Es programado
 * por [WorkManagerScheduler] y utiliza [StreakManager] para la lógica de negocio.
 *
 * ## Funcionamiento
 * 1. Se ejecuta a medianoche (configurado por WorkManagerScheduler)
 * 2. Verifica la última fecha de estudio
 * 3. Si pasó más de un día sin estudiar, reinicia la racha a 0
 *
 * ## Configuración con Hilt
 * Usa `@HiltWorker` y `@AssistedInject` para inyección de dependencias
 * con WorkManager, permitiendo inyectar [StreakManager] correctamente.
 *
 * ## Política de Reintentos
 * Si ocurre una excepción, retorna [Result.retry] para que WorkManager
 * vuelva a intentar la ejecución según su política de backoff.
 *
 * @param context Contexto de la aplicación (inyectado por WorkManager).
 * @param workerParams Parámetros del worker (inyectado por WorkManager).
 * @param streakManager Manager de racha para verificar expiración.
 *
 * @see WorkManagerScheduler Programa este worker.
 * @see StreakManager Contiene la lógica de verificación de racha.
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
@HiltWorker
class StreakWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val streakManager: StreakManager
) : CoroutineWorker(context, workerParams) {

    /**
     * Ejecuta la verificación de expiración de racha.
     *
     * @return [Result.success] si la verificación se completó correctamente,
     *         [Result.retry] si ocurrió un error y debe reintentarse.
     */
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