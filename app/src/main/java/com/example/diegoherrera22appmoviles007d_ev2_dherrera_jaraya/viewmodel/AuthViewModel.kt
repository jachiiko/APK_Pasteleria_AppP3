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
        rut: String,
        direccion: String,
        region: String,
        comuna: String,
        email: String,
        password: String
    ): Boolean {
        if (!esRutValido(rut)) {
            mensaje.value = "RUT inválido"
            return false
        }

        val nuevo = Usuario(nombre, apellido, rut, direccion, region, comuna, email, password)
        return if (FakeDatabase.registrar(nuevo)) {
            mensaje.value = "Registro exitoso"
            true
        } else {
            mensaje.value = "El usuario ya existe"
            false
        }
    }

    private fun esRutValido(rutIngresado: String): Boolean {
        val rut = rutIngresado
            .replace(".", "")
            .replace("-", "")
            .uppercase()

        if (rut.length < 2) return false

        val cuerpo = rut.dropLast(1)
        val dv = rut.last()
        val cuerpoNumero = cuerpo.toIntOrNull() ?: return false

        var suma = 0
        var multiplicador = 2

        cuerpo.reversed().forEach { caracter ->
            val digito = caracter.digitToInt()
            suma += digito * multiplicador
            multiplicador = if (multiplicador == 7) 2 else multiplicador + 1
        }

        val resto = 11 - (suma % 11)
        val dvEsperado = when (resto) {
            11 -> '0'
            10 -> 'K'
            else -> resto.digitToChar()
        }

        return dv == dvEsperado && cuerpoNumero > 0
    }


    fun login(email: String, password: String): Boolean {
        return if (FakeDatabase.login(email, password)) {
            usuarioActual.value = email
            mensaje.value = "Inicio de sesión exitoso"
            true
        } else {
            mensaje.value = "Credenciales inválidas"
            false
        }
    }




}