package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.ui.theme.pastelButtonColors
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.ui.theme.pastelOutlinedTextFieldColors
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.ui.theme.pastelTextButtonColors
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.viewmodel.AuthViewModel
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.viewmodel.RegionViewModel

private fun isAllowedEmail(email: String): Boolean {
    val e = email.trim().lowercase()
    return e.endsWith("@duoc.cl") || e.endsWith("@admin.cl")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, viewModel: AuthViewModel, regionViewModel: RegionViewModel = viewModel()) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var rutText by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var region by remember { mutableStateOf("") }
    var comuna by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf<String?>(null) }

    val regiones = remember { regionViewModel.regiones }
    val comunas = remember(region) { regionViewModel.comunasDe(region) }

    var regionsExpanded by remember { mutableStateOf(false) }
    var comunasExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("Registro", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
            colors = pastelOutlinedTextFieldColors()
        )

        OutlinedTextField(
            value = apellido,
            onValueChange = { apellido = it },
            label = { Text("Apellido") },
            modifier = Modifier.fillMaxWidth(),
            colors = pastelOutlinedTextFieldColors()
        )

        OutlinedTextField(
            value = rutText,
            onValueChange = { rutText = it.filter(Char::isDigit) },
            label = { Text("RUT (solo números, sin DV)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            colors = pastelOutlinedTextFieldColors()
        )

        OutlinedTextField(
            value = direccion,
            onValueChange = { direccion = it },
            label = { Text("Dirección") },
            modifier = Modifier.fillMaxWidth(),
            colors = pastelOutlinedTextFieldColors()
        )

        Box {
            OutlinedTextField(
                value = region,
                onValueChange = {},
                readOnly = true,
                label = { Text("Región") },
                trailingIcon = { Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 2.dp)
                    .clickable { regionsExpanded = !regionsExpanded },
                colors = pastelOutlinedTextFieldColors(),
                enabled = regiones.isNotEmpty()
            )
            DropdownMenu(
                expanded = regionsExpanded,
                onDismissRequest = { regionsExpanded = false }
            ) {
                regiones.forEach { r ->
                    DropdownMenuItem(
                        text = { Text(r) },
                        onClick = {
                            region = r
                            comuna = ""
                            regionsExpanded = false
                        }
                    )
                }
            }
        }

        Box {
            OutlinedTextField(
                value = comuna,
                onValueChange = {},
                readOnly = true,
                label = { Text("Comuna") },
                trailingIcon = { Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "") },
                supportingText = {
                    if (region.isBlank()) Text("Selecciona primero una región")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 2.dp)
                    .clickable(enabled = region.isNotBlank()) { comunasExpanded = !comunasExpanded },
                colors = pastelOutlinedTextFieldColors(),
                enabled = region.isNotBlank()
            )
            DropdownMenu(
                expanded = comunasExpanded,
                onDismissRequest = { comunasExpanded = false }
            ) {
                comunas.forEach { c ->
                    DropdownMenuItem(
                        text = { Text(c) },
                        onClick = {
                            comuna = c
                            comunasExpanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = null
            },
            isError = emailError != null,
            label = { Text("Email") },
            supportingText = {
                if (emailError != null) Text(emailError!!)
                else Text("Solo @duoc.cl o @admin.cl")
            },
            modifier = Modifier.fillMaxWidth(),
            colors = pastelOutlinedTextFieldColors()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            colors = pastelOutlinedTextFieldColors()
        )

        Spacer(modifier = Modifier.height(6.dp))

        Button(
            onClick = {
                if (!isAllowedEmail(email)) {
                    emailError = "Solo se permiten correos @duoc.cl o @admin.cl"
                    return@Button
                }
                val rut = rutText.toIntOrNull() ?: 0
                viewModel.registrar(
                    nombre = nombre,
                    apellido = apellido,
                    rut = rut,
                    direccion = direccion,
                    region = region,
                    comuna = comuna,
                    email = email,
                    password = password
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = pastelButtonColors()
        ) {
            Text("Registrar")
        }

        Text(viewModel.mensaje.value, modifier = Modifier.padding(top = 10.dp))

        TextButton(
            onClick = { navController.navigate("login") },
            colors = pastelTextButtonColors()
        ) {
            Text("¿Ya tienes cuenta? Inicia sesión")
        }
    }
}