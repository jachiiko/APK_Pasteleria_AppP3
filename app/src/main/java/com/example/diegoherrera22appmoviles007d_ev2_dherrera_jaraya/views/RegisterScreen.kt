package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.DropdownMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
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
            .padding(horizontal = 24.dp, vertical = 96.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            "Registro",
            style = MaterialTheme.typography.titleLarge.copy(
                fontFamily = FontFamily.Cursive
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp)
        )

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

        ExposedDropdownMenuBox(
            expanded = regionsExpanded,
            onExpandedChange = { regionsExpanded = it && regiones.isNotEmpty() }
        ) {
            OutlinedTextField(
                value = region,
                onValueChange = {},
                readOnly = true,
                label = { Text("Región") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = regionsExpanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                colors = pastelOutlinedTextFieldColors(),
                enabled = regiones.isNotEmpty()
            )
            ExposedDropdownMenu(
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

        ExposedDropdownMenuBox(
            expanded = comunasExpanded,
            onExpandedChange = { expanded ->
                if (region.isNotBlank()) comunasExpanded = expanded
            }
        ) {
            OutlinedTextField(
                value = comuna,
                onValueChange = {},
                readOnly = true,
                label = { Text("Comuna") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = comunasExpanded)
                },
                supportingText = {
                    if (region.isBlank()) Text("Selecciona primero una región")
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                colors = pastelOutlinedTextFieldColors(),
                enabled = region.isNotBlank()
            )
            ExposedDropdownMenu(
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

        Button(
            onClick = {
                if (!isAllowedEmail(email)) {
                    emailError = "Solo se permiten correos @duoc.cl o @admin.cl"
                    return@Button
                }
                val rut = rutText.toIntOrNull() ?: 0
                val registrado = viewModel.registrar(
                    nombre = nombre,
                    apellido = apellido,
                    rut = rut,
                    direccion = direccion,
                    region = region,
                    comuna = comuna,
                    email = email,
                    password = password
                )
                if (registrado) {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = pastelButtonColors()
        ) {
            Text("Registrar")
        }

        Text(viewModel.mensaje.value, modifier = Modifier.padding(top = 8.dp))

        TextButton(
            onClick = {
                navController.navigate("login") {
                    popUpTo("login") { inclusive = true }
                }
            },
            colors = pastelTextButtonColors(),
            modifier = Modifier
                .padding(top = 8.dp, bottom = 24.dp)
                .fillMaxWidth()
        ) {
            Text(
                "¿Ya tienes cuenta? Accede",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary)
            )
        }
    }
}
