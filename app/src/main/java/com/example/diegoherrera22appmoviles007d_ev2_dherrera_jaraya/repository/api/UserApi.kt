package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.repository.api

import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.model.User
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApi {

    @GET("users/me")
    suspend fun getMe(): User

    @PUT("users")
    suspend fun updateUser(@Body user: User): User

    // (Opcional) deja estos solo si los usará el admin/backoffice
    @GET("users")
    suspend fun getAllUsers(): List<User>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: String): User

    @POST("users")
    suspend fun createUser(@Body user: User): User

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: String): String
}
