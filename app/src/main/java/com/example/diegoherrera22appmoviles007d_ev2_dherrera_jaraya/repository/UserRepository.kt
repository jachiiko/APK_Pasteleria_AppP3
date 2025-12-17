package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.repository

import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.model.Region
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.model.User
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.repository.api.UserApi

class UserRepository(
    private val userApi: UserApi = ApiClient.userApi
) {

    /**
     * Usuario autenticado según el JWT (GET /users/me)
     */
    suspend fun getMe(): User? {
        return try {
            userApi.getMe()
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Actualiza el perfil del usuario autenticado.
     * 1) Trae el usuario actual (incluye id)
     * 2) Hace PUT /users con el payload actualizado
     */
    suspend fun updateMyProfile(
        nombre: String,
        apellido: String,
        rut: String,
        direccion: String,
        comuna: String,
        region: Region
    ): Result<User> {
        val me = getMe() ?: return Result.failure(IllegalStateException("Usuario no disponible"))

        val payload = me.copy(
            nombre = nombre,
            apellido = apellido,
            rut = rut,
            direccion = direccion,
            comuna = comuna,
            region = region,
            password = null // importante: tu API re-encodea password si viene
        )

        return try {
            Result.success(userApi.updateUser(payload))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Opcional: deja esto solo si de verdad lo necesitas (admin/backoffice).
    suspend fun getAllUsers(): List<User> = userApi.getAllUsers()
}