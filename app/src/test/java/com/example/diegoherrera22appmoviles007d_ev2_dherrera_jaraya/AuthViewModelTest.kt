package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya

import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.model.FakeDatabase
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.viewmodel.AuthViewModel
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class AuthViewModelTest : StringSpec({

    lateinit var viewModel: AuthViewModel

    beforeTest {
        FakeDatabase.clear()
        viewModel = AuthViewModel()
    }

    "registrar usuario válido debe permitir login" {

        val email = "usuario@correo.com"
        val password = "seguro"

        val registro = viewModel.registrar(
            nombre = "Juan",
            apellido = "Pérez",
            rut = "12.345.678-5",
            direccion = "Calle Falsa 123",
            region = "Metropolitana",
            comuna = "Santiago",
            email = email,
            password = password
        )

        registro shouldBe true
        viewModel.mensaje.value shouldBe "Registro exitoso"

        val login = viewModel.login(email, password)

        login shouldBe true
        viewModel.usuarioActual.value shouldBe email
        viewModel.mensaje.value shouldBe "Inicio de sesión exitoso"
    }

    "registrar usuario con RUT inválido debe mostrar error" {

        val registro = viewModel.registrar(
            nombre = "Ana",
            apellido = "Torres",
            rut = "12.345.678-9",
            direccion = "Otra calle 456",
            region = "Valparaíso",
            comuna = "Viña del Mar",
            email = "ana@correo.com",
            password = "password"
        )

        registro shouldBe false
        viewModel.mensaje.value shouldBe "RUT inválido"
    }

    "registrar usuario duplicado debe mostrar mensaje de existencia" {

        val email = "duplicado@correo.com"

        viewModel.registrar(
            nombre = "Luis",
            apellido = "Ramírez",
            rut = "12.345.678-5",
            direccion = "Calle Uno",
            region = "Biobío",
            comuna = "Concepción",
            email = email,
            password = "clave1"
        ) shouldBe true

        val registro2 = viewModel.registrar(
            nombre = "Luis",
            apellido = "Ramírez",
            rut = "12.345.678-5",
            direccion = "Calle Uno",
            region = "Biobío",
            comuna = "Concepción",
            email = email,
            password = "clave2"
        )

        registro2 shouldBe false
        viewModel.mensaje.value shouldBe "El usuario ya existe"
    }
})
