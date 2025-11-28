package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.repository

import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.R
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.model.Producto

object ProductRepository {
    fun getCatalog(): List<Producto> = listOf(
        Producto("p1", "Galletas de Chocolate (12un)", "Deliciosas galletas sabor a chocolate", 4000, R.drawable.galletasdechocolate),
        Producto("p2", "Torta Mil Hojas", "Rica torta mil hojas rellena de manjar", 7000, R.drawable.tortamilhojas),
        Producto("p3", "Pan Dulce", "Delicioso pan dulce hecho a mano", 4000, R.drawable.pandulce),
        Producto("p4", "Cupcakes Halloween", "Cupcakes tenebrosos para este dia especial", 3000, R.drawable.cupcakeshallowen)

    )

    fun getById(id: String): Producto? =
        getCatalog().firstOrNull { it.id == id }
}