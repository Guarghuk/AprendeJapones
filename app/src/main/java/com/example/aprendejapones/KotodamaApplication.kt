package com.example.aprendejapones

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.example.aprendejapones.workers.WorkManagerScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * Application class for Kotodama
 * Initializes Hilt dependency injection and WorkManager
 */
@HiltAndroidApp
class KotodamaApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var workManagerScheduler: WorkManagerScheduler

    override fun onCreate() {
        super.onCreate()

        // Programar verificación diaria de racha
        workManagerScheduler.scheduleStreakCheck()
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}