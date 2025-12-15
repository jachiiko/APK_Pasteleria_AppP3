@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views



import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.model.FakeDatabase
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.model.Producto
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.viewmodel.CatalogViewModel
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views.components.CartSummaryButton
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views.components.ProductCard
import kotlin.math.max
import kotlin.math.min
import java.text.NumberFormat
import java.util.Locale


@Composable
fun HomeScreen(
    email: String?,
    navController: NavController,
    parentEntry: androidx.navigation.NavBackStackEntry
) {
    // BackStackEntry del nav-graph padre "shop" para compartir el mismo ViewModel
    val backEntry = navController.currentBackStackEntry
    val parentEntry = remember(backEntry) { navController.getBackStackEntry("shop") }

    // Usa la MISMA instancia de CatalogViewModel en Home y Detalle
    val catalogVM: CatalogViewModel = viewModel(parentEntry)

    val money = remember {
        NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply { maximumFractionDigits = 0 }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Catálogo de Pastelería",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold)
                    )
                }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            val displayName = remember(email) {
                email?.let { mail ->
                    FakeDatabase.obtenerPorEmail(mail.trim())?.let { usuario ->
                        "${usuario.nombre} ${usuario.apellido}".trim()
                    }
                }
            }

            if (!displayName.isNullOrBlank()) {
                Text(
                    "Bienvenido, $displayName",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(Modifier.height(6.dp))
            } else if (!email.isNullOrBlank()) {
                Text(
                    "Bienvenido, $email",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(Modifier.height(6.dp))
            }

            var showCategoryMenu by remember { mutableStateOf(false) }
            var showPriceMenu by remember { mutableStateOf(false) }

            var minInput by remember { mutableStateOf(catalogVM.selectedPriceRange.start.toInt().toString()) }
            var maxInput by remember { mutableStateOf(catalogVM.selectedPriceRange.endInclusive.toInt().toString()) }

            LaunchedEffect(catalogVM.selectedPriceRange) {
                minInput = catalogVM.selectedPriceRange.start.toInt().toString()
                maxInput = catalogVM.selectedPriceRange.endInclusive.toInt().toString()
            }

            val categoryChipColors = FilterChipDefaults.filterChipColors(
                containerColor = Color.White,
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                labelColor = MaterialTheme.colorScheme.onSurface,
                selectedLabelColor = MaterialTheme.colorScheme.onSurface,
                iconColor = MaterialTheme.colorScheme.onSurface,
                selectedLeadingIconColor = MaterialTheme.colorScheme.onSurface
            )

            val chipLabelStyle = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Medium)
            val chipLabelColor = MaterialTheme.colorScheme.onSurface

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box {
                    FilterChip(
                        selected = showCategoryMenu,
                        onClick = { showCategoryMenu = !showCategoryMenu },
                        label = { Text("Categorías", style = chipLabelStyle, color = chipLabelColor) },
                        leadingIcon = {
                            Icon(
                                imageVector = if (showCategoryMenu) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                                contentDescription = null
                            )
                        },
                        colors = categoryChipColors
                    )

                    DropdownMenu(
                        expanded = showCategoryMenu,
                        onDismissRequest = { showCategoryMenu = false },
                        offset = DpOffset(0.dp, 8.dp),
                        containerColor = Color.White
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(12.dp)
                                .widthIn(min = 220.dp, max = 360.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text("Filtrar por categoría", style = chipLabelStyle, color = chipLabelColor)
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                catalogVM.categories.forEach { category ->
                                    FilterChip(
                                        selected = catalogVM.selectedCategories.contains(category),
                                        onClick = { catalogVM.toggleCategory(category) },
                                        label = { Text(category, style = chipLabelStyle, color = chipLabelColor) },
                                        colors = categoryChipColors
                                    )
                                }
                            }
                        }
                    }
                }

                Box {
                    FilterChip(
                        selected = showPriceMenu,
                        onClick = { showPriceMenu = !showPriceMenu },
                        label = { Text("Precio", style = chipLabelStyle, color = chipLabelColor) },
                        leadingIcon = {
                            Icon(
                                imageVector = if (showPriceMenu) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                                contentDescription = null
                            )
                        },
                        colors = categoryChipColors
                    )

                    DropdownMenu(
                        expanded = showPriceMenu,
                        onDismissRequest = { showPriceMenu = false },
                        offset = DpOffset(0.dp, 8.dp),
                        containerColor = Color.White
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text("Rango de precios", style = chipLabelStyle, color = chipLabelColor)
                                RangeSlider(
                                    value = catalogVM.selectedPriceRange,
                                    onValueChange = { range -> catalogVM.updatePriceRange(range) },
                                    valueRange = catalogVM.priceRangeLimits
                                )
                                Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                                    OutlinedTextField(
                                        value = minInput,
                                        onValueChange = { newValue ->
                                            minInput = newValue
                                            commitPriceRangeFromInput(
                                                newValue,
                                                maxInput,
                                                catalogVM.priceMinLimit,
                                                catalogVM.priceMaxLimit
                                            ) { min, max ->
                                                catalogVM.updatePriceRange(min..max)
                                            }
                                        },
                                        label = { Text("Mínimo", style = chipLabelStyle, color = chipLabelColor) },
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                        modifier = Modifier.weight(1f)
                                    )
                                    OutlinedTextField(
                                        value = maxInput,
                                        onValueChange = { newValue ->
                                            maxInput = newValue
                                            commitPriceRangeFromInput(
                                                minInput,
                                                newValue,
                                                catalogVM.priceMinLimit,
                                                catalogVM.priceMaxLimit
                                            ) { min, max ->
                                                catalogVM.updatePriceRange(min..max)
                                            }
                                        },
                                        label = { Text("Máximo", style = chipLabelStyle, color = chipLabelColor) },
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                Text(
                                    "${money.format(catalogVM.selectedPriceRange.start.toInt())} - ${money.format(catalogVM.selectedPriceRange.endInclusive.toInt())}",
                                    style = chipLabelStyle,
                                    color = chipLabelColor
                                )
                            }
                        }
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = if (catalogVM.selectedCategories.isEmpty())
                        "Categorías seleccionadas: Todas"
                    else "Categorías seleccionadas:",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (catalogVM.selectedCategories.isNotEmpty()) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        catalogVM.selectedCategories.forEach { selected ->
                            AssistChip(
                                onClick = { catalogVM.toggleCategory(selected) },
                                label = { Text(selected, style = chipLabelStyle) },
                                colors = AssistChipDefaults.assistChipColors(
                                    containerColor = Color.White,
                                    labelColor = MaterialTheme.colorScheme.onSurface,
                                    leadingIconContentColor = MaterialTheme.colorScheme.onSurface,
                                    trailingIconContentColor = MaterialTheme.colorScheme.onSurface
                                ),
                                border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.primary)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 220.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(catalogVM.filteredProducts, key = { it.id }) { p ->
                    ProductCard(
                        product = p, // <- Producto
                        onAddToCart = { added ->
                            catalogVM.addToCart(added)
                        },
                        onClick = { clicked ->
                            // Estás dentro del graph "shop", así que esta ruta funciona:
                            navController.navigate("product/${clicked.id}")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            CartSummaryButton(
                catalogVM = catalogVM,
                moneyFormatter = money,
                onNavigateToCart = { navController.navigate("cart") }
            )
        }
    }
}

private fun commitPriceRangeFromInput(
    minText: String,
    maxText: String,
    minLimit: Float,
    maxLimit: Float,
    onValidRange: (Float, Float) -> Unit
) {
    val minValue = minText.toFloatOrNull()
    val maxValue = maxText.toFloatOrNull()

    if (minValue != null && maxValue != null) {
        val clampedMin = minValue.coerceIn(minLimit, maxLimit)
        val clampedMax = maxValue.coerceIn(minLimit, maxLimit)
        val safeMin = min(clampedMin, clampedMax)
        val safeMax = max(clampedMin, clampedMax)
        onValidRange(safeMin, safeMax)
    }
}