package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.repository.api

import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.dto.LoginRequest
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.dto.LoginResponse
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.dto.RegisterRequest
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.dto.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}