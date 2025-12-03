package com.example.aprendejapones.domain.model

/**
 * Clase sellada que representa el estado de una operación asíncrona.
 *
 * Este patrón se utiliza para manejar los diferentes estados de las operaciones
 * de red o base de datos, proporcionando una forma type-safe de representar
 * carga, éxito y error.
 *
 * ## Uso
 *
 * ```kotlin
 * when (response) {
 *     is Response.Loading -> showLoadingIndicator()
 *     is Response.Success -> displayData(response.data)
 *     is Response.Failure -> showError(response.e?.message)
 * }
 * ```
 *
 * @param T Tipo de dato que contendrá en caso de éxito.
 *
 * @see Loading Estado que indica que la operación está en progreso.
 * @see Success Estado que indica que la operación fue exitosa con datos.
 * @see Failure Estado que indica que la operación falló.
 *
 * @author Kotodama Team
 * @since 1.0.0
 */
sealed class Response<out T> {

    /**
     * Estado que indica que una operación está en progreso.
     *
     * Útil para mostrar indicadores de carga en la UI mientras
     * se espera el resultado de una operación asíncrona.
     */
    object Loading: Response<Nothing>()

    /**
     * Estado que indica que la operación fue exitosa.
     *
     * Contiene los datos resultantes de la operación.
     *
     * @property data Los datos obtenidos de la operación exitosa.
     */
    data class Success<out T>(val data: T): Response<T>()

    /**
     * Estado que indica que la operación falló.
     *
     * Contiene información sobre el error ocurrido.
     *
     * @property e La excepción que causó el fallo (puede ser null).
     */
    data class Failure<out T>(val e: Exception?): Response<T>()
}