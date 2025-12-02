package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.model

class Producto (
    val id: String,
    val category: String,
    val name: String,
    val description: String,
    val price: Int,
    val imageRes: Int? = null // para referenciar al drawable
)