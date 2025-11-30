package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.ui.theme.pastelButtonColors
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.ui.theme.pastelTextButtonColors
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.ui.theme.SoftPink
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.viewmodel.CatalogViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutResultTabsScreen(
    navController: NavController,
    parentEntry: NavBackStackEntry
) {
    val catalogVM: CatalogViewModel = viewModel(parentEntry)
    val order = remember(catalogVM.cartLines) { catalogVM.buildOrderSummary() }

    var selectedTab by remember { mutableStateOf(0) } // 0=Éxito, 1=Rechazada
    val tabs = listOf("Compra exitosa", "Compra rechazada")

    val money = remember {
        NumberFormat.getCurrencyInstance(Locale("es","CL")).apply { maximumFractionDigits = 0 }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resultado de compra") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .background(MaterialTheme.colorScheme.background)
        ) {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            when (selectedTab) {
                0 -> SuccessTab(
                    order = order,
                    money = money,
                    onContinueShopping = {
                        // Regresar a Home (desde resultados: results -> cart -> home)
                        navController.popBackStack() // back a cart
                        navController.popBackStack() // back a home
                    },
                    onGoCart = {
                        navController.popBackStack() // back a cart
                    },
                    onClearCart = { catalogVM.clearCart() }
                )
                @@ -80,103 +98,113 @@ fun CheckoutResultTabsScreen(
                    money = money,
                    onGoCart = {
                        navController.popBackStack() // back a cart
                    },
                    onReviewPayment = {
                        // Placeholder: por ahora volvemos al carrito
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

@Composable
private fun SuccessTab(
    order: com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.viewmodel.OrderSummary,
    money: NumberFormat,
    onContinueShopping: () -> Unit,
    onGoCart: () -> Unit,
    onClearCart: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("¡Compra exitosa!", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("N° Pedido: ${order.orderId}")
        Text("Fecha: ${order.dateText}")

        Divider()

        Text("Detalle de pedido", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(order.items) { it ->
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("${it.qty} x ${it.name}")
                    Text("${money.format(it.unitPrice)}  →  ${money.format(it.subtotal)}")
                }
            }
        }

        Divider()

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Total", style = MaterialTheme.typography.titleMedium)
            Text(money.format(order.total), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    onClearCart()
                    onContinueShopping()
                },
                modifier = Modifier.weight(1f),
                colors = pastelButtonColors()
            ) { Text("Seguir comprando") }

            OutlinedButton(
                onClick = onGoCart,
                modifier = Modifier.weight(1f),
                colors = pastelTextButtonColors(),
                border = BorderStroke(1.dp, SoftPink)
            ) { Text("Volver al carrito") }
        }
    }
}

@Composable
private fun FailureTab(
    money: NumberFormat,
    onGoCart: () -> Unit,
    onReviewPayment: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("No pudimos procesar tu compra", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(
            "Puede deberse a falta de stock o un error en el pago. Revisa tu carrito o intenta nuevamente.",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(8.dp))

        Button(onClick = onGoCart, modifier = Modifier.fillMaxWidth(), colors = pastelButtonColors()) {
            Text("Volver al carrito")
        }
        OutlinedButton(
            onClick = onReviewPayment,
            modifier = Modifier.fillMaxWidth(),
            colors = pastelTextButtonColors(),
            border = BorderStroke(1.dp, SoftPink)
        ) {
            Text("Revisar métodos de pago")
        }
    }
}