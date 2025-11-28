package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.viewmodel.AuthViewModel
import android.net.Uri

@Composable
fun LoginScreen(navController: NavController, viewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Inicio de Sesión", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        //le puse el tema de transformar la contraseña a oculto, para que no se vea
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            //modifier = Modifier.fillMaxWidth() //no me gusto como se veia, asi que lo cambie
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                val ok = viewModel.login(email, password)
                if (!ok) return@Button

                val e = email.trim().lowercase()
                val destination = when {
                    e.endsWith("@admin.cl") -> "backoffice"
                    e.endsWith("@duoc.cl")  -> "home/$email"   // mantiene tu ruta con parámetro
                    else -> {
                        // si llega acá, el usuario existe pero con dominio no permitido
                        // (idealmente esto no pasará porque RegisterScreen ya lo bloquea)
                        viewModel.mensaje.value = "Dominio no permitido (usa @duoc.cl o @admin.cl)"
                        return@Button
                    }
                }
                navController.navigate(destination) {
                    popUpTo("login") { inclusive = true }
                }
            }

        ) {
            Text("Entrar")
        }

        Text(viewModel.mensaje.value, modifier = Modifier.padding(top = 10.dp))

        TextButton(
            onClick = { navController.navigate("register") },
            modifier = Modifier.padding(top = 4.dp)
        ) {
            Text("¿No tienes cuenta? Regístrate")
            }


    }
}