package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.model

object FakeDatabase {
    private val usuarios = mutableListOf<Usuario>()

    fun registrar(usuario: Usuario): Boolean {
        if (usuarios.any { it.email == usuario.email }) return false
        usuarios.add(usuario)
        return true
    }

    fun login(email: String, password: String): Boolean {
        return usuarios.any { it.email == email && it.password == password }
    }
}