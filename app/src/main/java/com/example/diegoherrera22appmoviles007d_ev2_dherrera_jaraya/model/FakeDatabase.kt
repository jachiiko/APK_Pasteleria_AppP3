package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.model

@Deprecated("Legacy – solo pruebas locales")
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

    fun obtenerPorEmail(email: String): Usuario? {
        return usuarios.firstOrNull { it.email.equals(email, ignoreCase = true) }
    }

    fun actualizarDireccion(email: String, direccion: String, comuna: String, region: String): Usuario? {
        val usuario = usuarios.firstOrNull { it.email.equals(email, ignoreCase = true) }
        if (usuario != null) {
            val index = usuarios.indexOf(usuario)
            usuarios[index] = Usuario(
                nombre = usuario.nombre,
                apellido = usuario.apellido,
                rut = usuario.rut,
                direccion = direccion,
                region = region,
                comuna = comuna,
                email = usuario.email,
                password = usuario.password
            )
            return usuarios[index]
        }
        return null
    }

    /**
     * Limpia los datos almacenados. Pensado para pruebas unitarias. //esto nos sirve por ahora, Esto se va a borrar una vez conectemos con la base de datos
     */
    fun clear() {
        usuarios.clear()
    }

}