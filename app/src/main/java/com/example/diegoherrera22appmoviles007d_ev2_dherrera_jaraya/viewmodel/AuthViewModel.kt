package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.dto.LoginRequest
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.model.Region
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.dto.RegisterRequest
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.repository.ApiClient
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.repository.api.AuthApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class AuthViewModel(
    private val authApi: AuthApi = ApiClient.authApi
) : ViewModel() {

    var mensaje = mutableStateOf("")
    var usuarioActual = mutableStateOf<String?>(null)
    var token = mutableStateOf<String?>(null)

    fun registrar(
        nombre: String,
        apellido: String,
        rut: String,
        direccion: String,
        region: Region,
        comuna: String,
        email: String,
        password: String
    ): Boolean {
        if (!esRutValido(rut)) {
            mensaje.value = "RUT inválido"
            return false
        }

        return runBlocking {
            try {
                val request = RegisterRequest(
                    nombre = nombre,
                    apellido = apellido,
                    rut = rut,
                    direccion = direccion,
                    region = region,
                    comuna = comuna,
                    email = email,
                    password = password
                )

                withContext(Dispatchers.IO) {
                    authApi.register(request)
                }

                mensaje.value = "Registro exitoso"
                true
            } catch (e: HttpException) {
                mensaje.value = when (e.code()) {
                    400 -> "Datos inválidos o incompletos"
                    404 -> "Región o comuna inválida"
                    409 -> "El email ya se encuentra registrado"
                    else -> "Error del servidor (${e.code()})"
                }
                false
            } catch (e: Exception) {
                mensaje.value = "Error de conexión. Intenta nuevamente"
                false
            }
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
        return runBlocking {
            try {
                val response = withContext(Dispatchers.IO) {
                    authApi.login(LoginRequest(email, password))
                }
                token.value = response.token
                usuarioActual.value = email
                mensaje.value = "Inicio de sesión exitoso"
                true
            } catch (e: HttpException) {
                mensaje.value = when (e.code()) {
                    400 -> "Datos faltantes"
                    401 -> "Credenciales inválidas"
                    else -> "Error del servidor (${e.code()})"
                }
                false
            } catch (e: Exception) {
                mensaje.value = "Error de conexión. Intenta nuevamente"
                false
            }
        }
    }
}