// views/CartScreen.kt
package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.viewmodel.CatalogViewModel
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.viewmodel.CartLine
import java.text.NumberFormat
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    parentEntry: NavBackStackEntry
) {
    val catalogVM: CatalogViewModel = viewModel(parentEntry)
    val lines by remember {
        derivedStateOf { catalogVM.cartLines } // se recalcula cuando cambia el estado del map
    }


    val money = remember {
        NumberFormat.getCurrencyInstance(Locale("es","CL")).apply { maximumFractionDigits = 0 }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carrito") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    if (lines.isNotEmpty()) {
                        TextButton(onClick = { catalogVM.clearCart() }) {
                            Text("Vaciar")
                        }
                    }
                }
            )
        }
    ) { inner ->
        if (lines.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(inner),
                contentAlignment = Alignment.Center
            ) { Text("Tu carrito está vacío") }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(inner)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(lines, key = { it.product.id }) { line ->
                        CartItemRow(
                            line = line,
                            money = money,
                            onInc = { catalogVM.addToCart(line.product, 1) },
                            onDec = { catalogVM.decrement(line.product.id, 1) },
                            onRemove = { catalogVM.removeLine(line.product.id) }
                        )
                    }
                }

                Divider()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Items (${catalogVM.itemsCount()})", style = MaterialTheme.typography.bodyMedium)
                    Text("Total: ${money.format(catalogVM.totalCLP())}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Button(
                    onClick = { navController.navigate("checkout/results") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Finalizar compra")
                }
            }
        }
    }
}

@Composable
private fun CartItemRow(
    line: CartLine,
    money: NumberFormat,
    onInc: () -> Unit,
    onDec: () -> Unit,
    onRemove: () -> Unit
) {
    Card {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(line.product.imageRes),
                contentDescription = line.product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(64.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(line.product.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    "${money.format(line.product.price)} c/u",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    "Subtotal: ${money.format(line.product.price * line.qty)}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(onClick = onDec) { Icon(Icons.Filled.Remove, contentDescription = "Restar") }
                Text("${line.qty}")
                IconButton(onClick = onInc) { Icon(Icons.Filled.Add, contentDescription = "Sumar") }
                IconButton(onClick = onRemove) { Icon(Icons.Filled.Delete, contentDescription = "Quitar") }
            }
        }
    }
}