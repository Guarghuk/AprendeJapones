package com.example.aprendejapones.domain.usecase

import com.example.aprendejapones.domain.model.User
import com.example.aprendejapones.domain.repository.UserRepository

/**
 * Caso de uso sencillo: obtener un usuario por id.
 * Los usecases orquestan llamadas a repositorios y aplican reglas de negocio.
 */
class GetUserByIdUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(id: Int): User? {
        return repository.getUserById(id)
    }
}