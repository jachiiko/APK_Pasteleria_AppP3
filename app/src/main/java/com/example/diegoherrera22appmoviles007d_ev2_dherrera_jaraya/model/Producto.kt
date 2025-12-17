package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.model

data class NutrientInfo(
    val name: String,
    val perServing: String,
    val totalProduct: String
)

data class Producto(
    val id: String,
    val category: String,
    val name: String,
    val description: String,
    val price: Int,
    val imageRes: Int? = null, // para referenciar al drawable
    val stock: Int = 0,
    val sku: String = "",
    val lotNumber: String = "",
    val nutritionalInfo: List<NutrientInfo> = emptyList()
)