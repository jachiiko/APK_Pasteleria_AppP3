package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views.backoffice

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.model.Producto
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.model.NutrientInfo
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.ui.theme.pastelOutlinedTextFieldColors
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.ui.theme.pastelTextButtonColors
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.viewmodel.CatalogViewModel

private data class NutrientRowState(
    val name: String = "",
    val perServing: String = "",
    val totalProduct: String = ""
)

/**
 * Back Office: ahora permite editar lote y stock sobre el catálogo en memoria.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackOfficeListScreen(
    onAddProduct: () -> Unit,
    parentEntry: NavBackStackEntry
) {
    val catalogVM: CatalogViewModel = viewModel(parentEntry)
    val products = catalogVM.products

    var toDelete by remember { mutableStateOf<Producto?>(null) }
    var editingProduct by remember { mutableStateOf<Producto?>(null) }
    var nameInput by remember { mutableStateOf("") }
    var priceInput by remember { mutableStateOf("") }
    var descriptionInput by remember { mutableStateOf("") }
    var categoryInput by remember { mutableStateOf("") }
    var skuInput by remember { mutableStateOf("") }
    var stockInput by remember { mutableStateOf("") }
    var lotInput by remember { mutableStateOf("") }
    val nutrientRows = remember { mutableStateListOf(NutrientRowState()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Back Office – Productos") },
                actions = {
                    TextButton(onClick = onAddProduct, colors = pastelTextButtonColors()) {
                        Text(
                            "Agregar",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(products, key = { it.id }) { p ->
                BackOfficeItemCard(
                    p = p,
                    onEdit = {
                        editingProduct = p
                        nameInput = p.name
                        priceInput = p.price.toString()
                        descriptionInput = p.description
                        categoryInput = p.category
                        skuInput = p.sku
                        stockInput = p.stock.toString()
                        lotInput = p.lotNumber
                        nutrientRows.clear()
                        if (p.nutritionalInfo.isEmpty()) {
                            nutrientRows.add(NutrientRowState())
                        } else {
                            nutrientRows.addAll(
                                p.nutritionalInfo.map {
                                    NutrientRowState(
                                        name = it.name,
                                        perServing = it.perServing,
                                        totalProduct = it.totalProduct
                                    )
                                }
                            )
                        }
                    },
                    onDelete = { toDelete = p }
                )
            }
            if (products.isEmpty()) {
                item {
                    Text(
                        "No hay productos registrados.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }

        if (toDelete != null) {
            AlertDialog(
                onDismissRequest = { toDelete = null },
                title = { Text("Eliminar producto") },
                text = { Text("¿Seguro que deseas eliminar \"${toDelete?.name}\"?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            toDelete?.let { catalogVM.removeProduct(it.id) }
                            toDelete = null
                        },
                        colors = pastelTextButtonColors()
                    ) {
                        Text(
                            "Eliminar",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { toDelete = null }, colors = pastelTextButtonColors()) {
                        Text(
                            "Cancelar",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            )
        }

        if (editingProduct != null) {
            AlertDialog(
                onDismissRequest = { editingProduct = null },
                title = { Text("Editar producto") },
                containerColor = Color.White,
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    ) {
                        OutlinedTextField(
                            value = nameInput,
                            onValueChange = { nameInput = it },
                            label = { Text("Nombre del producto") },
                            colors = pastelOutlinedTextFieldColors()
                        )
                        OutlinedTextField(
                            value = priceInput,
                            onValueChange = { priceInput = it.filter { ch -> ch.isDigit() } },
                            label = { Text("Precio (CLP)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = pastelOutlinedTextFieldColors()
                        )
                        OutlinedTextField(
                            value = descriptionInput,
                            onValueChange = { descriptionInput = it },
                            label = { Text("Descripción") },
                            colors = pastelOutlinedTextFieldColors()
                        )
                        OutlinedTextField(
                            value = categoryInput,
                            onValueChange = { categoryInput = it },
                            label = { Text("Categoría") },
                            colors = pastelOutlinedTextFieldColors()
                        )
                        OutlinedTextField(
                            value = skuInput,
                            onValueChange = { skuInput = it },
                            label = { Text("SKU") },
                            colors = pastelOutlinedTextFieldColors()
                        )
                        OutlinedTextField(
                            value = lotInput,
                            onValueChange = { lotInput = it },
                            label = { Text("Lote") },
                            colors = pastelOutlinedTextFieldColors()
                        )
                        OutlinedTextField(
                            value = stockInput,
                            onValueChange = { stockInput = it.filter { ch -> ch.isDigit() } },
                            label = { Text("Stock disponible") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = pastelOutlinedTextFieldColors()
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "Tabla nutricional",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Nutriente",
                                    modifier = Modifier.weight(1f),
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                )
                                Text(
                                    text = "Por porción",
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 8.dp),
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                )
                                Text(
                                    text = "Total",
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 8.dp),
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                )
                            }

                            nutrientRows.forEachIndexed { index, row ->
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    OutlinedTextField(
                                        value = row.name,
                                        onValueChange = { value ->
                                            nutrientRows[index] = nutrientRows[index].copy(name = value)
                                        },
                                        label = { Text("Nombre") },
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(end = 4.dp),
                                        colors = pastelOutlinedTextFieldColors()
                                    )
                                    OutlinedTextField(
                                        value = row.perServing,
                                        onValueChange = { value ->
                                            nutrientRows[index] = nutrientRows[index].copy(perServing = value)
                                        },
                                        label = { Text("Por porción") },
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(horizontal = 4.dp),
                                        colors = pastelOutlinedTextFieldColors()
                                    )
                                    OutlinedTextField(
                                        value = row.totalProduct,
                                        onValueChange = { value ->
                                            nutrientRows[index] = nutrientRows[index].copy(totalProduct = value)
                                        },
                                        label = { Text("Total producto") },
                                        modifier = Modifier
                                            .weight(1f)
                                            .padding(start = 4.dp),
                                        colors = pastelOutlinedTextFieldColors()
                                    )
                                }
                            }

                            TextButton(
                                onClick = { nutrientRows.add(NutrientRowState()) },
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Text("Agregar nutriente")
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val updatedNutrients = nutrientRows
                                .mapNotNull { row ->
                                    if (row.name.isBlank() && row.perServing.isBlank() && row.totalProduct.isBlank()) {
                                        null
                                    } else {
                                        NutrientInfo(
                                            name = row.name,
                                            perServing = row.perServing,
                                            totalProduct = row.totalProduct
                                        )
                                    }
                                }

                            val updatedProduct = editingProduct!!.copy(
                                name = nameInput.ifBlank { editingProduct!!.name },
                                price = priceInput.toIntOrNull() ?: editingProduct!!.price,
                                description = descriptionInput,
                                category = categoryInput.ifBlank { editingProduct!!.category },
                                sku = skuInput.ifBlank { editingProduct!!.sku }.uppercase(),
                                stock = stockInput.toIntOrNull() ?: editingProduct!!.stock,
                                lotNumber = lotInput,
                                nutritionalInfo = updatedNutrients
                            )

                            catalogVM.updateProduct(updatedProduct)
                            editingProduct = null
                        },
                        colors = pastelTextButtonColors()
                    ) {
                        Text(
                            "Guardar",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { editingProduct = null }, colors = pastelTextButtonColors()) {
                        Text(
                            "Cancelar",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            )
        }
    }
}

@Composable
private fun BackOfficeItemCard(
    p: Producto,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(Modifier.padding(12.dp)) {
            if (p.imageRes != null) {
                Image(
                    painter = painterResource(id = p.imageRes),
                    contentDescription = p.name,
                    modifier = Modifier.size(72.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Imagen\npendiente",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(p.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text("Precio: $${p.price}", style = MaterialTheme.typography.bodyMedium)
                Text("SKU: ${p.sku}", style = MaterialTheme.typography.bodySmall)
                Text("Lote: ${p.lotNumber.ifBlank { "No definido" }}", style = MaterialTheme.typography.bodySmall)
                Text("Stock: ${p.stock}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(6.dp))
                Text(p.description, style = MaterialTheme.typography.bodySmall, maxLines = 3)
                Row(modifier = Modifier.padding(top = 8.dp)) {
                    TextButton(onClick = onEdit, colors = pastelTextButtonColors()) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Editar", fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                    }
                }
            }
        }
    }
}