package com.example.aprendejapones.domain.usecase

import com.example.aprendejapones.domain.model.User
import com.example.aprendejapones.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Ejemplo de caso de uso: login (local). Aquí solo ilustrativo:
 * - Buscar por username
 * - comparar hash (la comparación del hash en producción la deberías hacer con una librería segura)
 */
class LoginUseCase(
    private val repository: UserRepository,
    private val passwordHasher: (String) -> String // función para hashear
) {
    suspend operator fun invoke(username: String, password: String): User? = withContext(Dispatchers.IO) {
        val user = repository.getUserByUsername(username) ?: return@withContext null
        // En este ejemplo no tenemos el passwordHash en domain, por simplicidad
        // Si tu domain incluye passwordHash haz la comparación aquí.
        // Ejemplo (pseudocódigo):
        // if (passwordHasher(password) == user.passwordHash) return user else null
        user
    }
}