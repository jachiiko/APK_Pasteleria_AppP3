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

    /**
     * Limpia los datos almacenados. Pensado para pruebas unitarias. //esto nos sirve por ahora, Esto se va a borrar una vez conectemos con la base de datos
     */
    fun clear() {
        usuarios.clear()
    }

}