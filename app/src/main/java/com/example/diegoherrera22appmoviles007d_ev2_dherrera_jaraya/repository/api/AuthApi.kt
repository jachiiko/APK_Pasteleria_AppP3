package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.repository.api

import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.model.LoginRequest
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.model.LoginResponse
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.model.RegisterRequest
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.model.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}