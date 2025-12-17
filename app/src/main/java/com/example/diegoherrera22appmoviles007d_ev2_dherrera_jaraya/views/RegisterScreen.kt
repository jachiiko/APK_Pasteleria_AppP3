package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views

import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalTextToolbar
import androidx.compose.ui.platform.TextToolbar
import androidx.compose.ui.platform.TextToolbarStatus
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.model.Region
import androidx.navigation.NavController
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.ui.theme.pastelButtonColors
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.ui.theme.pastelOutlinedTextFieldColors
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.ui.theme.pastelTextButtonColors
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.viewmodel.AuthViewModel
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.viewmodel.RegionViewModel

private fun isValidEmail(email: String): Boolean {
    val cleanedEmail = email.trim()
    if (cleanedEmail.isEmpty()) return false

    val parts = cleanedEmail.split("@")
    if (parts.size != 2) return false

    val domain = parts[1]
    return domain.contains(".") && domain.none { it.isWhitespace() }
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
    var selectedRegion by remember { mutableStateOf<Region?>(null) }
    var comuna by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf<String?>(null) }

    // Regiones y comunas desde el ViewModel correcto
    val regionesState by regionViewModel.regiones.collectAsState()
    val comunas = selectedRegion?.comunas ?: emptyList()

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
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
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
                        onValueChange = {
                            val cleaned = it
                                .uppercase()
                                .filter { char -> char.isDigit() || char == 'K' }

                            val cuerpo = cleaned.takeWhile { ch -> ch.isDigit() }.take(8)
                            val dv = cleaned.drop(cuerpo.length).firstOrNull()
                                ?.takeIf { ch -> ch.isDigit() || ch == 'K' }

                            rutText = buildString {
                                append(cuerpo)
                                if (cuerpo.isNotEmpty() && dv != null) {
                                    append('-')
                                    append(dv)
                                }
                            }
                        },
                        label = { Text("RUT (ej: 12345678-9)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
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
                            value = selectedRegion?.nombre ?: "",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Región") },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = regionsExpanded)
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            colors = pastelOutlinedTextFieldColors()
                        )

                        ExposedDropdownMenu(
                            expanded = regionsExpanded,
                            onDismissRequest = { regionsExpanded = false }
                        ) {
                            regionesState.forEach { region ->
                                DropdownMenuItem(
                                    text = { Text(region.nombre) },
                                    onClick = {
                                        selectedRegion = region
                                        comuna = ""
                                        regionsExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // COMUNA
                    ExposedDropdownMenuBox(
                        expanded = comunasExpanded,
                        onExpandedChange = {
                            if (selectedRegion != null) comunasExpanded = !comunasExpanded
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
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            colors = pastelOutlinedTextFieldColors()
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
                            else Text("Ingresa un correo electrónico válido")
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
                }

                Spacer(modifier = Modifier.height(12.dp))

                // BOTÓN REGISTRO
                Button(
                    onClick = {
                        if (!isValidEmail(email)) {
                            emailError = "Ingresa un correo electrónico válido"
                            return@Button
                        }
                        val regionSeleccionada = selectedRegion ?: return@Button

                        val registrado = viewModel.registrar(
                            nombre = nombre,
                            apellido = apellido,
                            rut = rutText,
                            direccion = direccion,
                            region = regionSeleccionada,
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
                    Text(
                        "Registrar",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
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
            }
        }
    }
}
