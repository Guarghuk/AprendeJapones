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
 * Worker que envía recordatorios diarios para estudiar japonés.
 *
 * Este worker se programa para los días y horas configurados por el usuario
 * en la pantalla de recordatorios. Envía notificaciones para motivar
 * al usuario a mantener su práctica diaria.
 *
 * ## Funcionamiento
 * 1. Verifica si el usuario ya estudió hoy usando [StreakManager]
 * 2. Si no ha estudiado, envía un recordatorio
 * 3. Si tiene racha activa, también envía alerta de racha en peligro
 *
 * ## Configuración
 * - [KEY_MOTIVATIONAL]: Si es `true`, usa mensajes motivacionales variados
 * - Programado por [WorkManagerScheduler.scheduleReminders]
 *
 * ## Notificaciones Enviadas
 * - **Recordatorio diario:** Mensaje estándar o motivacional
 * - **Alerta de racha:** Si tiene racha y no ha estudiado
 *
 * @param context Contexto de la aplicación (inyectado por WorkManager).
 * @param workerParams Parámetros del worker incluyendo inputData.
 * @param notificationHelper Helper para mostrar notificaciones.
 * @param streakManager Manager para verificar estado de estudio.
 *
 * @see WorkManagerScheduler Programa este worker.
 * @see NotificationHelper Muestra las notificaciones.
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val notificationHelper: NotificationHelper,
    private val streakManager: StreakManager
) : CoroutineWorker(context, workerParams) {

    /**
     * Ejecuta la lógica del recordatorio.
     *
     * Solo envía notificaciones si el usuario no ha estudiado hoy.
     * Si tiene racha activa, envía también alerta de racha en peligro.
     *
     * @return [Result.success] si se completó correctamente,
     *         [Result.retry] si ocurrió un error.
     */
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
        /**
         * Clave para el parámetro de mensajes motivacionales en inputData.
         *
         * Si es `true`, el recordatorio usará mensajes motivacionales
         * variados en lugar del mensaje estándar.
         */
        const val KEY_MOTIVATIONAL = "motivational_messages"
    }
}