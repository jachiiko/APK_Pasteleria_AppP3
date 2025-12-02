package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya

import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.viewmodel.CatalogViewModel
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
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
    }
})
