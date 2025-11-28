package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.model.Producto
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.repository.ProductRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class CartLine(
    val product: Producto,
    var qty: Int
)

data class OrderItem(
    val name: String,
    val unitPrice: Int,
    val qty: Int,
    val subtotal: Int
)

data class OrderSummary(
    val orderId: String,
    val dateText: String,
    val items: List<OrderItem>,
    val total: Int
)

class CatalogViewModel : ViewModel() {
    val products: List<Producto> = ProductRepository.getCatalog()

    private val _cart: SnapshotStateMap<String, CartLine> = mutableStateMapOf()
    val cartLines: List<CartLine> get() = _cart.values.toList()

    fun addToCart(product: Producto, amount: Int = 1) {
        val id = product.id
        val line = _cart[id]
        if (line == null) {
            _cart[id] = CartLine(product, amount.coerceAtLeast(1))
        } else {
            _cart[id] = line.copy(qty = line.qty + amount)
        }
    }

    fun decrement(productId: String, amount: Int = 1) {
        val line = _cart[productId] ?: return
        val newQty = line.qty - amount
        if (newQty <= 0) _cart.remove(productId)
        else _cart[productId] = line.copy(qty = newQty)
    }

    fun removeLine(productId: String) { _cart.remove(productId) }
    fun clearCart() { _cart.clear() }

    fun totalCLP(): Int = _cart.values.sumOf { it.product.price * it.qty }
    fun itemsCount(): Int = _cart.values.sumOf { it.qty }
    fun distinctCount(): Int = _cart.size

    fun buildOrderSummary(): OrderSummary {
        val items = cartLines.map {
            OrderItem(
                name = it.product.name,
                unitPrice = it.product.price,
                qty = it.qty,
                subtotal = it.product.price * it.qty
            )
        }
        val fmt = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale("es", "CL"))
        return OrderSummary(
            orderId = "P-${System.currentTimeMillis()}",
            dateText = fmt.format(Date()),
            items = items,
            total = items.sumOf { it.subtotal }
        )
    }
}