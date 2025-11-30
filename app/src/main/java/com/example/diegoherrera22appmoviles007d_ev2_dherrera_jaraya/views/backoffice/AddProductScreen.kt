package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views.backoffice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.ui.theme.PastelButtonColors
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.ui.theme.PastelOutlinedTextFieldColors

/**
 * Pantalla "Agregar Producto" SOLO VISUAL (no guarda en ningún lado).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(navController: NavController) {

    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var imageInfo by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agregar Producto (solo visual)") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre del producto") },
                modifier = Modifier.fillMaxWidth(),
                colors = PastelOutlinedTextFieldColors
            )

            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Precio (CLP)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                colors = PastelOutlinedTextFieldColors
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                colors = PastelOutlinedTextFieldColors
            )

            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Categoría") },
                modifier = Modifier.fillMaxWidth(),
                colors = PastelOutlinedTextFieldColors
            )

            OutlinedTextField(
                value = imageInfo,
                onValueChange = { imageInfo = it },
                label = { Text("Imagen (referencia visual)") },
                supportingText = { Text("Ej: nombre del drawable o link (no funcional)") },
                modifier = Modifier.fillMaxWidth(),
                colors = PastelOutlinedTextFieldColors
            )

            Button(
                onClick = {
                    // SOLO VISUAL: no persiste, volvemos atrás
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = PastelButtonColors
            ) {
                Text("Guardar (no funcional)")
            }
        }
    }
}