package com.example.aprendejapones.di

import android.content.Context
import androidx.room.Room
import com.example.aprendejapones.data.local.database.AppDatabase
import com.example.aprendejapones.data.local.database.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration() // For development only
            .build()
    }

    @Provides
    @Singleton
    fun provideUsuariosLocalDao(database: AppDatabase): UsuariosLocalDao {
        return database.usuariosLocalDao()
    }

    @Provides
    @Singleton
    fun provideProgresoCategoriaDao(database: AppDatabase): ProgresoCategoriaDao {
        return database.progresoCategoriaDao()
    }

    @Provides
    @Singleton
    fun provideHistorialLeccionesDao(database: AppDatabase): HistorialLeccionesDao {
        return database.historialLeccionesDao()
    }

    @Provides
    @Singleton
    fun provideLogrosLocalDao(database: AppDatabase): LogrosLocalDao {
        return database.logrosLocalDao()
    }
}
