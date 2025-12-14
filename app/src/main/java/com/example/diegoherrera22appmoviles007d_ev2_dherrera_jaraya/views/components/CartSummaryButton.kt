package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.ui.theme.pastelButtonColors
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.viewmodel.CatalogViewModel
import java.text.NumberFormat

@Composable
fun CartSummaryButton(
    catalogVM: CatalogViewModel,
    moneyFormatter: NumberFormat,
    modifier: Modifier = Modifier,
    onNavigateToCart: () -> Unit
) {
    val items = catalogVM.itemsCount()
    val total = catalogVM.totalCLP()

    Button(
        onClick = onNavigateToCart,
        modifier = modifier.fillMaxWidth(),
        enabled = items > 0,
        colors = pastelButtonColors()
    ) {
        Text("Ir al carrito: $items • ${moneyFormatter.format(total)}")
    }
}