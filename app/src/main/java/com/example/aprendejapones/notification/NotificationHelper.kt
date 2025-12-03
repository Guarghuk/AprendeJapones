package com.example.aprendejapones.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.aprendejapones.MainActivity
import com.example.aprendejapones.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Helper para crear y mostrar notificaciones de la aplicación.
 *
 * Esta clase centraliza toda la lógica de notificaciones, incluyendo
 * la creación de canales de notificación (requerido para Android 8.0+),
 * verificación de permisos y construcción de notificaciones.
 *
 * ## Canales de Notificación
 * - **kotodama_reminders:** Recordatorios de estudio (prioridad normal)
 * - **kotodama_streak:** Alertas de racha en peligro (prioridad alta)
 *
 * ## Tipos de Notificaciones
 * - **Recordatorio diario:** Mensaje estándar o motivacional
 * - **Alerta de racha:** Cuando la racha está en peligro de perderse
 *
 * ## Permisos
 * Requiere `POST_NOTIFICATIONS` en Android 13+ (TIRAMISU).
 *
 * ## Uso
 *
 * ```kotlin
 * // En un Worker o Service
 * notificationHelper.showDailyReminder(motivationalMessage = true)
 *
 * // Alerta de racha
 * notificationHelper.showStreakWarning(currentStreak = 7)
 * ```
 *
 * @property context Contexto de aplicación para operaciones de notificación.
 *
 * @see ReminderWorker Usa este helper para enviar recordatorios.
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        // ============ Canal de Recordatorios ============

        /** ID del canal de recordatorios de estudio */
        private const val CHANNEL_ID = "kotodama_reminders"

        /** Nombre visible del canal de recordatorios */
        private const val CHANNEL_NAME = "Recordatorios de Estudio"

        /** Descripción del canal de recordatorios */
        private const val CHANNEL_DESCRIPTION = "Notificaciones para recordarte estudiar japonés"

        // ============ Canal de Racha ============

        /** ID del canal de alertas de racha */
        private const val STREAK_CHANNEL_ID = "kotodama_streak"

        /** Nombre visible del canal de racha */
        private const val STREAK_CHANNEL_NAME = "Alertas de Racha"

        // ============ IDs de Notificación ============

        /** ID para notificaciones de recordatorio */
        private const val NOTIFICATION_ID_REMINDER = 1001

        /** ID para notificaciones de racha */
        private const val NOTIFICATION_ID_STREAK = 1002
    }

    init {
        createNotificationChannels()
    }

    /**
     * Crea los canales de notificación necesarios.
     *
     * Requerido para Android 8.0 (Oreo) y superior. Define las
     * características de las notificaciones como importancia y vibración.
     */
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Canal de recordatorios
            val reminderChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = CHANNEL_DESCRIPTION
                enableVibration(true)
            }

            // Canal de alertas de racha
            val streakChannel = NotificationChannel(
                STREAK_CHANNEL_ID,
                STREAK_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Alertas cuando tu racha está en peligro"
                enableVibration(true)
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(reminderChannel)
            notificationManager.createNotificationChannel(streakChannel)
        }
    }

    /**
     * Muestra una notificación de recordatorio diario.
     *
     * Al tocar la notificación, abre la aplicación en la pantalla principal.
     *
     * @param motivationalMessage Si es `true`, usa un mensaje motivacional
     *        aleatorio en lugar del mensaje estándar.
     */
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showDailyReminder(motivationalMessage: Boolean = false) {
        if (!hasNotificationPermission()) {
            return
        }

        val title = "¡Es hora de practicar! 🌸"
        val message = if (motivationalMessage) {
            getMotivationalMessage()
        } else {
            "No olvides tu lección de japonés de hoy"
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Cambiar por tu icono
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_REMINDER, notification)
    }

    /**
     * Muestra una notificación de alerta de racha en peligro.
     *
     * Usa prioridad alta para asegurar que el usuario la vea.
     * Incluye el número de días de la racha actual.
     *
     * @param currentStreak Número de días de la racha actual.
     */
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showStreakWarning(currentStreak: Int) {
        if (!hasNotificationPermission()) {
            return
        }

        val title = "⚠️ ¡Tu racha está en peligro!"
        val message = "Tienes una racha de $currentStreak días. ¡No la pierdas! Estudia hoy para mantenerla 🔥"

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, STREAK_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_STREAK, notification)
    }

    /**
     * Verifica si la aplicación tiene permiso para enviar notificaciones.
     *
     * En Android 13+ se requiere el permiso `POST_NOTIFICATIONS`.
     * En versiones anteriores, siempre retorna `true`.
     *
     * @return `true` si tiene permiso, `false` en caso contrario.
     */
    private fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    /**
     * Obtiene un mensaje motivacional aleatorio.
     *
     * Usado cuando el usuario activa mensajes motivacionales
     * en la configuración de recordatorios.
     *
     * @return Mensaje motivacional aleatorio en español.
     */
    private fun getMotivationalMessage(): String {
        val messages = listOf(
            "¡Cada día estás más cerca de tu meta! 頑張って！",
            "La constancia es la clave del éxito 🌟",
            "Un poco cada día hace la diferencia 📚",
            "¡Tu futuro yo te agradecerá! 💪",
            "El mejor momento para estudiar es ahora ⏰",
            "¡Vamos! Tu racha te está esperando 🔥",
            "Pequeños pasos, grandes logros 🎯",
            "¡Hoy es un buen día para aprender algo nuevo! 🌸"
        )
        return messages.random()
    }
}