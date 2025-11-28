package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
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

    // datos desde JSON (assets) via ViewModel/Repository

    // error específico del email
    var emailError by remember { mutableStateOf<String?>(null) }

    val regiones = remember { regionViewModel.regiones }
    val comunas = remember(region) { regionViewModel.comunasDe(region) }

    // control de menús desplegables
    var regionsExpanded by remember { mutableStateOf(false) }
    var comunasExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Registro", style = MaterialTheme.typography.titleLarge)

        // Campos básicos
        OutlinedTextField(
            value = nombre, onValueChange = { nombre = it },
            label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = apellido, onValueChange = { apellido = it },
            label = { Text("Apellido") }, modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = rutText, onValueChange = { rutText = it.filter(Char::isDigit) },
            label = { Text("RUT (solo números, sin DV)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = direccion, onValueChange = { direccion = it },
            label = { Text("Dirección") }, modifier = Modifier.fillMaxWidth()
        )

        // Región (desde JSON)
        ExposedDropdownMenuBox(
            expanded = regionsExpanded,
            onExpandedChange = { regionsExpanded = !regionsExpanded }
        ) {
            OutlinedTextField(
                value = region,
                onValueChange = {},
                readOnly = true,
                label = { Text("Región") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = regionsExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
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
                            comuna = "" // al cambiar región, limpiar comuna
                            regionsExpanded = false
                        }
                    )
                }
            }
        }

        // Comuna dependiente de región (desde JSON)
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
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = comunasExpanded) },
                supportingText = {
                    if (region.isBlank()) Text("Selecciona primero una región")
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
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
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = { Text("Contraseña") }, modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                if (!isAllowedEmail(email)) {
                    emailError = "Solo se permiten correos @duoc.cl o @admin.cl"
                    return@Button}
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
            modifier = Modifier.fillMaxWidth()
        ) { Text("Registrar") }

        Text(viewModel.mensaje.value, modifier = Modifier.padding(top = 10.dp))

        TextButton(onClick = { navController.navigate("login") }) {
            Text("¿Ya tienes cuenta? Inicia sesión")
        }
    }
}