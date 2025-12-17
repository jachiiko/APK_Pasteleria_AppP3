package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya

import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.viewmodel.CatalogViewModel
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotContain

class CatalogViewModelTest : StringSpec({

    lateinit var viewModel: CatalogViewModel

    beforeTest {
        viewModel = CatalogViewModel()
    }

    "toggleCategory debe filtrar productos por categoría" {

        val categoria = viewModel.categories.first()

        viewModel.toggleCategory(categoria)

        viewModel.selectedCategories shouldContain categoria
        viewModel.filteredProducts.isNotEmpty() shouldBe true
        viewModel.filteredProducts.all { it.category == categoria } shouldBe true

        viewModel.toggleCategory(categoria)

        viewModel.selectedCategories shouldNotContain categoria
        viewModel.filteredProducts shouldHaveSize viewModel.products.size
    }

    "updatePriceRange debe filtrar productos por precio" {

        val rango = viewModel.priceRangeLimits.start..(viewModel.priceRangeLimits.start + 10000f)

        viewModel.updatePriceRange(rango)

        viewModel.filteredProducts.isNotEmpty() shouldBe true
        viewModel.filteredProducts.all { it.price.toFloat() in rango } shouldBe true
    }

    "operaciones del carro deben actualizar totales y contadores" {

        val producto = viewModel.products.first()

        viewModel.addToCart(producto, amount = 2)

        viewModel.itemsCount() shouldBe 2
        viewModel.totalCLP() shouldBe (producto.price * 2)
        viewModel.distinctCount() shouldBe 1

        viewModel.decrement(producto.id)

        viewModel.itemsCount() shouldBe 1
        viewModel.totalCLP() shouldBe producto.price

        viewModel.decrement(producto.id)

        viewModel.itemsCount() shouldBe 0
        viewModel.totalCLP() shouldBe 0
    }

    "applyDiscountCode debe controlar códigos aplicados" {
        val producto = viewModel.products.first()
        viewModel.addToCart(producto)

        val resultado = viewModel.applyDiscountCode("pastelito")

        resultado.success shouldBe true
        resultado.message shouldBe "Aplicando 10% de descuento con PASTELITO."
        viewModel.appliedDiscount?.percent shouldBe 10
        viewModel.discountAmount() shouldBe ((producto.price * 10) / 100)
        viewModel.totalWithDiscount() shouldBe (producto.price - viewModel.discountAmount())

        val segundoIntento = viewModel.applyDiscountCode("1000sabor")

        segundoIntento.success shouldBe false
        segundoIntento.message shouldBe "Solo puedes usar un código por compra."
        viewModel.appliedDiscount?.code shouldBe "PASTELITO"
    }

    "finalizeOrder debe generar resumen, limpiar carro y descontar stock" {
        val producto = viewModel.products.first()
        val stockInicial = producto.stock

        viewModel.addToCart(producto, amount = 2)

        val resumen = viewModel.finalizeOrder("Cliente", "Calle 123")

        resumen?.items?.first()?.qty shouldBe 2
        viewModel.itemsCount() shouldBe 0
        viewModel.cartLines shouldHaveSize 0
        viewModel.lastOrderSummary shouldBe resumen
        viewModel.orderHistory.firstOrNull() shouldBe resumen

        val productoActualizado = viewModel.products.first { it.id == producto.id }
        productoActualizado.stock shouldBe (stockInicial - 2)

        // Restaurar stock para evitar efectos colaterales en otras pruebas
        viewModel.updateInventory(producto.id, stockInicial, productoActualizado.lotNumber)
    }

    "buildOrderSummary debe reflejar las líneas del carro" {

        val producto = viewModel.products.first()

        viewModel.addToCart(producto, amount = 3)

        val resumen = viewModel.buildOrderSummary()
        val item = resumen.items.first()

        resumen.items shouldHaveSize 1
        item.name shouldBe producto.name
        item.unitPrice shouldBe producto.price
        item.qty shouldBe 3
        item.subtotal shouldBe (producto.price * 3)
        resumen.total shouldBe (producto.price * 3)
        resumen.discountPercent shouldBe 0
        resumen.discountAmount shouldBe 0
        resumen.finalTotal shouldBe resumen.total
        resumen.recipientName shouldBe "Destinatario"
        resumen.shippingAddress shouldBe "Dirección no indicada"
    }

    "toggleCategory debe combinar múltiples filtros de categoría" {
        val primerasDosCategorias = viewModel.categories.take(2)

        primerasDosCategorias.forEach { viewModel.toggleCategory(it) }

        viewModel.selectedCategories shouldContainExactly primerasDosCategorias
        viewModel.filteredProducts.isNotEmpty() shouldBe true
        viewModel.filteredProducts.all { it.category in primerasDosCategorias } shouldBe true
    }

    "updatePriceRange debe expandirse cuando se agrega un producto más caro" {
        val rangoOriginal = viewModel.priceRangeLimits
        val productoNuevo = viewModel.products.first().copy(id = "NUEVO", price = rangoOriginal.endInclusive.toInt() + 50000)

        viewModel.addProduct(productoNuevo)

        viewModel.priceMaxLimit shouldBe productoNuevo.price.toFloat()
        viewModel.selectedPriceRange shouldBe viewModel.priceRangeLimits

        viewModel.removeProduct(productoNuevo.id)
        viewModel.priceRangeLimits shouldBe rangoOriginal
    }

    "addToCart debe rechazar cantidades mayores al stock y registrar error" {
        val producto = viewModel.products.first { it.stock <= 2 }
        val resultado = viewModel.addToCart(producto, amount = producto.stock + 5)

        resultado shouldBe false
        viewModel.itemsCount() shouldBe 0
        viewModel.consumeError() shouldBe "No hay stock disponible para ${producto.name}."
        viewModel.consumeError() shouldBe null
    }

    "removeLine debe mantener las demás líneas del carro" {
        val producto1 = viewModel.products[0]
        val producto2 = viewModel.products[1]

        viewModel.addToCart(producto1)
        viewModel.addToCart(producto2)

        viewModel.removeLine(producto1.id)

        viewModel.cartLines shouldHaveSize 1
        viewModel.cartLines.first().product.id shouldBe producto2.id
    }

    "clearCart debe limpiar descuentos y restablecer destinatario" {
        val producto = viewModel.products.first()

        viewModel.addToCart(producto)
        viewModel.applyDiscountCode("pastelito")
        viewModel.updateCheckoutDetails("Juan", "Calle 1")

        viewModel.clearCart()

        viewModel.itemsCount() shouldBe 0
        viewModel.appliedDiscount shouldBe null

        val resumen = viewModel.buildOrderSummary()
        resumen.recipientName shouldBe "Destinatario"
        resumen.shippingAddress shouldBe "Dirección no indicada"
    }

    "applyDiscountCode debe permitir reintentar el mismo código" {
        val producto = viewModel.products.first()

        viewModel.addToCart(producto)

        val primeraVez = viewModel.applyDiscountCode("1000sabor")
        val repetido = viewModel.applyDiscountCode("1000sabor")

        primeraVez.success shouldBe true
        repetido.success shouldBe true
        repetido.message shouldBe "El código ya está aplicado."
        viewModel.appliedDiscount?.code shouldBe "1000SABOR"
    }

    "un mismo usuario no puede reutilizar un código en compras distintas" {
        val producto = viewModel.products.first()
        viewModel.updateUserEmail("cliente@correo.com")

        viewModel.addToCart(producto)
        viewModel.applyDiscountCode("pastelito")
        viewModel.clearCart()

        val reuso = viewModel.applyDiscountCode("pastelito")

        reuso.success shouldBe false
        reuso.message shouldBe "Ya utilizaste este código en una compra anterior."
    }

    "updateCheckoutDetails debe reflejarse en el resumen" {
        val producto = viewModel.products.first()
        viewModel.addToCart(producto, amount = 2)
        viewModel.updateCheckoutDetails("Ana", "Av. Siempre Viva 742")

        val resumen = viewModel.buildOrderSummary()

        resumen.recipientName shouldBe "Ana"
        resumen.shippingAddress shouldBe "Av. Siempre Viva 742"
    }

    "consumeError debe limpiar el mensaje de error" {
        val producto = viewModel.products.first { it.stock <= 2 }
        viewModel.addToCart(producto, amount = producto.stock + 10)

        viewModel.errorMessage shouldBe "No hay stock disponible para ${producto.name}."
        viewModel.consumeError() shouldBe "No hay stock disponible para ${producto.name}."
        viewModel.errorMessage shouldBe null
    }

    "updateInventory debe actualizar el stock del producto" {
        val producto = viewModel.products.first()
        val stockOriginal = producto.stock
        val nuevoStock = stockOriginal + 3

        viewModel.updateInventory(producto.id, nuevoStock, producto.lotNumber)

        val actualizado = viewModel.products.first { it.id == producto.id }
        actualizado.stock shouldBe nuevoStock

        viewModel.updateInventory(producto.id, stockOriginal, producto.lotNumber)
    }

    "toggleCategory debe limpiar el filtro al quedarse sin categorías" {
        val categoria = viewModel.categories.first()

        viewModel.toggleCategory(categoria)
        viewModel.selectedCategories shouldContain categoria

        viewModel.toggleCategory(categoria)

        viewModel.selectedCategories shouldHaveSize 0
        viewModel.filteredProducts shouldHaveSize viewModel.products.size
    }

    "updatePriceRange debe permitir dejar todos los productos fuera del rango" {
        val rangoVacio = 0f..0f

        viewModel.updatePriceRange(rangoVacio)

        viewModel.filteredProducts shouldHaveSize 0
    }

    "addToCart debe incrementar la cantidad si el producto ya existe" {
        val producto = viewModel.products.first()

        viewModel.addToCart(producto, amount = 1)
        viewModel.addToCart(producto, amount = 2)

        viewModel.itemsCount() shouldBe 3
        viewModel.cartLines.first().qty shouldBe 3
    }

    "decrement debe eliminar la línea cuando la cantidad llega a cero" {
        val producto = viewModel.products.first()

        viewModel.addToCart(producto, amount = 2)

        viewModel.decrement(producto.id, amount = 2)

        viewModel.itemsCount() shouldBe 0
        viewModel.cartLines shouldHaveSize 0
    }

    "discountAmount debe ser cero cuando no hay descuento aplicado" {
        val producto = viewModel.products.first()

        viewModel.addToCart(producto)

        viewModel.discountAmount() shouldBe 0
        viewModel.totalWithDiscount() shouldBe viewModel.totalCLP()
    }

    "discountAmount debe considerar el porcentaje de descuento aplicado" {
        val producto = viewModel.products.first()

        viewModel.addToCart(producto, amount = 2)
        viewModel.applyDiscountCode("pastelito")

        viewModel.discountAmount() shouldBe (viewModel.totalCLP() * 10) / 100
        viewModel.totalWithDiscount() shouldBe viewModel.totalCLP() - viewModel.discountAmount()
    }

    "buildOrderSummary debe calcular neto e IVA por línea" {
        val producto = viewModel.products.first()

        viewModel.addToCart(producto)

        val resumen = viewModel.buildOrderSummary()
        val item = resumen.items.first()

        (item.netPrice + item.ivaAmount) shouldBe item.unitPrice
        item.subtotal shouldBe (item.unitPrice * item.qty)
    }

    "updateCheckoutDetails debe restablecer valores por defecto si son nulos o vacíos" {
        viewModel.updateCheckoutDetails(null, "")

        val resumen = viewModel.buildOrderSummary()
        resumen.recipientName shouldBe "Destinatario"
        resumen.shippingAddress shouldBe "Dirección no indicada"
    }

    "finalizeOrder debe registrar error si un producto queda sin stock" {
        val producto = viewModel.products.first()
        val stockOriginal = producto.stock

        viewModel.addToCart(producto)
        viewModel.updateInventory(producto.id, 0, producto.lotNumber)

        val resumen = viewModel.finalizeOrder("Cliente", "Calle 1")

        resumen shouldBe null
        viewModel.consumeError() shouldBe "No hay stock de ${producto.name}."

        viewModel.updateInventory(producto.id, stockOriginal, producto.lotNumber)
    }

    "finalizeOrder debe agregar la orden al historial" {
        val producto = viewModel.products.first()
        val stockOriginal = producto.stock

        viewModel.addToCart(producto)
        val resumen = viewModel.finalizeOrder("Cliente", "Calle 1")

        viewModel.orderHistory.firstOrNull() shouldBe resumen
        viewModel.lastOrderSummary shouldBe resumen

        viewModel.updateInventory(producto.id, stockOriginal, producto.lotNumber)
    }

    "applyDiscountCode debe registrar descuentos usados por usuario" {
        viewModel.updateUserEmail("cliente@correo.com")
        val producto = viewModel.products.first()

        viewModel.addToCart(producto)
        viewModel.applyDiscountCode("1000sabor")
        viewModel.clearCart()

        val reintento = viewModel.applyDiscountCode("1000sabor")

        reintento.success shouldBe false
        reintento.message shouldBe "Ya utilizaste este código en una compra anterior."
    }

    "addProduct debe añadir la categoría a la lista ordenada" {
        val nuevaCategoria = "Nueva"
        val productoBase = viewModel.products.first()
        val productoNuevo = productoBase.copy(id = "nuevo-prod", category = nuevaCategoria, price = productoBase.price + 1000)

        viewModel.addProduct(productoNuevo)

        viewModel.categories shouldContain nuevaCategoria

        viewModel.removeProduct(productoNuevo.id)
    }

    "updateProduct debe actualizar el precio y recalcular el rango" {
        val producto = viewModel.products.first()
        val precioOriginal = producto.price
        val productoActualizado = producto.copy(price = precioOriginal + 5000)

        viewModel.updateProduct(productoActualizado)

        viewModel.products.first { it.id == producto.id }.price shouldBe productoActualizado.price
        viewModel.selectedPriceRange shouldBe viewModel.priceRangeLimits

        viewModel.updateProduct(producto.copy(price = precioOriginal))
    }

    "itemsCount debe reflejar la suma de todas las líneas" {
        val producto1 = viewModel.products[0]
        val producto2 = viewModel.products[1]

        viewModel.addToCart(producto1, amount = 2)
        viewModel.addToCart(producto2, amount = 3)

        viewModel.itemsCount() shouldBe 5
    }

        "clearCart debe limpiar líneas, totales y descuentos" {
            val producto = viewModel.products.first()

            viewModel.addToCart(producto, amount = 2)
            viewModel.applyDiscountCode("pastelito")

            viewModel.itemsCount() shouldBe 2
            viewModel.appliedDiscount shouldNotBe null

            viewModel.clearCart()

            viewModel.itemsCount() shouldBe 0
            viewModel.appliedDiscount shouldBe null
            viewModel.cartLines shouldBe emptyList()
            viewModel.totalCLP() shouldBe 0
    }
})
