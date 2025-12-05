package com.example.aprendejapones.workers

import android.content.Context
import androidx.work.*
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Scheduler para programar Workers periódicos con WorkManager.
 *
 * Esta clase centraliza la configuración y programación de todos los
 * workers de la aplicación, incluyendo verificación de racha y
 * recordatorios de estudio.
 *
 * ## Workers Gestionados
 * - [StreakWorker]: Verificación diaria de racha (medianoche)
 * - [ReminderWorker]: Recordatorios según configuración del usuario
 *
 * ## Características
 * - Cálculo automático de delay hasta la hora programada
 * - Uso de políticas `KEEP` y `REPLACE` según el caso
 * - Soporte para múltiples días de la semana
 *
 * ## Uso
 *
 * ```kotlin
 * // En KotodamaApplication
 * @Inject lateinit var workManagerScheduler: WorkManagerScheduler
 *
 * override fun onCreate() {
 *     super.onCreate()
 *     workManagerScheduler.scheduleStreakCheck()
 * }
 * ```
 *
 * @property context Contexto de aplicación para acceder a WorkManager.
 *
 * @see StreakWorker Worker de verificación de racha.
 * @see ReminderWorker Worker de recordatorios.
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
@Singleton
class WorkManagerScheduler @Inject constructor(
        @ApplicationContext private val context: Context
) {

    /**
     * Programa la verificación diaria de racha a medianoche.
     *
     * Usa `PeriodicWorkRequest` con intervalo de 1 día. Si el trabajo
     * ya existe, no lo reemplaza (política KEEP).
     *
     * ## Configuración
     * - Intervalo: 24 horas
     * - Hora de ejecución: Medianoche (00:00:00)
     * - Política: No reemplazar si ya existe
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
     * Cancela la verificación periódica de racha.
     */
    fun cancelStreakCheck() {
        WorkManager.getInstance(context).cancelUniqueWork(STREAK_WORK_NAME)
    }

    /**
     * Programa recordatorios diarios según la configuración del usuario.
     *
     * Crea un [ReminderWorker] para cada día seleccionado, programado
     * para la hora especificada.
     *
     * @param hour Hora del recordatorio (0-23).
     * @param minute Minuto del recordatorio (0-59).
     * @param selectedDays Set de días en español ("Lunes", "Martes", etc.).
     * @param motivationalMessages Si debe usar mensajes motivacionales.
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
     * Programa un recordatorio para un día específico de la semana.
     *
     * @param dayOfWeek Día de la semana según [Calendar] (ej: Calendar.MONDAY).
     * @param hour Hora del recordatorio (0-23).
     * @param minute Minuto del recordatorio (0-59).
     * @param motivationalMessages Si debe usar mensajes motivacionales.
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
     * Cancela todos los recordatorios programados.
     */
    fun cancelReminders() {
        WorkManager.getInstance(context).cancelAllWorkByTag(REMINDER_WORK_TAG)
    }

    /**
     * Convierte el nombre de un día en español a su valor de [Calendar].
     *
     * @param dayName Nombre del día en español.
     * @return Valor de Calendar.DAY_OF_WEEK o `null` si no es válido.
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
        /** Nombre único del trabajo de verificación de racha */
        private const val STREAK_WORK_NAME = "streak_check_work"

        /** Tag para identificar trabajos de racha */
        private const val STREAK_WORK_TAG = "streak_check"

        /** Nombre único del trabajo de recordatorio */
        private const val REMINDER_WORK_NAME = "reminder_work"

        /** Tag para identificar trabajos de recordatorio */
        private const val REMINDER_WORK_TAG = "reminder"
    }
}