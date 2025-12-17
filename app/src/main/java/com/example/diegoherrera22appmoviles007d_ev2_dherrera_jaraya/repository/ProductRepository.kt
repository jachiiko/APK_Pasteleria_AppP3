package com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.repository

import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.model.Producto
import com.example.diegoherrera22appmoviles007d_ev2_dherrera_jaraya.R

data class NutrientInfo(
    val name: String,
    val perServing: String,
    val totalProduct: String
)

data class ProductSpecifications(
    val sku: String,
    val lotNumber: String,
    val nutritionalInfo: List<NutrientInfo>
)

object ProductRepository {
    private val productSpecifications = mapOf(
        "TC001" to ProductSpecifications(
            sku = "TQ-CHOC-GAN-2500",
            lotNumber = "L-202512-TQ",
            nutritionalInfo = listOf(
                NutrientInfo("Energía", "460 kcal", "5.520 kcal"),
                NutrientInfo("Proteínas", "6 g", "72 g"),
                NutrientInfo("Grasas totales", "26 g", "312 g"),
                NutrientInfo("Grasas saturadas", "8 g", "96 g"),
                NutrientInfo("Carbohidratos", "52 g", "624 g"),
                NutrientInfo("Azúcares", "38 g", "456 g"),
                NutrientInfo("Sodio", "180 mg", "2.160 mg"),
            )
        ),
        "TC002" to ProductSpecifications(
            sku = "TQ-FRUT-CHANT-2800",
            lotNumber = "L-202512-TF",
            nutritionalInfo = listOf(
                NutrientInfo("Energía", "390 kcal", "5.460 kcal"),
                NutrientInfo("Proteínas", "18 g", "224 g"),
                NutrientInfo("Grasas totales", "56 g", "700 g"),
                NutrientInfo("Grasas saturadas", "32 g", "224 g"),
                NutrientInfo("Carbohidratos", "35 g", "448 g"),
                NutrientInfo("Azúcares", "30 g", "380 g"),
                NutrientInfo("Fibra", "16 g", "224 g"),
                NutrientInfo("Sodio", "160 mg", "2.240 mg"),
            )
        ),
        "TT001" to ProductSpecifications(
            sku = "TC-VAIN-CLAS-2000",
            lotNumber = "L-202512-TV",
            nutritionalInfo = listOf(
                NutrientInfo("Energía", "420 kcal", "4.200 kcal"),
                NutrientInfo("Proteínas", "24 g", "210 g"),
                NutrientInfo("Grasas totales", "56 g", "500 g"),
                NutrientInfo("Grasas saturadas", "10 g", "90 g"),
                NutrientInfo("Carbohidratos", "52 g", "420 g"),
                NutrientInfo("Azúcares", "32 g", "250 g"),
                NutrientInfo("Fibra", "18 g", "190 g"),
                NutrientInfo("Sodio", "140 mg", "1.500 mg"),
            )
        ),
        "TT002" to ProductSpecifications(
            sku = "TC-MANJ-CLAS-2000",
            lotNumber = "L-202512-TC",
            nutritionalInfo = listOf(
                NutrientInfo("Energía", "480 kcal", "4.800 kcal"),
                NutrientInfo("Proteínas", "22 g", "220 g"),
                NutrientInfo("Grasas totales", "64 g", "640 g"),
                NutrientInfo("Grasas saturadas", "12 g", "120 g"),
                NutrientInfo("Carbohidratos", "58 g", "580 g"),
                NutrientInfo("Azúcares", "32 g", "320 g"),
                NutrientInfo("Fibra", "12 g", "120 g"),
                NutrientInfo("Sodio", "210 mg", "1.900 mg"),
            )
        ),
        "PI001" to ProductSpecifications(
            sku = "PI-MOUS-CHOC-150",
            lotNumber = "L-202512-PI",
            nutritionalInfo = listOf(
                NutrientInfo("Energía", "380 kcal", ""),
                NutrientInfo("Proteínas", "28 g", ""),
                NutrientInfo("Grasas totales", "28 g", ""),
                NutrientInfo("Grasas saturadas", "9 g", ""),
                NutrientInfo("Carbohidratos", "34 g", ""),
                NutrientInfo("Azúcares", "32 g", ""),
                NutrientInfo("Fibra", "9 g", ""),
                NutrientInfo("Sodio", "95 mg", ""),
            )
        ),
        "PI002" to ProductSpecifications(
            sku = "PI-TRA-CLAS-180",
            lotNumber = "L-202512-PI",
            nutritionalInfo = listOf(
                NutrientInfo("Energía", "450 kcal", ""),
                NutrientInfo("Proteínas", "22 g", ""),
                NutrientInfo("Grasas totales", "48 g", ""),
                NutrientInfo("Grasas saturadas", "12 g", ""),
                NutrientInfo("Carbohidratos", "32 g", ""),
                NutrientInfo("Azúcares", "24 g", ""),
                NutrientInfo("Fibra", "8 g", ""),
                NutrientInfo("Sodio", "170 mg", ""),
            )
        ),
        "PSA001" to ProductSpecifications(
            sku = "TS-SA-NAR-1900",
            lotNumber = "L-202512-SA",
            nutritionalInfo = listOf(
                NutrientInfo("Energía", "330 kcal", "3.500 kcal"),
                NutrientInfo("Proteínas", "6 g", "60 g"),
                NutrientInfo("Grasas totales", "20 g", "200 g"),
                NutrientInfo("Grasas saturadas", "8 g", "80 g"),
                NutrientInfo("Carbohidratos", "28 g", "280 g"),
                NutrientInfo("Azúcares", "20 g", "200 g"),
                NutrientInfo("Fibra", "8 g", "80 g"),
                NutrientInfo("Sodio", "110 mg", "1.500 mg"),
            )
        ),
        "PSA002" to ProductSpecifications(
            sku = "CS-SA CREM-1600",
            lotNumber = "L-202512-CS",
            nutritionalInfo = listOf(
                NutrientInfo("Energía", "380 kcal", "2.880 kcal"),
                NutrientInfo("Proteínas", "12 g", "144 g"),
                NutrientInfo("Grasas totales", "18 g", "216 g"),
                NutrientInfo("Grasas saturadas", "8 g", "96 g"),
                NutrientInfo("Carbohidratos", "22 g", "144 g"),
                NutrientInfo("Azúcares", "6 g", "36 g"),
                NutrientInfo("Fibra", "8 g", "112 g"),
                NutrientInfo("Sodio", "210 mg", "1.680 mg"),
            )
        ),
        "PT001" to ProductSpecifications(
            sku = "PP-EMP-MANZ-120",
            lotNumber = "L-202512-PP",
            nutritionalInfo = listOf(
                NutrientInfo("Energía", "320 kcal", ""),
                NutrientInfo("Proteínas", "15 g", ""),
                NutrientInfo("Grasas totales", "15 g", ""),
                NutrientInfo("Grasas saturadas", "4 g", ""),
                NutrientInfo("Carbohidratos", "24 g", ""),
                NutrientInfo("Azúcares", "24 g", ""),
                NutrientInfo("Sodio", "140 mg", ""),
            )
        ),
        "PT002" to ProductSpecifications(
            sku = "PI-SANT-130",
            lotNumber = "L-202512-PI",
            nutritionalInfo = listOf(
                NutrientInfo("Energía", "410 kcal", ""),
                NutrientInfo("Proteínas", "28 g", ""),
                NutrientInfo("Grasas totales", "32 g", ""),
                NutrientInfo("Grasas saturadas", "8 g", ""),
                NutrientInfo("Carbohidratos", "32 g", ""),
                NutrientInfo("Azúcares", "22 g", ""),
                NutrientInfo("Sodio", "60 mg", ""),
            )
        ),
        "PG001" to ProductSpecifications(
            sku = "SG-BROW-140",
            lotNumber = "L-202512-SG",
            nutritionalInfo = listOf(
                NutrientInfo("Energía", "430 kcal", ""),
                NutrientInfo("Proteínas", "24 g", ""),
                NutrientInfo("Grasas totales", "24 g", ""),
                NutrientInfo("Grasas saturadas", "7 g", ""),
                NutrientInfo("Carbohidratos", "30 g", ""),
                NutrientInfo("Azúcares", "30 g", ""),
                NutrientInfo("Sodio", "120 mg", ""),
            )
        ),
        "PG002" to ProductSpecifications(
            sku = "PSG-PAN-BLAN-450",
            lotNumber = "L-202512-SG",
            nutritionalInfo = listOf(
                NutrientInfo("Energía", "240 kcal", "960 kcal"),
                NutrientInfo("Proteínas", "4 g", "16 g"),
                NutrientInfo("Grasas totales", "6 g", "24 g"),
                NutrientInfo("Grasas saturadas", "1 g", "4 g"),
                NutrientInfo("Carbohidratos", "42 g", "168 g"),
                NutrientInfo("Azúcares", "8 g", "32 g"),
                NutrientInfo("Sodio", "220 mg", "880 mg"),
            )
        ),
        "PV001" to ProductSpecifications(
            sku = "TV-CHOC-VEG-2300",
            lotNumber = "L-202512-VEG",
            nutritionalInfo = listOf(
                NutrientInfo("Energía", "420 kcal", "5.600 kcal"),
                NutrientInfo("Proteínas", "5 g", "60 g"),
                NutrientInfo("Grasas totales", "32 g", "648 g"),
                NutrientInfo("Grasas saturadas", "8 g", "200 g"),
                NutrientInfo("Carbohidratos", "52 g", "740 g"),
                NutrientInfo("Azúcares", "32 g", "460 g"),
                NutrientInfo("Fibra", "22 g", "640 g"),
                NutrientInfo("Sodio", "160 mg", "1.920 mg"),
            )
        ),
        "PV002" to ProductSpecifications(
            sku = "VEG-GALL-AVE-90",
            lotNumber = "L-202512-VEG",
            nutritionalInfo = listOf(
                NutrientInfo("Energía", "360 kcal", ""),
                NutrientInfo("Proteínas", "12 g", ""),
                NutrientInfo("Grasas totales", "24 g", ""),
                NutrientInfo("Grasas saturadas", "12 g", ""),
                NutrientInfo("Carbohidratos", "68 g", ""),
                NutrientInfo("Azúcares", "44 g", ""),
                NutrientInfo("Fibra", "12 g", ""),
                NutrientInfo("Sodio", "90 mg", ""),
            )
        ),
        "TE001" to ProductSpecifications(
            sku = "TE-CUMP-PERS-3000",
            lotNumber = "L-202512-CUMP",
            nutritionalInfo = listOf(
                NutrientInfo("Energía", "470 kcal", "4.600 kcal"),
                NutrientInfo("Proteínas", "22 g", "160 g"),
                NutrientInfo("Grasas totales", "18 g", "130 g"),
                NutrientInfo("Grasas saturadas", "8 g", "60 g"),
                NutrientInfo("Carbohidratos", "60 g", "1.500 g"),
                NutrientInfo("Azúcares", "28 g", "900 g"),
                NutrientInfo("Fibra", "12 g", "320 g"),
                NutrientInfo("Sodio", "200 mg", "3.000 mg"),
            )
        ),
        "TE002" to ProductSpecifications(
            sku = "TE-BODA-PREM-5000",
            lotNumber = "L-202512-ESP",
            nutritionalInfo = listOf(
                NutrientInfo("Energía", "450 kcal", "6.000 kcal"),
                NutrientInfo("Proteínas", "32 g", "400 g"),
                NutrientInfo("Grasas totales", "12 g", "1.450 g"),
                NutrientInfo("Grasas saturadas", "8 g", "900 g"),
                NutrientInfo("Carbohidratos", "68 g", "8.400 g"),
                NutrientInfo("Azúcares", "32 g", "3.800 g"),
                NutrientInfo("Fibra", "20 g", "3.000 g"),
                NutrientInfo("Sodio", "190 mg", "4.750 mg"),
            )
        )
    )

    private val detailDescriptions = mapOf(
        "TC001" to """
            Porciones: 12 personas
            Peso: 2.500 g (2,5 kg)
            Refrigerado dura: 10 días
            Calorías por porción: 460 kcal
            Calorías totales: 5.520 kcal
            
            Bizcocho de chocolate húmedo relleno y cubierto con crema de chocolate.
            Sabor intenso y textura suave, ideal para compartir en celebraciones.
            Formato cuadrado pensado para porciones generosas.
        """.trimIndent(),

        "TC002" to """
            Porciones: 14 personas
            Peso: 2.800 g (2,8 kg)
            Refrigerado dura: 7 días
            Calorías por porción: 390 kcal
            Calorías totales: 5.460 kcal
            
            Bizcocho suave relleno con crema y frutas, decorada con fruta fresca.
            Sabor fresco y ligero, con contraste entre dulzor y acidez.
            Ideal para eventos diurnos y celebraciones familiares.
        """.trimIndent(),

        "TT001" to """
            Porciones: 10 personas
            Peso: 2.000 g (2 kg)
            Refrigerado dura: 10 días
            Calorías por porción: 420 kcal
            Calorías totales: 4.200 kcal
            
            Bizcocho de vainilla esponjoso con relleno de crema suave.
            Sabor equilibrado y delicado, no excesivamente dulce.
            Ideal para acompañar café o compartir en familia.
        """.trimIndent(),

        "TT002" to """
            Porciones: 10 personas
            Peso: 2.200 g (2,2 kg)
            Refrigerado dura: 10 días
            Calorías por porción: 480 kcal
            Calorías totales: 4.800 kcal
            
            Capas de bizcocho de vainilla rellenas con abundante manjar.
            Postre clásico, dulce y suave, muy popular en celebraciones.
            Presentación circular ideal para cumpleaños y reuniones.
        """.trimIndent(),

        "PI001" to """
            Porciones: 1 persona
            Peso: 150 g (0,15 kg)
            Refrigerado dura: 5 días
            Calorías por porción: 380 kcal
            
            Postre individual de mousse de chocolate cremoso.
            Textura aireada y sabor intenso.
            Ideal como postre personal.
        """.trimIndent(),

        "PI002" to """
            Porciones: 1 persona
            Peso: 180 g (0,18 kg)
            Refrigerado dura: 5 días
            Calorías por porción: 450 kcal
            
            Postre individual con capas suaves y sabor a café.
            Equilibrio entre dulzor y amargor.
            Clásico listo para disfrutar.
        """.trimIndent(),

        "PSA001" to """
            Porciones: 10 personas
            Peso: 1.900 g (1,9 kg)
            Refrigerado dura: 7 días
            Calorías por porción: 330 kcal
            Calorías totales: 3.500 kcal
            
            Torta sin azúcar añadida con aroma natural a naranja.
            Textura suave y sabor fresco, ligeramente cítrico.
            Pensada para quienes buscan una opción más equilibrada.
        """.trimIndent(),

        "PSA002" to """
            Porciones: 8 personas
            Peso: 1.600 g (1,6 kg)
            Refrigerado dura: 7 días
            Calorías por porción: 380 kcal
            Calorías totales: 2.880 kcal
            
            Cheesecake cremoso preparado sin azúcar añadida.
            Mantiene el sabor clásico del queso crema.
            Opción ideal para dietas controladas.
        """.trimIndent(),

        "PT001" to """
            Porciones: 1 persona
            Peso: 120 g (0,12 kg)
            Refrigerado dura: 4 día
            Calorías por porción: 320 kcal
            
            Empanada dulce rellena con manzana cocida.
            Masa dorada y relleno suave.
            Perfecta como colación o postre rápido.
        """.trimIndent(),

        "PT002" to """
            Porciones: 1 personas
            Peso: 130 g (0,13 kg)
            Refrigerado dura: 7 días
            Calorías por porción: 410 kcal
            
            Tarta individual elaborada a base de almendras.
            Textura densa y sabor tradicional.
            Ideal para acompañar bebidas calientes.
        """.trimIndent(),

        "PG001" to """
            Porciones: 1 persona
            Peso: 140 g (0,14 kg)
            Refrigerado dura: 5 días
            Calorías por porción: 430 kcal
            
            Brownie individual sin gluten de textura húmeda.
            Sabor intenso a chocolate.
            Apto para personas con intolerancia al gluten.
        """.trimIndent(),

        "PG002" to """
            Porciones: 4 personas
            Peso: 450 g (0,45 kg)
            Refrigerado dura: 10 días
            Calorías por porción: 240 kcal
            Calorías totales: 960 kcal
            
            Pan elaborado sin gluten, de miga suave y corteza ligera.
            Apto para personas con intolerancia al gluten.
            Ideal para consumo diario o acompañar comidas.
        """.trimIndent(),

        "PV001" to """
            Porciones: 12 personas
            Peso: 2.300 g (2,3 kg)
            Refrigerado dura: 7 días
            Calorías por porción: 420 kcal
            Calorías totales: 5.600 kcal
            
            Torta de chocolate elaborada sin ingredientes de origen animal.
            Textura húmeda y sabor intenso a cacao.
            Ideal para personas veganas o alimentación consciente.
        """.trimIndent(),

        "PV002" to """
            Porciones: 1 personas
            Peso: 90 g (0,09 kg)
            Refrigerado dura: 10 días
            Calorías por porción: 360 kcal
            
            Galletas individuales elaboradas con avena.
            Sin ingredientes de origen animal.
            Ideales como colación diaria.
        """.trimIndent(),

        "TE001" to """
            Porciones: 15 personas
            Peso: 3.000 g (3 kg)
            Refrigerado dura: 7 días
            Calorías por porción: 470 kcal
            Calorías totales: 4.600 kcal
            
            Torta diseñada para celebraciones de cumpleaños.
            Bizcocho suave con relleno cremoso y decoración personalizable.
            Ideal para compartir en grupos grandes.
        """.trimIndent(),

        "TE002" to """
            Porciones: 25 personas
            Peso: 5.000 g (5 kg)
            Refrigerado dura: 7 días
            Calorías por porción: 450 kcal
            Calorías totales: 6.000 kcal
            
            Torta de gran formato pensada para celebraciones de boda.
            Capas suaves con rellenos cremosos y presentación elegante.
            Producto premium para compartir.
        """.trimIndent()
    )

    fun getCatalog(): List<Producto> = listOf(
        Producto(
            "TC001",
            "Tortas Cuadradas",
            "Torta Cuadrada de Chocolate",
            "Deliciosa torta de chocolate con capas de ganache y un toque de avellanas.",
            25000,
            R.drawable.tc001
        ),
        Producto(
            "TC002",
            "Tortas Cuadradas",
            "Torta Cuadrada de Frutas",
            "Una mezcla de frutas frescas y crema chantilly sobre un suave bizcocho de vainilla.",
            27000,
            R.drawable.tc002
        ),
        Producto(
            "TT001",
            "Tortas Circulares",
            "Torta Circular de Vainilla",
            "Bizcocho de vainilla clásico relleno con crema pastelera y cubierto con un glaseado dulce.",
            22000,
            R.drawable.tt001
        ),
        Producto(
            "TT002",
            "Tortas Circulares",
            "Torta Circular de Manjar",
            "Torta tradicional chilena con manjar y nueces, un deleite de sabores dulces y clásicos.",
            23000,
            R.drawable.tt002
        ),
        Producto(
            "PI001",
            "Postres Individuales",
            "Mousse de Chocolate",
            "Postre individual cremoso y suave, hecho con chocolate de alta calidad, ideal para los amantes del chocolate.",
            4500,
            R.drawable.pi001
        ),
        Producto(
            "PI002",
            "Postres Individuales",
            "Tiramisú Clásico",
            "Un postre italiano individual con capas de café, mascarpone y cacao, perfecto para finalizar cualquier comida.",
            4500,
            R.drawable.pi002
        ),
        Producto(
            "PSA001",
            "Productos Sin Azúcar",
            "Torta Sin Azúcar de Naranja",
            "Torta ligera y deliciosa, endulzada naturalmente, ideal para quienes buscan opciones más saludables.",
            23000,
            R.drawable.psa001
        ),
        Producto(
            "PSA002",
            "Productos Sin Azúcar",
            "Cheesecake Sin Azúcar",
            "Suave y cremoso, este cheesecake es una opción perfecta para disfrutar sin culpa.",
            24000,
            R.drawable.psa002
        ),
        Producto(
            "PT001",
            "Pastelería Tradicional",
            "Empanada de Manzana",
            "Pastelería tradicional rellena de manzanas especiadas, perfecta para un dulce desayuno o merienda.",
            3500,
            R.drawable.pt001
        ),
        Producto(
            "PT002",
            "Pastelería Tradicional",
            "Tarta de Santiago",
            "Tradicional tarta española hecha con almendras, azúcar, y huevos, una delicia de postres clásicos.",
            4000,
            R.drawable.pt002
        ),
        Producto(
            "PG001",
            "Productos Sin Gluten",
            "Brownie Sin Gluten",
            "Rico y denso, este brownie es perfecto para quienes necesitan evitar el gluten sin sacrificar el sabor.",
            4500,
            R.drawable.pg001
        ),
        Producto(
            "PG002",
            "Productos Sin Gluten",
            "Pan Sin Gluten",
            "Suave y esponjoso, ideal para sándwiches o para acompañar cualquier comida.",
            11000,
            R.drawable.pg002
        ),
        Producto(
            "PV001",
            "Productos Veganos",
            "Torta Vegana de Chocolate",
            "Torta de chocolate húmeda y deliciosa, hecha sin productos de origen animal.",
            26000,
            R.drawable.pv001
        ),
        Producto(
            "PV002",
            "Productos Veganos",
            "Galletas Veganas de Avena",
            "Crujientes y sabrosas, excelente opción para un snack saludable y vegano.",
            3000,
            R.drawable.pv002
        ),
        Producto(
            "TE001",
            "Tortas Especiales",
            "Torta Especial de Cumpleaños",
            "Torta de queque diseñada especialmente para celebraciones de cumpleaños.",
            30000,
            R.drawable.te001
        ),
        Producto(
            "TE002",
            "Tortas Especiales",
            "Torta Especial de Boda",
            "Elegante y deliciosa, esta torta está diseñada para ser el centro de atención en cualquier boda.",
            70000,
            R.drawable.te002
        )

    )

    fun getById(id: String): Producto? =
        getCatalog().firstOrNull { it.id == id }

    fun getProductSpecifications(id: String): ProductSpecifications? = productSpecifications[id]

    fun getDetailDescription(id: String): String? = detailDescriptions[id]
}