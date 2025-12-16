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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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

    if (product == null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Producto no encontrado") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    }
                )
            }
        ) { inner ->
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(inner)
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) { Text("No encontramos este producto.") }
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Producto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 4.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (product.imageRes != null) {
                        Image(
                            painter = painterResource(product.imageRes),
                            contentDescription = product.name,
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
                        product.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        product.category,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(money.format(product.price), style = MaterialTheme.typography.titleLarge)

                    Box {
                        val chipLabelStyle = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Medium)

                        FilterChip(
                            selected = showSpecifications,
                            onClick = { showSpecifications = !showSpecifications },
                            label = { Text("|Especificaciones", style = chipLabelStyle) },
                            leadingIcon = {
                                Icon(
                                    imageVector = if (showSpecifications) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                                    contentDescription = null
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color.White,
                                selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                labelColor = MaterialTheme.colorScheme.onSurface,
                                selectedLabelColor = MaterialTheme.colorScheme.onSurface,
                                iconColor = MaterialTheme.colorScheme.onSurface,
                                selectedLeadingIconColor = MaterialTheme.colorScheme.onSurface
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
                                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Text(
                                            "SKU: ${specifications.sku}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            "Lote: ${specifications.lotNumber}",
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }

                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        val headerStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                        Row(modifier = Modifier.fillMaxWidth()) {
                                            Text("Nutriente", modifier = Modifier.weight(1f), style = headerStyle)
                                            Text("Por porción", modifier = Modifier.weight(1f), style = headerStyle)
                                            Text("Total producto", modifier = Modifier.weight(1f), style = headerStyle)
                                        }

                                        specifications.nutritionalInfo.forEach { nutrient: NutrientInfo ->
                                            Row(modifier = Modifier.fillMaxWidth()) {
                                                Text(
                                                    nutrient.name,
                                                    modifier = Modifier.weight(1f),
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                                Text(
                                                    nutrient.perServing,
                                                    modifier = Modifier.weight(1f),
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                                Text(
                                                    nutrient.totalProduct,
                                                    modifier = Modifier.weight(1f),
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
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

                    Text(
                        detailDescription ?: product.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

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

            Button(
                onClick = {
                    catalogVM.addToCart(product, quantity)
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