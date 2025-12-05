package com.example.aprendejapones.domain.repository

import com.example.aprendejapones.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio para la gestión de usuarios locales.
 *
 * Esta interfaz define el contrato para las operaciones relacionadas con
 * el usuario local almacenado en Room Database. Gestiona el perfil del
 * usuario, progreso, experiencia y moneda virtual.
 *
 * ## Diferencia con [FirestoreUserRepository]
 * - Este repositorio maneja datos locales (Room Database)
 * - [FirestoreUserRepository] maneja datos en la nube (Firestore)
 *
 * ## Responsabilidades
 * - Crear y obtener el usuario local
 * - Gestionar puntos de experiencia (XP) y niveles
 * - Gestionar la racha de estudio
 * - Gestionar la moneda virtual (drops)
 *
 * @see User Modelo de dominio del usuario.
 * @see FirestoreUserRepository Para operaciones en Firestore.
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
interface UserRepository {

    /**
     * Obtiene el usuario actual como un Flow reactivo.
     *
     * Permite observar cambios en tiempo real en los datos del usuario,
     * actualizándose automáticamente cuando hay modificaciones en la
     * base de datos local.
     *
     * @return Flow que emite el [User] actual o `null` si no existe.
     */
    fun getCurrentUserFlow(): Flow<User?>

    /**
     * Obtiene el usuario actual de forma única (no reactiva).
     *
     * Útil cuando solo se necesita el valor actual sin observar cambios.
     *
     * @return El [User] actual o `null` si no existe.
     */
    suspend fun getCurrentUser(): User?

    /**
     * Obtiene el usuario existente o crea uno nuevo.
     *
     * Si no existe un usuario en la base de datos local, crea uno
     * nuevo con valores por defecto y un UUID único.
     *
     * @return El [User] existente o el recién creado.
     */
    suspend fun getOrCreateUser(): User

    /**
     * Actualiza el perfil del usuario.
     *
     * Persiste los cambios del modelo [User] en la base de datos local.
     *
     * @param user El [User] con los datos actualizados.
     */
    suspend fun updateUser(user: User)

    /**
     * Añade puntos de experiencia al usuario y gestiona subidas de nivel.
     *
     * Cuando los XP superan el máximo del nivel actual, el usuario
     * sube de nivel automáticamente. Los XP sobrantes se conservan
     * para el nuevo nivel.
     *
     * @param xp Cantidad de puntos de experiencia a añadir.
     * @return El [User] actualizado con el nuevo XP y posible nuevo nivel.
     * @throws IllegalStateException Si no se encuentra el usuario.
     */
    suspend fun addXP(xp: Int): User

    /**
     * Actualiza la racha de días de estudio del usuario.
     *
     * La racha representa días consecutivos de estudio.
     * Un valor de 0 indica que la racha se ha roto.
     *
     * @param streak Nueva cantidad de días de racha.
     */
    suspend fun updateStreak(streak: Int)

    /**
     * Añade monedas (drops) al usuario.
     *
     * Los drops son la moneda virtual que se puede ganar
     * completando lecciones y desafíos.
     *
     * @param amount Cantidad de drops a añadir (debe ser positivo).
     */
    suspend fun addDrops(amount: Int)

    /**
     * Gasta monedas (drops) del usuario.
     *
     * Verifica que el usuario tenga suficientes drops antes de gastar.
     *
     * @param amount Cantidad de drops a gastar.
     * @return `true` si el gasto fue exitoso, `false` si no hay suficientes drops.
     */
    suspend fun spendDrops(amount: Int): Boolean
}
