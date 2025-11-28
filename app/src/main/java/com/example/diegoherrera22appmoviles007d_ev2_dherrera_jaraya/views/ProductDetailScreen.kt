@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavBackStackEntry
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.repository.ProductRepository
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.viewmodel.CatalogViewModel
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.model.Producto
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

@Composable
fun ProductDetailScreen(
    productId: String,
    navController: NavController,
    parentEntry: NavBackStackEntry

) {

    val catalogVM: CatalogViewModel = viewModel(parentEntry)

    val product = remember(productId) { ProductRepository.getById(productId) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Estado + efecto para snackbar (como en Home)
    var lastAdded by remember { mutableStateOf<Producto?>(null) }
    LaunchedEffect(lastAdded) {
        lastAdded?.let { snackbarHostState.showSnackbar("Agregado: ${it.name}") }
    }

    val money = remember {
        NumberFormat.getCurrencyInstance(Locale("es","CL")).apply { maximumFractionDigits = 0 }
    }

    if (product == null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Producto no encontrado") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    }
                )
            }
        ) { inner ->
            Box(
                Modifier.fillMaxSize().padding(inner),
                contentAlignment = Alignment.Center
            ) { Text("No encontramos este producto.") }
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(product.name) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(product.imageRes),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )

            Text(product.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(product.description, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(money.format(product.price), style = MaterialTheme.typography.titleLarge)

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    catalogVM.addToCart(product)
                    lastAdded = product   //
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Agregar al carrito") }
        }
    }
}
