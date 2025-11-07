package com.example.aprendejapones.data.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class UserDto(
    @SerializedName("id") val id: Int,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("nombre_completo") val fullName: String?,
    @SerializedName("rol") val role: String,
    @SerializedName("nivel_actual") val currentLevel: String?,
    @SerializedName("puntos_totales") val totalPoints: Int,
    @SerializedName("fecha_registro") val registeredAt: Date
)