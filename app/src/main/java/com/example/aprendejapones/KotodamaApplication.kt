package com.example.aprendejapones

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.example.aprendejapones.workers.WorkManagerScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * Clase Application principal para la aplicación Kotodama.
 *
 * Esta clase inicializa los componentes fundamentales de la aplicación:
 * - **Hilt:** Inyección de dependencias mediante `@HiltAndroidApp`
 * - **WorkManager:** Configuración para workers con soporte de Hilt
 * - **Verificación de Racha:** Programación de verificación diaria
 *
 * ## Inicialización de WorkManager
 * Implementa [Configuration.Provider] para proporcionar una configuración
 * personalizada de WorkManager que usa [HiltWorkerFactory] para permitir
 * la inyección de dependencias en Workers.
 *
 * ## Lifecycle
 * - [onCreate]: Programa la verificación diaria de racha
 * - [workManagerConfiguration]: Configura WorkManager con HiltWorkerFactory
 *
 * ## Uso
 *
 * Esta clase debe declararse en el AndroidManifest.xml:
 *
 * ```xml
 * <application
 *     android:name=".KotodamaApplication"
 *     ...>
 * ```
 *
 * @property workerFactory Factory para crear Workers con inyección de dependencias.
 * @property workManagerScheduler Scheduler para programar verificación de racha.
 *
 * @see WorkManagerScheduler Para programación de workers.
 * @see HiltWorkerFactory Para inyección en workers.
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
@HiltAndroidApp
class KotodamaApplication : Application(), Configuration.Provider {

    /** Factory de Hilt para crear workers con inyección de dependencias */
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    /** Scheduler para programar verificación de racha y recordatorios */
    @Inject
    lateinit var workManagerScheduler: WorkManagerScheduler

    /**
     * Inicializa la aplicación.
     *
     * Programa la verificación diaria de racha que se ejecuta a medianoche
     * para detectar si el usuario perdió su racha por no estudiar.
     */
    override fun onCreate() {
        super.onCreate()

        // Programar verificación diaria de racha
        workManagerScheduler.scheduleStreakCheck()
    }

    /**
     * Proporciona la configuración de WorkManager.
     *
     * Configura WorkManager para usar [HiltWorkerFactory], permitiendo
     * que los workers reciban dependencias mediante inyección de Hilt.
     *
     * @return Configuración de WorkManager con HiltWorkerFactory.
     */
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}