package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.model.FakeDatabase
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.model.Usuario

class AuthViewModel : ViewModel() {

    var mensaje = mutableStateOf("")
    var usuarioActual = mutableStateOf<String?>(null)

    fun registrar(
        nombre: String,
        apellido: String,
        rut: Int,
        direccion: String,
        region: String,
        comuna: String,
        email: String,
        password: String
    ) {
        val nuevo = Usuario(nombre, apellido, rut, direccion, region, comuna, email, password)
        if (FakeDatabase.registrar(nuevo)) {
            mensaje.value = "Registro exitoso ✅"
        } else {
            mensaje.value = "El usuario ya existe ❌"
        }
    }


    fun login(email: String, password: String): Boolean {
        return if (FakeDatabase.login(email, password)) {
            usuarioActual.value = email
            mensaje.value = "Inicio de sesión exitoso 🎉"
            true
        } else {
            mensaje.value = "Credenciales inválidas ❌"
            false
        }
    }




}