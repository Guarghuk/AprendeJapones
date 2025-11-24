package com.example.aprendejapones

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for Kotodama
 * Initializes Hilt dependency injection
 */
@HiltAndroidApp
class KotodamaApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Any app-wide initialization can go here
        // Hilt will handle DI initialization automatically
    }
}