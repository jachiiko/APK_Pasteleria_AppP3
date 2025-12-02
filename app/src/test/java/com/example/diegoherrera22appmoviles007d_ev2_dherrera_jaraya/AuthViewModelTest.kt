package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya

import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.model.FakeDatabase
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.viewmodel.AuthViewModel
import org.junit.jupiter.api.*

class AuthViewModelTest {

    private lateinit var viewModel: AuthViewModel

    @BeforeEach
    fun setUp() {
        FakeDatabase.clear()
        viewModel = AuthViewModel()
    }

    @Test
    fun registrarUsuarioValido_actualizaMensajeYPermiteLogin() {
        val email = "usuario@correo.com"
        val password = "seguro"

        val registroExitoso = viewModel.registrar(
            nombre = "Juan",
            apellido = "Pérez",
            rut = "12.345.678-5",
            direccion = "Calle Falsa 123",
            region = "Metropolitana",
            comuna = "Santiago",
            email = email,
            password = password
        )

        Assertions.assertTrue(registroExitoso)
        Assertions.assertEquals("Registro exitoso", viewModel.mensaje.value)

        val loginExitoso = viewModel.login(email, password)

        Assertions.assertTrue(loginExitoso)
        Assertions.assertEquals(email, viewModel.usuarioActual.value)
        Assertions.assertEquals("Inicio de sesión exitoso", viewModel.mensaje.value)
    }

    @Test
    fun registrarUsuarioConRutInvalido_actualizaMensajeError() {
        val registroExitoso = viewModel.registrar(
            nombre = "Ana",
            apellido = "Torres",
            rut = "12.345.678-9",
            direccion = "Otra calle 456",
            region = "Valparaíso",
            comuna = "Viña del Mar",
            email = "ana@correo.com",
            password = "password"
        )

        Assertions.assertFalse(registroExitoso)
        Assertions.assertEquals("RUT inválido", viewModel.mensaje.value)
    }

    @Test
    fun registrarUsuarioDuplicado_devuelveMensajeDeExistencia() {
        val email = "duplicado@correo.com"

        val primerRegistro = viewModel.registrar(
            nombre = "Luis",
            apellido = "Ramírez",
            rut = "12.345.678-5",
            direccion = "Calle Uno",
            region = "Biobío",
            comuna = "Concepción",
            email = email,
            password = "clave1"
        )

        Assertions.assertTrue(primerRegistro)

        val segundoRegistro = viewModel.registrar(
            nombre = "Luis",
            apellido = "Ramírez",
            rut = "12.345.678-5",
            direccion = "Calle Uno",
            region = "Biobío",
            comuna = "Concepción",
            email = email,
            password = "clave2"
        )

        Assertions.assertFalse(segundoRegistro)
        Assertions.assertEquals("El usuario ya existe", viewModel.mensaje.value)
    }
}
