package com.example.aprendejapones.presentation.screens.sync

/**
 * Events for the Sync screen
 */
sealed class SyncEvent {
    object StartSync : SyncEvent()
    object CancelSync : SyncEvent()
    object DismissError : SyncEvent()
}
