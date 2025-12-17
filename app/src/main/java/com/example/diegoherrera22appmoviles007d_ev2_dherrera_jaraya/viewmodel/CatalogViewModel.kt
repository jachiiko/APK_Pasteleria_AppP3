package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.lifecycle.ViewModel
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.model.Producto
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.repository.ProductRepository
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt
import kotlin.ranges.ClosedFloatingPointRange

data class CartLine(
    val product: Producto,
    var qty: Int
)

data class OrderItem(
    val name: String,
    val unitPrice: Int,
    val netPrice: Int,
    val ivaAmount: Int,
    val qty: Int,
    val subtotal: Int
)

data class OrderSummary(
    val orderId: String,
    val dateText: String,
    val recipientName: String,
    val shippingAddress: String,
    val items: List<OrderItem>,
    val total: Int,
    val discountPercent: Int,
    val discountAmount: Int,
    val finalTotal: Int
)

data class AppliedDiscount(
    val code: String,
    val percent: Int
)

data class DiscountResult(
    val success: Boolean,
    val message: String
)

class CatalogViewModel : ViewModel() {
    companion object {
        private const val IVA_RATE = 0.19
        private val AVAILABLE_CODES = mapOf(
            "PASTELITO" to 10,
            "1000SABOR" to 20
        )
    }
    val products: SnapshotStateList<Producto> = ProductRepository.observeProducts()
    val categories: List<String> get() = products.map { it.category }.distinct().sorted()

    var userEmail: String? by mutableStateOf(null)
        private set

    private val _orderHistory = mutableStateListOf<OrderSummary>()
    val orderHistory: List<OrderSummary> get() = _orderHistory

    var lastOrderSummary: OrderSummary? by mutableStateOf(null)
        private set

    private val _selectedCategories = mutableStateListOf<String>()
    val selectedCategories: List<String> get() = _selectedCategories

    private val priceRangeLimitsState: ClosedFloatingPointRange<Float>
        get() {
            val min = products.minOfOrNull { it.price }?.toFloat() ?: 0f
            val max = products.maxOfOrNull { it.price }?.toFloat() ?: 0f
            return min..max
        }

    var selectedPriceRange by mutableStateOf(priceRangeLimitsState)
        private set

    val priceRangeLimits: ClosedFloatingPointRange<Float> get() = priceRangeLimitsState
    val priceMinLimit: Float get() = priceRangeLimitsState.start
    val priceMaxLimit: Float get() = priceRangeLimitsState.endInclusive

    val filteredProducts: List<Producto>
        get() {
            val categoryFilter = selectedCategories.toSet()
            return products.filter { product ->
                (categoryFilter.isEmpty() || categoryFilter.contains(product.category)) &&
                        product.price.toFloat() in selectedPriceRange
            }
        }

    private val _cart: SnapshotStateMap<String, CartLine> = mutableStateMapOf()
    val cartLines: List<CartLine> get() = _cart.values.toList()

    private val usedDiscountsByUser = mutableStateMapOf<String, MutableSet<String>>()

    var appliedDiscount: AppliedDiscount? by mutableStateOf(null)
        private set

    var errorMessage: String? by mutableStateOf(null)
        private set

    private var checkoutRecipient: String? by mutableStateOf(null)
    private var checkoutAddress: String? by mutableStateOf(null)

    fun updateUserEmail(email: String?) {
        userEmail = email
    }

    fun toggleCategory(category: String) {
        if (_selectedCategories.contains(category)) {
            _selectedCategories.remove(category)
        } else {
            _selectedCategories.add(category)
        }
    }

    fun updatePriceRange(range: ClosedFloatingPointRange<Float>) {
        selectedPriceRange = range
    }

    fun addToCart(product: Producto, amount: Int = 1): Boolean {
        val id = product.id
        val line = _cart[id]
        val currentQty = line?.qty ?: 0
        val requested = currentQty + amount
        if (!ProductRepository.hasStock(id, requested)) {
            errorMessage = "No hay stock disponible para ${product.name}."
            return false
        }

        if (line == null) {
            _cart[id] = CartLine(product, amount.coerceAtLeast(1))
        } else {
            _cart[id] = line.copy(qty = requested)
        }
        return true
    }

    fun decrement(productId: String, amount: Int = 1) {
        val line = _cart[productId] ?: return
        val newQty = line.qty - amount
        if (newQty <= 0) _cart.remove(productId)
        else _cart[productId] = line.copy(qty = newQty)
    }

    fun removeLine(productId: String) { _cart.remove(productId) }
    fun clearCart() {
        _cart.clear()
        appliedDiscount = null
        checkoutRecipient = null
        checkoutAddress = null
    }

    fun totalCLP(): Int = _cart.values.sumOf { it.product.price * it.qty }
    fun discountAmount(): Int {
        val discount = appliedDiscount?.percent ?: 0
        return (totalCLP() * discount) / 100
    }
    fun totalWithDiscount(): Int = totalCLP() - discountAmount()
    fun itemsCount(): Int = _cart.values.sumOf { it.qty }
    fun distinctCount(): Int = _cart.size

    fun buildOrderSummary(): OrderSummary {
        val items = cartLines.map {
            val netPrice = priceWithoutIva(it.product.price)
            OrderItem(
                name = it.product.name,
                unitPrice = it.product.price,
                netPrice = netPrice,
                ivaAmount = it.product.price - netPrice,
                qty = it.qty,
                subtotal = it.product.price * it.qty
            )
        }
        val discountPercent = appliedDiscount?.percent ?: 0
        val discountAmount = (items.sumOf { it.subtotal } * discountPercent) / 100
        val finalTotal = items.sumOf { it.subtotal } - discountAmount
        val recipient = checkoutRecipient?.takeIf { it.isNotBlank() } ?: "Destinatario"
        val address = checkoutAddress?.takeIf { it.isNotBlank() } ?: "Dirección no indicada"
        val localeCL = Locale("es", "CL")
        val chileZone = ZoneId.of("America/Santiago")
        val zonedDateTime = ZonedDateTime.now(chileZone)
        val dateFormatter = DateTimeFormatter.ofPattern("EEEE d 'de' MMMM yyyy, HH:mm", localeCL)
        val zoneFormatter = DateTimeFormatter.ofPattern("z", localeCL)
        val dateText = "${zonedDateTime.format(dateFormatter)} hr."
        return OrderSummary(
            orderId = "P-${System.currentTimeMillis()}",
            dateText = dateText,
            recipientName = recipient,
            shippingAddress = address,
            items = items,
            total = items.sumOf { it.subtotal },
            discountPercent = discountPercent,
            discountAmount = discountAmount,
            finalTotal = finalTotal
        )
    }

    fun finalizeOrder(recipient: String, address: String): OrderSummary? {
        val lacking = cartLines.firstOrNull { !ProductRepository.hasStock(it.product.id, it.qty) }
        if (lacking != null) {
            errorMessage = "No hay stock de ${lacking.product.name}."
            return null
        }

        ProductRepository.consumeStock(cartLines.map { it.product.id to it.qty })
        checkoutRecipient = recipient
        checkoutAddress = address
        val summary = buildOrderSummary()
        lastOrderSummary = summary
        _orderHistory.add(0, summary)
        clearCart()
        return summary
    }

    fun updateCheckoutDetails(recipient: String?, address: String?) {
        checkoutRecipient = recipient
        checkoutAddress = address
    }

    fun applyDiscountCode(input: String): DiscountResult {
        val code = input.trim().uppercase()
        val percent = AVAILABLE_CODES[code]
            ?: return DiscountResult(false, "Código inválido o no disponible.")

        val userKey = userEmail ?: "guest"
        val usedCodes = usedDiscountsByUser.getOrPut(userKey) { mutableSetOf() }

        if (appliedDiscount != null && appliedDiscount?.code != code) {
            return DiscountResult(false, "Solo puedes usar un código por compra.")
        }

        if (appliedDiscount?.code == code) {
            return DiscountResult(true, "El código ya está aplicado.")
        }

        if (usedCodes.contains(code)) {
            return DiscountResult(false, "Ya utilizaste este código en una compra anterior.")
        }

        appliedDiscount = AppliedDiscount(code = code, percent = percent)
        usedCodes.add(code)

        return DiscountResult(true, "Aplicando ${percent}% de descuento con $code.")
    }

    fun updateInventory(productId: String, stock: Int, lotNumber: String) {
        ProductRepository.updateInventory(productId, stock, lotNumber)
    }

    fun updateProduct(product: Producto) {
        ProductRepository.updateProduct(product)
        selectedPriceRange = priceRangeLimitsState
    }

    fun addProduct(product: Producto) {
        ProductRepository.addProduct(product)
        selectedPriceRange = priceRangeLimitsState
    }

    fun removeProduct(productId: String) {
        ProductRepository.removeProduct(productId)
        selectedPriceRange = priceRangeLimitsState
    }

    fun consumeError(): String? {
        val msg = errorMessage
        errorMessage = null
        return msg
    }

    private fun priceWithoutIva(priceWithIva: Int): Int {
        return (priceWithIva / (1 + IVA_RATE)).roundToInt()
    }
}