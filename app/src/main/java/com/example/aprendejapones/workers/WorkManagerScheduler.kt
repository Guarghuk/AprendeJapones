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
    /**
     * Programa recordatorios diarios según la configuración del usuario
     */
    fun scheduleReminders(
        hour: Int,
        minute: Int,
        selectedDays: Set<String>,
        motivationalMessages: Boolean
    ) {
        // Cancelar recordatorios anteriores
        cancelReminders()

        // Convertir días seleccionados a números (1 = Lunes, 7 = Domingo)
        val dayNumbers = selectedDays.mapNotNull { dayNameToDayNumber(it) }

        dayNumbers.forEach { dayNumber ->
            scheduleReminderForDay(dayNumber, hour, minute, motivationalMessages)
        }
    }

    /**
     * Programa un recordatorio para un día específico
     */
    private fun scheduleReminderForDay(
        dayOfWeek: Int,
        hour: Int,
        minute: Int,
        motivationalMessages: Boolean
    ) {
        val currentTime = Calendar.getInstance()
        val scheduledTime = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, dayOfWeek)
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)

            // Si ya pasó esta semana, programar para la próxima
            if (before(currentTime)) {
                add(Calendar.WEEK_OF_YEAR, 1)
            }
        }

        val delay = scheduledTime.timeInMillis - currentTime.timeInMillis

        val inputData = workDataOf(
            ReminderWorker.KEY_MOTIVATIONAL to motivationalMessages
        )

        val reminderRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .addTag(REMINDER_WORK_TAG)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "$REMINDER_WORK_NAME$dayOfWeek",
            ExistingWorkPolicy.REPLACE,
            reminderRequest
        )
    }

    /**
     * Cancela todos los recordatorios programados
     */
    fun cancelReminders() {
        WorkManager.getInstance(context).cancelAllWorkByTag(REMINDER_WORK_TAG)
    }

    /**
     * Convierte nombre de día a número (Calendar.DAY_OF_WEEK)
     */
    private fun dayNameToDayNumber(dayName: String): Int? {
        return when (dayName) {
            "Lunes" -> Calendar.MONDAY
            "Martes" -> Calendar.TUESDAY
            "Miércoles" -> Calendar.WEDNESDAY
            "Jueves" -> Calendar.THURSDAY
            "Viernes" -> Calendar.FRIDAY
            "Sábado" -> Calendar.SATURDAY
            "Domingo" -> Calendar.SUNDAY
            else -> null
        }
    }


    companion object {
        private const val STREAK_WORK_NAME = "streak_check_work"
        private const val STREAK_WORK_TAG = "streak_check"


        private const val REMINDER_WORK_NAME = "reminder_work"
        private const val REMINDER_WORK_TAG = "reminder"
    }
}