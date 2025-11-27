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
 * Helper para crear y mostrar notificaciones
 */
@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        private const val CHANNEL_ID = "kotodama_reminders"
        private const val CHANNEL_NAME = "Recordatorios de Estudio"
        private const val CHANNEL_DESCRIPTION = "Notificaciones para recordarte estudiar japonés"

        private const val STREAK_CHANNEL_ID = "kotodama_streak"
        private const val STREAK_CHANNEL_NAME = "Alertas de Racha"

        private const val NOTIFICATION_ID_REMINDER = 1001
        private const val NOTIFICATION_ID_STREAK = 1002
    }

    init {
        createNotificationChannels()
    }

    /**
     * Crea los canales de notificación necesarios (Android 8.0+)
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
     * Muestra notificación de recordatorio diario
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
     * Muestra notificación de alerta de racha
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
     * Verifica si tenemos permiso de notificaciones
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
     * Obtiene un mensaje motivacional aleatorio
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