package com.example.aprendejapones.domain.repository

import com.example.aprendejapones.domain.model.FirestoreUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    // Estado de autenticación
    val currentUser: Flow<FirestoreUser?>

    // Login
    suspend fun loginWithEmail(email: String, password: String): Result<FirestoreUser>
    suspend fun loginWithGoogle(idToken: String): Result<FirestoreUser>

    // Registro
    suspend fun registerWithEmail(
        email: String,
        password: String,
        username: String
    ): Result<FirestoreUser>

    // Sesión
    suspend fun logout()
    suspend fun isUserLoggedIn(): Boolean
    suspend fun getCurrentUserId(): String?
}