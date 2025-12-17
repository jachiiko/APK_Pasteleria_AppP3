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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.model.NutrientInfo
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.model.Producto
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.ui.theme.pastelButtonColors
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.ui.theme.pastelOutlinedTextFieldColors
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.viewmodel.CatalogViewModel

/**
 * Pantalla "Agregar Producto" ahora guarda en el catálogo en memoria.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(navController: NavController, parentEntry: NavBackStackEntry) {

    val catalogVM: CatalogViewModel = viewModel(parentEntry)

    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var imageInfo by remember { mutableStateOf("") }
    var sku by remember { mutableStateOf("") }
    var lot by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var nutritionalTable by remember { mutableStateOf("") }

    val isValid = name.isNotBlank() && price.toIntOrNull() != null && sku.isNotBlank() && stock.toIntOrNull() != null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agregar Producto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    val nutritionalInfo = nutritionalTable.lines()
                        .map { it.trim() }
                        .filter { it.isNotBlank() }
                        .map { line ->
                            val parts = line.split("|").map { it.trim() }
                            NutrientInfo(
                                name = parts.getOrElse(0) { "Dato" },
                                perServing = parts.getOrElse(1) { "" },
                                totalProduct = parts.getOrElse(2) { "" }
                            )
                        }

                    val product = Producto(
                        id = sku.uppercase(),
                        category = category.ifBlank { "Sin categoría" },
                        name = name,
                        description = description.ifBlank { "Sin descripción" },
                        price = price.toIntOrNull() ?: 0,
                        imageRes = null,
                        stock = stock.toIntOrNull() ?: 0,
                        sku = sku.uppercase(),
                        lotNumber = lot,
                        nutritionalInfo = nutritionalInfo
                    )
                    catalogVM.addProduct(product)
                    navController.popBackStack()
                },
                enabled = isValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                colors = pastelButtonColors(),
            ) {
                Text(
                    "Guardar producto",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre del producto") },
                modifier = Modifier.fillMaxWidth(),
                colors = pastelOutlinedTextFieldColors(),
            )

            OutlinedTextField(
                value = price,
                onValueChange = { price = it.filter { ch -> ch.isDigit() } },
                label = { Text("Precio (CLP)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                colors = pastelOutlinedTextFieldColors(),
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                colors = pastelOutlinedTextFieldColors(),
            )

            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Categoría") },
                modifier = Modifier.fillMaxWidth(),
                colors = pastelOutlinedTextFieldColors(),
            )

            OutlinedTextField(
                value = sku,
                onValueChange = { sku = it },
                label = { Text("SKU") },
                modifier = Modifier.fillMaxWidth(),
                colors = pastelOutlinedTextFieldColors(),
                supportingText = { Text("Se usará como identificador del producto") }
            )

            OutlinedTextField(
                value = lot,
                onValueChange = { lot = it },
                label = { Text("Lote") },
                modifier = Modifier.fillMaxWidth(),
                colors = pastelOutlinedTextFieldColors(),
            )

            OutlinedTextField(
                value = stock,
                onValueChange = { stock = it.filter { ch -> ch.isDigit() } },
                label = { Text("Stock disponible") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                colors = pastelOutlinedTextFieldColors(),
            )

            OutlinedTextField(
                value = nutritionalTable,
                onValueChange = { nutritionalTable = it },
                label = { Text("Tabla nutricional") },
                supportingText = { Text("Formato: Nutriente|por porción|total (una línea por nutriente)") },
                modifier = Modifier.fillMaxWidth(),
                colors = pastelOutlinedTextFieldColors(),
            )

            OutlinedTextField(
                value = imageInfo,
                onValueChange = { imageInfo = it },
                label = { Text("Imagen (referencia visual)") },
                supportingText = { Text("Ej: nombre del drawable o link") },
                modifier = Modifier.fillMaxWidth(),
                colors = pastelOutlinedTextFieldColors(),
            )
        }
    }
}