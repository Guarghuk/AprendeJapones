package com.example.aprendejapones.data.repository

import com.benasher44.uuid.uuid4
import com.example.aprendejapones.data.local.database.dao.UsuariosLocalDao
import com.example.aprendejapones.data.local.database.entity.UsuariosLocalEntity
import com.example.aprendejapones.domain.model.User
import com.example.aprendejapones.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementación del repositorio de usuarios locales usando Room Database.
 *
 * Esta clase gestiona los datos del usuario almacenados localmente,
 * incluyendo el perfil, experiencia, niveles y moneda virtual.
 * Trabaja con [UsuariosLocalDao] para las operaciones de base de datos.
 *
 * ## Características
 * - Creación automática de usuario con UUID único
 * - Sistema de niveles con XP progresivo
 * - Gestión de racha de estudio
 * - Sistema de moneda virtual (drops)
 *
 * ## Sistema de Niveles
 * - Nivel 1: 100 XP para subir
 * - Cada nivel aumenta el XP requerido en 50
 * - Fórmula: `maxXP = 100 + (nivel - 1) * 50`
 *
 * @property usuariosLocalDao DAO para operaciones de base de datos.
 *
 * @see UserRepository Interfaz que implementa esta clase.
 * @see UsuariosLocalEntity Entidad de Room para usuarios.
 * @see User Modelo de dominio retornado.
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
@Singleton
class UserRepositoryImpl @Inject constructor(
    private val usuariosLocalDao: UsuariosLocalDao
) : UserRepository {

    /** Formato de fecha para mostrar "miembro desde" en español */
    private val dateFormat = SimpleDateFormat("MMMM yyyy", Locale("es", "ES"))

    companion object {
        // ============ Valores por Defecto para Nuevos Usuarios ============

        /** Nombre de usuario por defecto */
        private const val DEFAULT_USERNAME = "Usuario"

        /** Email por defecto (vacío para usuarios locales) */
        private const val DEFAULT_EMAIL = ""

        /** Nivel inicial */
        private const val DEFAULT_LEVEL = 1

        /** XP inicial */
        private const val DEFAULT_XP = 0

        /** XP máximo para el nivel 1 */
        private const val DEFAULT_MAX_XP = 100

        /** Racha inicial */
        private const val DEFAULT_STREAK = 0

        /** Monedas iniciales */
        private const val DEFAULT_COINS = 0

        /** Rango inicial en japonés (初心者 = Principiante) */
        private const val DEFAULT_RANK = "初心者"

        /** Letra de avatar por defecto */
        private const val DEFAULT_AVATAR = "K"

        /** Incremento de XP requerido por nivel */
        private const val XP_PER_LEVEL_INCREMENT = 50
    }

    /**
     * Obtiene el usuario actual como Flow reactivo.
     *
     * Se actualiza automáticamente cuando hay cambios en la base de datos.
     *
     * @return Flow que emite el [User] actual o `null`.
     */
    override fun getCurrentUserFlow(): Flow<User?> {
        return usuariosLocalDao.getCurrentUsuarioFlow().map { it?.toDomain() }
    }

    /**
     * Obtiene el usuario actual de forma única.
     *
     * @return El [User] actual o `null` si no existe.
     */
    override suspend fun getCurrentUser(): User? {
        return usuariosLocalDao.getCurrentUsuario()?.toDomain()
    }

    /**
     * Obtiene el usuario existente o crea uno nuevo.
     *
     * Si no existe usuario en la base de datos, crea uno con UUID único
     * y valores por defecto. Esta es la forma principal de inicializar
     * un nuevo usuario local.
     *
     * @return El [User] existente o recién creado.
     */
    override suspend fun getOrCreateUser(): User {
        val existing = usuariosLocalDao.getCurrentUsuario()

        return if (existing != null) {
            existing.toDomain()
        } else {
            // Create new local user with UUID
            val now = System.currentTimeMillis()
            val newUserEntity = UsuariosLocalEntity(
                idUsuario = uuid4().toString(),
                nombreUsuario = DEFAULT_USERNAME,
                email = DEFAULT_EMAIL,
                nivel = DEFAULT_LEVEL,
                xpActual = DEFAULT_XP,
                xpMaxNivel = DEFAULT_MAX_XP,
                rachaDias = DEFAULT_STREAK,
                monedas = DEFAULT_COINS,
                fechaRegistro = now,
                ultimaConexion = now
            )

            usuariosLocalDao.insertUsuario(newUserEntity)
            newUserEntity.toDomain()
        }
    }

    /**
     * Actualiza el perfil del usuario en la base de datos.
     *
     * @param user El [User] con los datos actualizados.
     */
    override suspend fun updateUser(user: User) {
        val entity = user.toEntity()
        usuariosLocalDao.updateUsuario(entity)
    }

    /**
     * Añade puntos de experiencia y gestiona subidas de nivel.
     *
     * Implementa la lógica de level-up: cuando los XP superan el máximo
     * del nivel actual, el usuario sube de nivel y el XP sobrante se
     * conserva para el nuevo nivel.
     *
     * @param xp Cantidad de XP a añadir.
     * @return El [User] actualizado con nuevo XP y posible nuevo nivel.
     * @throws IllegalStateException Si no se encuentra el usuario.
     */
    override suspend fun addXP(xp: Int): User {
        val user = getCurrentUser() ?: throw IllegalStateException("No user found")

        var newXP = user.currentXP + xp
        var newLevel = user.level
        var newMaxXP = user.maxXP

        // Level up logic
        while (newXP >= newMaxXP) {
            newXP -= newMaxXP
            newLevel++
            newMaxXP = calculateMaxXP(newLevel)
        }

        usuariosLocalDao.updateXP(user.id, newXP, newLevel)

        return user.copy(
            currentXP = newXP,
            level = newLevel,
            maxXP = newMaxXP
        )
    }

    /**
     * Actualiza la racha de días de estudio.
     *
     * @param streak Nueva cantidad de días de racha.
     */
    override suspend fun updateStreak(streak: Int) {
        val user = getCurrentUser() ?: return
        usuariosLocalDao.updateRacha(user.id, streak)
    }

    /**
     * Añade monedas (drops) al usuario.
     *
     * @param amount Cantidad de drops a añadir (debe ser positivo).
     */
    override suspend fun addDrops(amount: Int) {
        val user = getCurrentUser() ?: return
        usuariosLocalDao.addMonedas(user.id, amount)
    }

    /**
     * Gasta monedas (drops) del usuario.
     *
     * Verifica que el usuario tenga suficientes drops antes de gastar.
     *
     * @param amount Cantidad de drops a gastar.
     * @return `true` si el gasto fue exitoso, `false` si no hay suficientes.
     */
    override suspend fun spendDrops(amount: Int): Boolean {
        val user = getCurrentUser() ?: return false
        val rowsAffected = usuariosLocalDao.spendMonedas(user.id, amount)
        return rowsAffected > 0
    }

    /**
     * Calcula el XP máximo requerido para un nivel específico.
     *
     * Fórmula: `100 + (nivel - 1) * 50`
     * - Nivel 1: 100 XP
     * - Nivel 2: 150 XP
     * - Nivel 3: 200 XP
     * - etc.
     *
     * @param level Nivel para calcular.
     * @return XP máximo requerido para ese nivel.
     */
    private fun calculateMaxXP(level: Int): Int {
        return DEFAULT_MAX_XP + (level - 1) * XP_PER_LEVEL_INCREMENT
    }

    // ============ Mapper Functions ============

    /**
     * Convierte una entidad de Room a modelo de dominio.
     *
     * @receiver Entidad de usuario de Room.
     * @return Modelo [User] de dominio.
     */
    private fun UsuariosLocalEntity.toDomain(): User {
        return User(
            id = idUsuario,
            username = nombreUsuario,
            rank = DEFAULT_RANK, // Default rank, could be calculated from level
            level = nivel,
            currentXP = xpActual,
            maxXP = xpMaxNivel,
            streak = rachaDias,
            drops = monedas,
            memberSince = dateFormat.format(Date(fechaRegistro)),
            avatarLetter = nombreUsuario.firstOrNull()?.toString() ?: DEFAULT_AVATAR
        )
    }

    /**
     * Convierte un modelo de dominio a entidad de Room.
     *
     * @receiver Modelo de dominio [User].
     * @return Entidad [UsuariosLocalEntity] para Room.
     */
    private fun User.toEntity(): UsuariosLocalEntity {
        return UsuariosLocalEntity(
            idUsuario = id,
            nombreUsuario = username,
            email = "", // Email not stored in User domain model
            nivel = level,
            xpActual = currentXP,
            xpMaxNivel = maxXP,
            rachaDias = streak,
            monedas = drops,
            fechaRegistro = System.currentTimeMillis(), // Will be overwritten if existing
            ultimaConexion = System.currentTimeMillis()
        )
    }
}