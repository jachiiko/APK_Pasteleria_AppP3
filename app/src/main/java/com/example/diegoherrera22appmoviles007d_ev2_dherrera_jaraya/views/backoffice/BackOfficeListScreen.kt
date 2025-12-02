package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views.backoffice



import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.model.Producto
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.repository.ProductRepository
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.ui.theme.pastelTextButtonColors

/**
 * Back Office (SOLO VISUAL):
 * - Lista productos desde ProductRepository.getCatalog()
 * - Permite "Eliminar" en la UI (no persiste; solo remueve del estado local)
 * - Botón "Agregar" navega a la pantalla de formulario visual.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackOfficeListScreen(
    onAddProduct: () -> Unit
) {
    // Estado local editable (no se guarda en repo ni disco)
    val products = remember {
        mutableStateListOf<Producto>().apply { addAll(ProductRepository.getCatalog()) }
    }

    var toDelete by remember { mutableStateOf<Producto?>(null) } // para confirmar eliminación
    val openDialog = toDelete != null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Back Office – Productos") },
                actions = {
                    TextButton(onClick = onAddProduct, colors = pastelTextButtonColors()) { Text("Agregar") }
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
                    onDelete = { toDelete = p }
                )
            }
            if (products.isEmpty()) {
                item {
                    Text(
                        "No hay productos (eliminaste todos en esta sesión).",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }

        // Diálogo de confirmación de borrado
        if (openDialog) {
            AlertDialog(
                onDismissRequest = { toDelete = null },
                title = { Text("Eliminar producto") },
                text = { Text("¿Seguro que deseas eliminar \"${toDelete?.name}\"?\n(Acción solo visual)") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            toDelete?.let { products.remove(it) }
                            toDelete = null
                        },
                        colors = pastelTextButtonColors()
                    ) { Text("Eliminar") }
                },
                dismissButton = {
                    TextButton(onClick = { toDelete = null }, colors = pastelTextButtonColors()) { Text("Cancelar") }
                }
            )
        }
    }
}

@Composable
private fun BackOfficeItemCard(
    p: Producto,
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
                Spacer(Modifier.height(6.dp))
                Text(p.description, style = MaterialTheme.typography.bodySmall, maxLines = 3)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }
    }
}