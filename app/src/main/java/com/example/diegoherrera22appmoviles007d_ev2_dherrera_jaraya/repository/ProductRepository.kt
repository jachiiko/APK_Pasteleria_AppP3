package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.repository

import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.model.Producto
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.R

object ProductRepository {
    private val detailDescriptions = mapOf(
        "TC001" to """
            Porciones: 12 personas
            Peso: 2.500 g (2,5 kg)
            Refrigerado dura: 10 días
            Deliciosa torta de chocolate con capas de ganache y un toque de avellanas. Personalizable con mensajes especiales.
        """.trimIndent(),
        "TC002" to """
            Porciones: 14 personas
            Peso: 2.800 g (2,8 kg)
            Refrigerado dura: 7 días
            Mezcla de frutas frescas y crema chantilly sobre bizcocho de vainilla.
        """.trimIndent(),
        "TT001" to """
            Porciones: 10 personas
            Peso: 2.000 g (2 kg)
            Refrigerado dura: 10 días
            Bizcocho de vainilla clásico con crema pastelera.
        """.trimIndent(),
        "TT002" to """
            Porciones: 10 personas
            Peso: 2.200 g (2,2 kg)
            Refrigerado dura: 10 días
            Torta chilena rellena de manjar y nueces.
        """.trimIndent(),
        "PI001" to """
            Porciones: 1 persona
            Peso: 150 g (0,15 kg)
            Refrigerado dura: 5 días
            Mousse cremoso y suave.
        """.trimIndent(),
        "PI002" to """
            Porciones: 1 persona
            Peso: 180 g (0,18 kg)
            Refrigerado dura: 5 días
            Tiramisú italiano individual.
        """.trimIndent(),
        "PSA001" to """
            Porciones: 10 personas
            Peso: 1.900 g (1,9 kg)
            Refrigerado dura: 7 días
            Torta ligera de naranja, endulzada naturalmente.
        """.trimIndent(),
        "PSA002" to """
            Porciones: 8 personas
            Peso: 1.600 g (1,6 kg)
            Refrigerado dura: 7 días
            Cheesecake cremoso sin azúcar.
        """.trimIndent(),
        "PT001" to """
            Porciones: 1 persona
            Peso: 120 g (0,12 kg)
            Refrigerado dura: 4 día
            Empanada rellena de manzanas especiadas.
        """.trimIndent(),
        "PT002" to """
            Porciones: 6 personas
            Peso: 700 g (0,7 kg)
            Refrigerado dura: 7 días
            Tarta tradicional española de almendras.
        """.trimIndent(),
        "PG001" to """
            Porciones: 1 persona
            Peso: 140 g (0,14 kg)
            Refrigerado dura: 5 días
            Brownie denso sin gluten.
        """.trimIndent(),
        "PG002" to """
            Porciones: 4 personas
            Peso: 450 g (0,45 kg)
            Refrigerado dura: 10 días
            Pan suave y esponjoso sin gluten.
        """.trimIndent(),
        "PV001" to """
            Porciones: 12 personas
            Peso: 2.300 g (2,3 kg)
            Refrigerado dura: 7 días
            Torta vegana de chocolate húmeda y deliciosa.
        """.trimIndent(),
        "PV002" to """
            Porciones: 1 personas
            Peso: 90 g (0,09 kg)
            Refrigerado dura: 10 días
            Galletas veganas crujientes de avena.
        """.trimIndent(),
        "TE001" to """
            Porciones: 15 personas
            Peso: 3.000 g (3 kg)
            Refrigerado dura: 7 días
            Torta especial personalizable.
        """.trimIndent(),
        "TE002" to """
            Porciones: 25 personas
            Peso: 5.000 g (5 kg)
            Refrigerado dura: 7 días
            Torta elegante diseñada para bodas.
        """.trimIndent()
    )

    fun getCatalog(): List<Producto> = listOf(
        Producto(
            "TC001",
            "Tortas Cuadradas",
            "Torta Cuadrada de Chocolate",
            "Deliciosa torta de chocolate con capas de ganache y un toque de avellanas. Personalizable con mensajes especiales.",
            45000,
            R.drawable.tc001
        ),
        Producto(
            "TC002",
            "Tortas Cuadradas",
            "Torta Cuadrada de Frutas",
            "Una mezcla de frutas frescas y crema chantilly sobre un suave bizcocho de vainilla, ideal para celebraciones.",
            50000,
            R.drawable.tc002
        ),
        Producto(
            "TT001",
            "Tortas Circulares",
            "Torta Circular de Vainilla",
            "Bizcocho de vainilla clásico relleno con crema pastelera y cubierto con un glaseado dulce, perfecto para cualquier ocasión.",
            40000,
            R.drawable.tt001
        ),
        Producto(
            "TT002",
            "Tortas Circulares",
            "Torta Circular de Manjar",
            "Torta tradicional chilena con manjar y nueces, un deleite para los amantes de los sabores dulces y clásicos.",
            42000,
            R.drawable.tt002
        ),
        Producto(
            "PI001",
            "Postres Individuales",
            "Mousse de Chocolate",
            "Postre individual cremoso y suave, hecho con chocolate de alta calidad, ideal para los amantes del chocolate.",
            5000,
            R.drawable.pi001
        ),
        Producto(
            "PI002",
            "Postres Individuales",
            "Tiramisú Clásico",
            "Un postre italiano individual con capas de café, mascarpone y cacao, perfecto para finalizar cualquier comida.",
            5500,
            R.drawable.pi002
        ),
        Producto(
            "PSA001",
            "Productos Sin Azúcar",
            "Torta Sin Azúcar de Naranja",
            "Torta ligera y deliciosa, endulzada naturalmente, ideal para quienes buscan opciones más saludables.",
            48000,
            R.drawable.psa001
        ),
        Producto(
            "PSA002",
            "Productos Sin Azúcar",
            "Cheesecake Sin Azúcar",
            "Suave y cremoso, este cheesecake es una opción perfecta para disfrutar sin culpa.",
            47000,
            R.drawable.psa002
        ),
        Producto(
            "PT001",
            "Pastelería Tradicional",
            "Empanada de Manzana",
            "Pastelería tradicional rellena de manzanas especiadas, perfecta para un dulce desayuno o merienda.",
            3000,
            R.drawable.pt001
        ),
        Producto(
            "PT002",
            "Pastelería Tradicional",
            "Tarta de Santiago",
            "Tradicional tarta española hecha con almendras, azúcar, y huevos, una delicia para los amantes de los postres clásicos.",
            6000,
            R.drawable.pt002
        ),
        Producto(
            "PG001",
            "Productos Sin Gluten",
            "Brownie Sin Gluten",
            "Rico y denso, este brownie es perfecto para quienes necesitan evitar el gluten sin sacrificar el sabor.",
            4000,
            R.drawable.pg001
        ),
        Producto(
            "PG002",
            "Productos Sin Gluten",
            "Pan Sin Gluten",
            "Suave y esponjoso, ideal para sándwiches o para acompañar cualquier comida.",
            3500,
            R.drawable.pg002
        ),
        Producto(
            "PV001",
            "Productos Veganos",
            "Torta Vegana de Chocolate",
            "Torta de chocolate húmeda y deliciosa, hecha sin productos de origen animal, perfecta para veganos.",
            50000,
            R.drawable.pv001
        ),
        Producto(
            "PV002",
            "Productos Veganos",
            "Galletas Veganas de Avena",
            "Crujientes y sabrosas, estas galletas son una excelente opción para un snack saludable y vegano.",
            4500,
            R.drawable.pv002
        ),
        Producto(
            "TE001",
            "Tortas Especiales",
            "Torta Especial de Cumpleaños",
            "Diseñada especialmente para celebraciones, personalizable con decoraciones y mensajes únicos.",
            55000,
            R.drawable.te001
        ),
        Producto(
            "TE002",
            "Tortas Especiales",
            "Torta Especial de Boda",
            "Elegante y deliciosa, esta torta está diseñada para ser el centro de atención en cualquier boda.",
            60000,
            R.drawable.te002
        )

    )

    fun getById(id: String): Producto? =
        getCatalog().firstOrNull { it.id == id }

    fun getDetailDescription(id: String): String? = detailDescriptions[id]
}