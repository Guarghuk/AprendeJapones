package com.example.aprendejapones.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de Hilt para la configuración de servicios de Firebase.
 *
 * Este módulo proporciona las instancias singleton de los servicios
 * de Firebase utilizados en la aplicación: Authentication, Firestore
 * y Storage.
 *
 * ## Servicios Proporcionados
 * - [FirebaseAuth]: Autenticación de usuarios
 * - [FirebaseFirestore]: Base de datos NoSQL en tiempo real
 * - [FirebaseStorage]: Almacenamiento de archivos
 *
 * ## Configuración de Firestore
 * - Persistencia offline habilitada
 * - Caché ilimitada para mejor experiencia offline
 *
 * ## Uso
 *
 * Los servicios se inyectan automáticamente en los repositorios:
 *
 * ```kotlin
 * class AuthRepositoryImpl @Inject constructor(
 *     private val firebaseAuth: FirebaseAuth,
 *     private val firestore: FirebaseFirestore
 * ) : AuthRepository
 * ```
 *
 * @see com.example.aprendejapones.data.firebase.FirebaseManager Manager centralizado de Firebase.
 * @see RepositoryModule Módulo que usa estos servicios.
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    /**
     * Proporciona la instancia singleton de Firebase Authentication.
     *
     * @return Instancia de [FirebaseAuth] para autenticación de usuarios.
     */
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    /**
     * Proporciona la instancia singleton de Firebase Firestore.
     *
     * Configura Firestore con:
     * - Persistencia offline habilitada para uso sin conexión
     * - Caché ilimitada para almacenar más datos localmente
     *
     * @return Instancia configurada de [FirebaseFirestore].
     */
    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return Firebase.firestore.apply {
            // Configurar para persistencia offline
            firestoreSettings = firestoreSettings {
                isPersistenceEnabled = true
                cacheSizeBytes = FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED
            }
        }
    }

    /**
     * Proporciona la instancia singleton de Firebase Storage.
     *
     * Usado para almacenar fotos de perfil e imágenes de publicaciones.
     *
     * @return Instancia de [FirebaseStorage] para almacenamiento de archivos.
     */
    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage {
        return Firebase.storage
    }
}