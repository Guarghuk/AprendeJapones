package com.example.aprendejapones.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Utility functions for time formatting
 */
object TimeUtils {
    /**
     * Format timestamp to a human-readable relative time string
     */
    fun formatRelativeTime(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp
        
        return when {
            diff < 60000 -> "Hace un momento"
            diff < 3600000 -> "Hace ${diff / 60000} min"
            diff < 86400000 -> "Hace ${diff / 3600000}h"
            else -> {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                dateFormat.format(Date(timestamp))
            }
        }
    }
}
