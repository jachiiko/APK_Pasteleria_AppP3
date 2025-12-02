package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya

import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.viewmodel.CatalogViewModel
import org.junit.jupiter.api.*

class CatalogViewModelTest {

    private lateinit var viewModel: CatalogViewModel

    @BeforeEach
    fun setUp() {
        viewModel = CatalogViewModel()
    }

    @Test
    fun toggleCategory_filtraProductosPorCategoria() {
        val categoria = viewModel.categories.first()

        viewModel.toggleCategory(categoria)

        Assertions.assertTrue(viewModel.selectedCategories.contains(categoria))
        Assertions.assertTrue(viewModel.filteredProducts.isNotEmpty())
        Assertions.assertTrue(viewModel.filteredProducts.all { it.category == categoria })

        viewModel.toggleCategory(categoria)

        Assertions.assertFalse(viewModel.selectedCategories.contains(categoria))
        Assertions.assertEquals(viewModel.products.size, viewModel.filteredProducts.size)
    }

    @Test
    fun updatePriceRange_filtraProductosPorPrecio() {
        val rango = viewModel.priceRangeLimits.start..(viewModel.priceRangeLimits.start + 10000f)

        viewModel.updatePriceRange(rango)

        Assertions.assertTrue(viewModel.filteredProducts.isNotEmpty())
        Assertions.assertTrue(viewModel.filteredProducts.all { it.price.toFloat() in rango })
    }

    @Test
    fun operacionesDeCarro_actualizanContadoresYTotales() {
        val producto = viewModel.products.first()

        viewModel.addToCart(producto, amount = 2)

        Assertions.assertEquals(2, viewModel.itemsCount())
        Assertions.assertEquals(producto.price * 2, viewModel.totalCLP())
        Assertions.assertEquals(1, viewModel.distinctCount())

        viewModel.decrement(producto.id)

        Assertions.assertEquals(1, viewModel.itemsCount())
        Assertions.assertEquals(producto.price, viewModel.totalCLP())

        viewModel.decrement(producto.id)

        Assertions.assertEquals(0, viewModel.itemsCount())
        Assertions.assertEquals(0, viewModel.totalCLP())
    }

    @Test
    fun buildOrderSummary_reflejaLineasDelCarro() {
        val producto = viewModel.products.first()

        viewModel.addToCart(producto, amount = 3)

        val resumen = viewModel.buildOrderSummary()
        val item = resumen.items.first()

        Assertions.assertEquals(1, resumen.items.size)
        Assertions.assertEquals(producto.name, item.name)
        Assertions.assertEquals(producto.price, item.unitPrice)
        Assertions.assertEquals(3, item.qty)
        Assertions.assertEquals(producto.price * 3, item.subtotal)
        Assertions.assertEquals(producto.price * 3, resumen.total)
    }
}
