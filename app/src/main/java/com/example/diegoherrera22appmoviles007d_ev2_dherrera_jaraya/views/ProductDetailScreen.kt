@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.repository.ProductRepository
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.repository.ProductSpecifications
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.repository.NutrientInfo
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.ui.theme.pastelButtonColors
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.ui.theme.SoftPink
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.viewmodel.CatalogViewModel
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views.components.CartSummaryButton
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ProductDetailScreen(
    productId: String,
    navController: NavController,
    parentEntry: NavBackStackEntry
) {
    val catalogVM: CatalogViewModel = viewModel(parentEntry)

    val product = remember(productId) {
        catalogVM.products.firstOrNull { it.id == productId } ?: ProductRepository.getById(productId)
    }
    val detailDescription = remember(productId) { ProductRepository.getDetailDescription(productId) }
    val specifications: ProductSpecifications? = remember(productId) { catalogVM.getSpecifications(productId) }

    val money = remember {
        NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply { maximumFractionDigits = 0 }
    }

    var quantity by remember { mutableStateOf(1) }
    var showSpecifications by remember { mutableStateOf(false) }

    val contentProduct = product

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (contentProduct == null) "Producto no encontrado" else "Producto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
    ) { inner ->
        val layoutDirection = LocalLayoutDirection.current
        val startPadding = inner.calculateStartPadding(layoutDirection)
        val endPadding = inner.calculateEndPadding(layoutDirection)
        val topPadding = inner.calculateTopPadding()
        val bottomPadding = inner.calculateBottomPadding()

        if (contentProduct == null) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(
                        start = startPadding,
                        top = topPadding,
                        end = endPadding,
                        bottom = bottomPadding
                    )
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) { Text("No encontramos este producto.") }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = startPadding,
                        top = topPadding,
                        end = endPadding,
                        bottom = bottomPadding
                    )
                    .padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 16.dp)
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val cardShape = RoundedCornerShape(16.dp)

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(4.dp, cardShape, clip = false)
                            .clip(cardShape),
                        shape = cardShape,
                        color = Color.White
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            if (contentProduct.imageRes != null) {
                                Image(
                                    painter = painterResource(contentProduct.imageRes),
                                    contentDescription = contentProduct.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(220.dp)
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(220.dp)
                                        .background(MaterialTheme.colorScheme.surfaceVariant),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Imagen próximamente",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Text(
                                contentProduct.name,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                contentProduct.category,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(money.format(contentProduct.price), style = MaterialTheme.typography.titleLarge)

                            Text(
                                detailDescription ?: contentProduct.description,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Box {
                                val chipLabelStyle = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Medium)

                                FilterChip(
                                    selected = showSpecifications,
                                    onClick = { showSpecifications = !showSpecifications },
                                    label = { Text("Especificaciones", style = chipLabelStyle) },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = if (showSpecifications) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                                            contentDescription = null
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        containerColor = Color.White,
                                        selectedContainerColor = SoftPink,
                                        labelColor = MaterialTheme.colorScheme.onSurface,
                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                        iconColor = MaterialTheme.colorScheme.onSurface,
                                        selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                )

                                DropdownMenu(
                                    expanded = showSpecifications,
                                    onDismissRequest = { showSpecifications = false },
                                    offset = DpOffset(0.dp, 8.dp),
                                    containerColor = Color.White
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(12.dp)
                                            .widthIn(min = 280.dp, max = 360.dp),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Text(
                                            "Especificaciones Producto",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )

                                        if (specifications != null) {
                                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                                Row(modifier = Modifier.fillMaxWidth()) {
                                                    Text(
                                                        "SKU:",
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    Text(
                                                        specifications.sku,
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        modifier = Modifier.padding(start = 4.dp)
                                                    )
                                                }
                                                Row(modifier = Modifier.fillMaxWidth()) {
                                                    Text(
                                                        "Lote:",
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    Text(
                                                        specifications.lotNumber,
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        modifier = Modifier.padding(start = 4.dp)
                                                    )
                                                }
                                            }

                                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                                val headerStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                                val dataColumnIndent = 12.dp
                                                Row(modifier = Modifier.fillMaxWidth()) {
                                                    Text("Nutirentes", modifier = Modifier.weight(1f), style = headerStyle)
                                                    Text(
                                                        "Por porción",
                                                        modifier = Modifier
                                                            .weight(1f)
                                                            .padding(start = dataColumnIndent),
                                                        style = headerStyle,
                                                        softWrap = false
                                                    )
                                                    Text(
                                                        "Total producto",
                                                        modifier = Modifier
                                                            .weight(1f)
                                                            .padding(start = dataColumnIndent),
                                                        style = headerStyle
                                                    )
                                                }

                                                Divider()

                                                Spacer(modifier = Modifier.height(8.dp))

                                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                                    specifications.nutritionalInfo.forEach { nutrient: NutrientInfo ->
                                                        Row(
                                                            modifier = Modifier
                                                                .fillMaxWidth()
                                                                .padding(vertical = 6.dp)
                                                        ) {
                                                            Text(
                                                                nutrient.name,
                                                                modifier = Modifier.weight(1f),
                                                                style = MaterialTheme.typography.bodyMedium
                                                            )
                                                            Text(
                                                                nutrient.perServing,
                                                                modifier = Modifier
                                                                    .weight(1f)
                                                                    .padding(start = dataColumnIndent),
                                                                style = MaterialTheme.typography.bodyMedium
                                                            )
                                                            Text(
                                                                nutrient.totalProduct,
                                                                modifier = Modifier
                                                                    .weight(1f)
                                                                    .padding(start = dataColumnIndent),
                                                                style = MaterialTheme.typography.bodyMedium
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        } else {
                                            Text(
                                                "Pronto agregaremos las especificaciones para este producto.",
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Cantidad", style = MaterialTheme.typography.titleMedium)
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    IconButton(
                                        onClick = { if (quantity > 1) quantity-- }
                                    ) { Icon(Icons.Filled.Remove, contentDescription = "Disminuir cantidad") }

                                    Text(
                                        quantity.toString(),
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.padding(horizontal = 4.dp)
                                    )

                                    IconButton(onClick = { quantity++ }) {
                                        Icon(Icons.Filled.Add, contentDescription = "Aumentar cantidad")
                                    }
                                }
                            }
                        }
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            catalogVM.addToCart(contentProduct, quantity)
                            quantity = 1
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = pastelButtonColors()
                    ) { Text("Agregar al carrito") }

                    CartSummaryButton(
                        catalogVM = catalogVM,
                        moneyFormatter = money,
                        onNavigateToCart = { navController.navigate("cart") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}