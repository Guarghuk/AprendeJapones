package com.example.aprendejapones.presentation.screens.sync

/**
 * State for the Sync screen
 */
data class SyncState(
    val isSyncing: Boolean = false,
    val syncProgress: Float = 0f,
    val localDataCount: Int = 0,
    val cloudDataCount: Int = 0,
    val lastSyncTime: String? = null,
    val error: String? = null
)
