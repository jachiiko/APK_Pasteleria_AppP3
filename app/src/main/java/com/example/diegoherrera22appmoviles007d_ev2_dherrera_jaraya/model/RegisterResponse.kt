package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.model

data class RegisterResponse(
    val id: String,
    val nombre: String,
    val apellido: String,
    val rut: String,
    val direccion: String,
    val region: Region,
    val comuna: String,
    val email: String,
    val role: String
)
