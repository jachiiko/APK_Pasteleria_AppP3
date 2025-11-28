package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.views.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.painterResource
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.model.Producto
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ProductCard (
    product: Producto,
    onAddToCart: (Producto) -> Unit,
    onClick: (Producto) -> Unit, //este de aqui es para ir a detalles
    modifier: Modifier = Modifier
) {
    val money = NumberFormat.getCurrencyInstance(Locale("es", "CL")).apply { maximumFractionDigits = 0 }

    Card(onClick = { onClick(product) }, //se le puede hacer click
    modifier = modifier,
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            )

            Text(product.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(product.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(money.format(product.price), style = MaterialTheme.typography.titleMedium)
                Button(onClick = { onAddToCart(product) }) { Text("Agregar") }
            }
        }
    }
}
