package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views

import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

// Dropdown menus (Material 2)
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalTextToolbar
import androidx.compose.ui.platform.TextToolbar
import androidx.compose.ui.platform.TextToolbarStatus
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
fun RegisterScreen(
    navController: NavController,
    viewModel: AuthViewModel,
    regionViewModel: RegionViewModel // ← ViewModel REAL (NO CREAR OTRO)
) {

    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var rutText by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var region by remember { mutableStateOf("") }
    var comuna by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf<String?>(null) }

    // Regiones y comunas desde el ViewModel correcto
    val regionesState by regionViewModel.regiones.collectAsState()
    val regiones = regionesState.map { it.nombre }
    val comunas = regionViewModel.comunasDe(region)

    var regionsExpanded by remember { mutableStateOf(false) }
    var comunasExpanded by remember { mutableStateOf(false) }

    // BLOQUEAR copiar/pegar
    val disabledTextToolbar = object : TextToolbar {
        override val status = TextToolbarStatus.Hidden
        override fun showMenu(
            rect: androidx.compose.ui.geometry.Rect,
            onCopyRequested: (() -> Unit)?,
            onPasteRequested: (() -> Unit)?,
            onCutRequested: (() -> Unit)?,
            onSelectAllRequested: (() -> Unit)?
        ) = Unit
        override fun hide() = Unit
    }

    CompositionLocalProvider(LocalTextToolbar provides disabledTextToolbar) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 24.dp)
                    .padding(top = 56.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                Text(
                    "Registro",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                // CAMPOS NORMALES
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    colors = pastelOutlinedTextFieldColors()
                )

                OutlinedTextField(
                    value = apellido,
                    onValueChange = { apellido = it },
                    label = { Text("Apellido") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    colors = pastelOutlinedTextFieldColors()
                )

                OutlinedTextField(
                    value = rutText,
                    onValueChange = { rutText = it.filter(Char::isDigit) },
                    label = { Text("RUT (solo números)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    colors = pastelOutlinedTextFieldColors()
                )

                OutlinedTextField(
                    value = direccion,
                    onValueChange = { direccion = it },
                    label = { Text("Dirección") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    colors = pastelOutlinedTextFieldColors()
                )

                // REGIÓN
                ExposedDropdownMenuBox(
                    expanded = regionsExpanded,
                    onExpandedChange = { regionsExpanded = !regionsExpanded }
                ) {
                    OutlinedTextField(
                        value = region,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Región") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = regionsExpanded)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = pastelOutlinedTextFieldColors()
                    )

                    DropdownMenu(
                        expanded = regionsExpanded,
                        onDismissRequest = { regionsExpanded = false }
                    ) {
                        regiones.forEach { r ->
                            DropdownMenuItem(onClick = {
                                region = r
                                comuna = ""
                                regionsExpanded = false
                            }) {
                                Text(r)
                            }
                        }
                    }
                }

                // COMUNA
                ExposedDropdownMenuBox(
                    expanded = comunasExpanded,
                    onExpandedChange = {
                        if (region.isNotBlank()) comunasExpanded = !comunasExpanded
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
                        modifier = Modifier.fillMaxWidth(),
                        colors = pastelOutlinedTextFieldColors()
                    )

                    DropdownMenu(
                        expanded = comunasExpanded,
                        onDismissRequest = { comunasExpanded = false }
                    ) {
                        comunas.forEach { c ->
                            DropdownMenuItem(onClick = {
                                comuna = c
                                comunasExpanded = false
                            }) {
                                Text(c)
                            }
                        }
                    }
                }

                // EMAIL
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
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    colors = pastelOutlinedTextFieldColors()
                )

                // CONTRASEÑA
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    visualTransformation = PasswordVisualTransformation(),
                    colors = pastelOutlinedTextFieldColors()
                )

                // BOTÓN REGISTRO
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

                // BOTÓN PARA IR AL LOGIN
                TextButton(
                    onClick = {
                        navController.navigate("login") {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = pastelTextButtonColors()
                ) {
                    Text(
                        "¿Ya tienes cuenta? Inicia sesión",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // MENSAJE DEL VIEWMODEL
                Text(viewModel.mensaje.value, modifier = Modifier.padding(top = 6.dp))
            }
        }
    }
}
