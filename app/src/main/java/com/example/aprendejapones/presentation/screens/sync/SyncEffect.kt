package com.example.aprendejapones.presentation.screens.sync

/**
 * One-time effects for the Sync screen
 */
sealed class SyncEffect {
    data class ShowToast(val message: String) : SyncEffect()
    object SyncCompleted : SyncEffect()
    data class SyncFailed(val error: String) : SyncEffect()
}
