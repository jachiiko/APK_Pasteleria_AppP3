package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.model.FakeDatabase
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.ui.theme.pastelButtonColors
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.ui.theme.pastelOutlinedTextFieldColors
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.viewmodel.CatalogViewModel
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.viewmodel.OrderItem
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.viewmodel.OrderSummary
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.viewmodel.RegionViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    email: String?,
    navController: NavController,
    parentEntry: NavBackStackEntry
) {
    val catalogVM: CatalogViewModel = viewModel(parentEntry)
    val regionViewModel: RegionViewModel = viewModel(parentEntry)
    val user = remember(email) { email?.let { FakeDatabase.obtenerPorEmail(it.trim()) } }

    var nombre by remember(user) { mutableStateOf(user?.nombre.orEmpty()) }
    var apellido by remember(user) { mutableStateOf(user?.apellido.orEmpty()) }
    var direccion by remember(user) { mutableStateOf(user?.direccion.orEmpty()) }
    var comuna by remember(user) { mutableStateOf(user?.comuna.orEmpty()) }
    var region by remember(user) { mutableStateOf(user?.region.orEmpty()) }
    var selectedOrder by remember { mutableStateOf<OrderSummary?>(null) }
    var userDataExpanded by remember { mutableStateOf(false) }
    var saveMessage by remember { mutableStateOf<String?>(null) }

    val regionesState by regionViewModel.regiones.collectAsState()
    val regiones = regionesState.map { it.nombre }
    val comunas = regionViewModel.comunasDe(region)
    var regionsExpanded by remember { mutableStateOf(false) }
    var comunasExpanded by remember { mutableStateOf(false) }

    val money = remember {
        NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply { maximumFractionDigits = 0 }
    }

    LaunchedEffect(email) {
        if (email != null) catalogVM.updateUserEmail(email)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { userDataExpanded = !userDataExpanded },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Datos del usuario",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Icon(
                            imageVector = if (userDataExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                            contentDescription = if (userDataExpanded) "Contraer" else "Expandir"
                        )
                    }

                    AnimatedVisibility(visible = userDataExpanded) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedTextField(
                                value = nombre,
                                onValueChange = { nombre = it },
                                label = { Text("Nombre") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = apellido,
                                onValueChange = { apellido = it },
                                label = { Text("Apellido") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = user?.rut.orEmpty(),
                                onValueChange = {},
                                label = { Text("RUT") },
                                enabled = false,
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = user?.email.orEmpty(),
                                onValueChange = {},
                                label = { Text("Correo") },
                                enabled = false,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Text(
                                "Dirección editable",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Medium
                            )
                            OutlinedTextField(
                                value = direccion,
                                onValueChange = { direccion = it },
                                label = { Text("Dirección") },
                                modifier = Modifier.fillMaxWidth(),
                                colors = pastelOutlinedTextFieldColors()
                            )
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                ExposedDropdownMenuBox(
                                    expanded = regionsExpanded,
                                    onExpandedChange = { regionsExpanded = !regionsExpanded },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    OutlinedTextField(
                                        value = region,
                                        onValueChange = {},
                                        readOnly = true,
                                        label = { Text("Región") },
                                        trailingIcon = {
                                            Icon(
                                                imageVector = if (regionsExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                                                contentDescription = if (regionsExpanded) "Cerrar" else "Abrir"
                                            )
                                        },
                                        modifier = Modifier
                                            .menuAnchor()
                                            .fillMaxWidth(),
                                        colors = pastelOutlinedTextFieldColors()
                                    )

                                    ExposedDropdownMenu(
                                        expanded = regionsExpanded,
                                        onDismissRequest = { regionsExpanded = false },
                                        containerColor = Color.White
                                    ) {
                                        regiones.forEach { r ->
                                            DropdownMenuItem(
                                                text = { Text(r) },
                                                onClick = {
                                                    region = r
                                                    comuna = ""
                                                    regionsExpanded = false
                                                }
                                            )
                                        }
                                    }
                                }

                                ExposedDropdownMenuBox(
                                    expanded = comunasExpanded,
                                    onExpandedChange = { comunasExpanded = !comunasExpanded },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    OutlinedTextField(
                                        value = comuna,
                                        onValueChange = {},
                                        readOnly = true,
                                        label = { Text("Comuna") },
                                        trailingIcon = {
                                            Icon(
                                                imageVector = if (comunasExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                                                contentDescription = if (comunasExpanded) "Cerrar" else "Abrir"
                                            )
                                        },
                                        modifier = Modifier
                                            .menuAnchor()
                                            .fillMaxWidth(),
                                        colors = pastelOutlinedTextFieldColors()
                                    )

                                    ExposedDropdownMenu(
                                        expanded = comunasExpanded,
                                        onDismissRequest = { comunasExpanded = false },
                                        containerColor = Color.White
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
                            }
                            Button(
                                onClick = {
                                    if (!email.isNullOrBlank()) {
                                        FakeDatabase.actualizarDireccion(email, direccion.trim(), comuna.trim(), region.trim())
                                        saveMessage = "Datos guardados"
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = pastelButtonColors()
                            ) {
                                Text(
                                    "Guardar",
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }
                            AnimatedVisibility(visible = saveMessage != null) {
                                Text(
                                    saveMessage.orEmpty(),
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }

            Text(
                "Historial",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            if (catalogVM.orderHistory.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Text(
                        "Aún no tienes boletas registradas.",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(catalogVM.orderHistory) { order ->
                        OrderHistoryCard(order = order, money = money) {
                            selectedOrder = order
                        }
                    }
                }
            }
        }
    }

    selectedOrder?.let { summary ->
        OrderDetailDialog(summary = summary, money = money) {
            selectedOrder = null
        }
    }
}

@Composable
private fun OrderHistoryCard(order: OrderSummary, money: NumberFormat, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(order.orderId, fontWeight = FontWeight.Bold)
            Text(order.dateText, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(order.recipientName, style = MaterialTheme.typography.titleSmall)
            Text(order.shippingAddress, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
                money.format(order.finalTotal),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun OrderDetailDialog(summary: OrderSummary, money: NumberFormat, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Detalle de boleta") },
        confirmButton = {
            Button(onClick = onDismiss) { Text("Cerrar") }
        },
        containerColor = Color.White,
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Pedido: ${summary.orderId}", fontWeight = FontWeight.SemiBold)
                Text(summary.dateText)
                Text("Destinatario: ${summary.recipientName}")
                Text("Dirección: ${summary.shippingAddress}")
                Spacer(Modifier.height(4.dp))
                summary.items.forEach { item ->
                    OrderDetailRow(item = item, money = money)
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    "Total: ${money.format(summary.finalTotal)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}

@Composable
private fun OrderDetailRow(item: OrderItem, money: NumberFormat) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(item.name, fontWeight = FontWeight.SemiBold)
            Text("${item.qty} x ${money.format(item.unitPrice)}", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Text(money.format(item.subtotal))
    }
}