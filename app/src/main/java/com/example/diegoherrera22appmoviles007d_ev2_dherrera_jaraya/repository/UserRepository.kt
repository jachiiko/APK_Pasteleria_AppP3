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
     * Actualiza dirección del usuario autenticado.
     * 1) Trae el usuario actual (incluye id)
     * 2) Hace PUT /users con el payload actualizado
     */
    suspend fun updateMyAddress(
        direccion: String,
        comuna: String,
        region: Region
    ): User? {
        val me = getMe() ?: return null

        val payload = me.copy(
            direccion = direccion,
            comuna = comuna,
            region = region,
            password = null // importante: tu API re-encodea password si viene
        )

        return try {
            userApi.updateUser(payload)
        } catch (_: Exception) {
            null
        }
    }

    // Opcional: deja esto solo si de verdad lo necesitas (admin/backoffice).
    suspend fun getAllUsers(): List<User> = userApi.getAllUsers()
}
