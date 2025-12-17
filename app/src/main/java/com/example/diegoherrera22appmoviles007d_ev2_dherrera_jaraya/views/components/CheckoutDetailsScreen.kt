package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.ui.theme.pastelButtonColors
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.viewmodel.CatalogViewModel
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.viewmodel.DiscountResult
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.viewmodel.UserViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutDetailsScreen(
    navController: NavController,
    parentEntry: NavBackStackEntry
) {
    val catalogVM: CatalogViewModel = viewModel(parentEntry)
    val userViewModel: UserViewModel = viewModel(parentEntry)
    val userEmail = catalogVM.userEmail

    LaunchedEffect(Unit) {
        userViewModel.loadMe()
    }


    val registeredUser by userViewModel.user.collectAsState()

    val money = remember {
        NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply { maximumFractionDigits = 0 }
    }

    var discountCode by remember { mutableStateOf("") }
    var discountFeedback by remember { mutableStateOf<DiscountResult?>(null) }
    var recipientOption by remember { mutableStateOf(RecipientOption.Me) }
    var recipientName by remember { mutableStateOf("") }
    var recipientLastname by remember { mutableStateOf("") }
    var useSavedAddress by remember { mutableStateOf(true) }
    var customAddress by remember { mutableStateOf("") }
    var customComuna by remember { mutableStateOf("") }
    var customCity by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(catalogVM.errorMessage) {
        catalogVM.consumeError()?.let { snackbarHostState.showSnackbar(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de compra") },
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 4.dp,
                color = Color.White
            ) {
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Código de descuento",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    OutlinedTextField(
                        value = discountCode,
                        onValueChange = {
                            discountCode = it
                            discountFeedback = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Ingresa tu código") },
                        singleLine = true
                    )
                    Button(
                        onClick = {
                            discountFeedback = catalogVM.applyDiscountCode(discountCode)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = discountCode.isNotBlank(),
                        colors = pastelButtonColors()
                    ) {
                        Text("Aplicar código")
                    }
                    val applied = catalogVM.appliedDiscount
                    val feedback = discountFeedback ?: applied?.let {
                        DiscountResult(true, "Aplicando ${it.percent}% de descuento con ${it.code}.")
                    }
                    feedback?.let { result ->
                        Text(
                            text = result.message,
                            color = if (result.success) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                        )
                    }

                    DividerSection(title = "Destinatario")
                    RecipientSection(
                        option = recipientOption,
                        registeredUserName = registeredUser?.let { "${it.nombre} ${it.apellido}" }
                            ?: userEmail,
                        recipientName = recipientName,
                        recipientLastname = recipientLastname,
                        onOptionSelected = { recipientOption = it },
                        onNameChange = { recipientName = it },
                        onLastnameChange = { recipientLastname = it }
                    )

                    DividerSection(title = "Dirección de envío")
                    AddressSection(
                        useSavedAddress = useSavedAddress,
                        savedAddress = registeredUser?.direccion,
                        savedComuna = registeredUser?.comuna,
                        savedRegion = registeredUser?.region?.nombre,
                        customAddress = customAddress,
                        customComuna = customComuna,
                        customCity = customCity,
                        onUseSavedChange = { useSavedAddress = it },
                        onAddressChange = { customAddress = it },
                        onComunaChange = { customComuna = it },
                        onCityChange = { customCity = it },
                        hasSavedAddress = registeredUser != null
                    )

                    DividerSection(title = "Resumen")
                    SummaryRows(
                        subtotal = catalogVM.totalCLP(),
                        discountAmount = catalogVM.discountAmount(),
                        discountInfo = applied?.let { "${it.code} -${it.percent}%" },
                        totalWithDiscount = catalogVM.totalWithDiscount(),
                        money = money
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        val resolvedRecipient = if (recipientOption == RecipientOption.Me) {
                            registeredUser?.let { "${it.nombre} ${it.apellido}" }
                                ?: userEmail
                                ?: "Destinatario"
                        } else {
                            listOf(recipientName, recipientLastname)
                                .filter { it.isNotBlank() }
                                .joinToString(" ")
                                .ifBlank { "Destinatario" }
                        }
                        val user = registeredUser

                        val resolvedAddress = if (useSavedAddress && user != null) {
                            listOfNotNull(
                                user.direccion.takeIf { it.isNotBlank() },
                                user.comuna.takeIf { it.isNotBlank() },
                                user.region.nombre.takeIf { it.isNotBlank() }
                            ).joinToString(", ")
                                .ifBlank { "Dirección no indicada" }
                        } else {
                            listOf(customAddress, customComuna, customCity)
                                .filter { it.isNotBlank() }
                                .joinToString(", ")
                                .ifBlank { "Dirección no indicada" }
                        }

                        val summary = catalogVM.finalizeOrder(
                            recipient = resolvedRecipient,
                            address = resolvedAddress
                        )

                        if (summary != null) {
                            navController.navigate("checkout/success")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = pastelButtonColors()
                ) {
                    Text(
                        "Finalizar compra",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Button(
                    onClick = { navController.navigate("checkout/failure") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = pastelButtonColors()
                ) {
                    Text(
                        "Compra rechazada",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Composable
private fun DividerSection(title: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private enum class RecipientOption { Me, Other }

@Composable
private fun RecipientSection(
    option: RecipientOption,
    registeredUserName: String?,
    recipientName: String,
    recipientLastname: String,
    onOptionSelected: (RecipientOption) -> Unit,
    onNameChange: (String) -> Unit,
    onLastnameChange: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        RecipientOptionCard(
            title = "Para mí",
            description = registeredUserName ?: "Usa tus datos registrados",
            selected = option == RecipientOption.Me,
            onClick = { onOptionSelected(RecipientOption.Me) },
            modifier = Modifier.fillMaxWidth()
        )

        RecipientOptionCard(
            title = "Otro destinatario",
            description = "Enviar a otra persona",
            selected = option == RecipientOption.Other,
            onClick = { onOptionSelected(RecipientOption.Other) },
            modifier = Modifier.fillMaxWidth()
        )

        if (option == RecipientOption.Other) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = recipientName,
                    onValueChange = onNameChange,
                    modifier = Modifier.weight(1f),
                    label = { Text("Nombre") }
                )
                OutlinedTextField(
                    value = recipientLastname,
                    onValueChange = onLastnameChange,
                    modifier = Modifier.weight(1f),
                    label = { Text("Apellido") }
                )
            }
        }
    }
}

@Composable
private fun RecipientOptionCard(
    title: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val background = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.White
    val borderColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline

    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = background),
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, fontWeight = FontWeight.SemiBold)
            Text(description, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun AddressSection(
    useSavedAddress: Boolean,
    savedAddress: String?,
    savedComuna: String?,
    savedRegion: String?,
    customAddress: String,
    customComuna: String,
    customCity: String,
    onUseSavedChange: (Boolean) -> Unit,
    onAddressChange: (String) -> Unit,
    onComunaChange: (String) -> Unit,
    onCityChange: (String) -> Unit,
    hasSavedAddress: Boolean
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        AddressOptionRow(
            selected = useSavedAddress,
            title = "Usar dirección registrada",
            description = savedAddress?.let { address ->
                listOfNotNull(address, savedComuna, savedRegion).joinToString(", ")
            } ?: "No hay una dirección guardada",
            enabled = hasSavedAddress,
            onSelect = { if (hasSavedAddress) onUseSavedChange(true) }
        )

        AddressOptionRow(
            selected = !useSavedAddress,
            title = "Usar otra dirección",
            description = "Ingresa un nuevo destino",
            onSelect = { onUseSavedChange(false) }
        )

        if (!useSavedAddress || !hasSavedAddress) {
            OutlinedTextField(
                value = customAddress,
                onValueChange = onAddressChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Dirección") }
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = customComuna,
                    onValueChange = onComunaChange,
                    modifier = Modifier.weight(1f),
                    label = { Text("Comuna") }
                )
                OutlinedTextField(
                    value = customCity,
                    onValueChange = onCityChange,
                    modifier = Modifier.weight(1f),
                    label = { Text("Ciudad") }
                )
            }
        }
    }
}

@Composable
private fun AddressOptionRow(
    selected: Boolean,
    title: String,
    description: String,
    enabled: Boolean = true,
    onSelect: () -> Unit,
) {
    val background = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.White
    val borderColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline

    Card(
        onClick = { if (enabled) onSelect() },
        colors = CardDefaults.cardColors(containerColor = background),
        border = BorderStroke(1.dp, borderColor),
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            RadioButton(selected = selected, onClick = onSelect, enabled = enabled)
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.SemiBold)
                Text(description, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun SummaryRows(
    subtotal: Int,
    discountAmount: Int,
    discountInfo: String?,
    totalWithDiscount: Int,
    money: NumberFormat
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Subtotal")
            Text(money.format(subtotal))
        }
        if (discountAmount > 0 && discountInfo != null) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Descuento ($discountInfo)", color = MaterialTheme.colorScheme.primary)
                Text("-${money.format(discountAmount)}", color = MaterialTheme.colorScheme.primary)
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                "Total a pagar",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                money.format(totalWithDiscount),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}