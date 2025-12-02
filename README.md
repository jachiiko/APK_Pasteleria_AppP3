# Pastelería App — Evaluación 2 (Duoc UC)

Aplicación móvil en **Jetpack Compose** para el ramo *Aplicaciones Móviles*. Permite registrar/validar usuarios, explorar un catálogo de pastelería con filtros, administrar un carrito de compras y visualizar un *checkout* de prueba. Incluye un pequeño **Back Office visual** que reutiliza el mismo catálogo en memoria.

## Integrantes
- Diego Herrera
- Joaquín Araya

## Sección
DESARROLLO DE APLICACIONES MOVILES_007D

## Requisitos
- **Android Studio Electric Eel** o superior.
- **JDK 11** o posterior.
- Emulador o dispositivo físico con **Android API 33+**.

## Ejecución rápida
1. Clona el repositorio y ábrelo en Android Studio.
2. Sincroniza Gradle (usa el *wrapper* incluido).
3. Ejecuta la app en un emulador/dispositivo. La pantalla inicial es **login**.

## Flujo y pantallas principales
- **Login (`LoginScreen`)**: valida credenciales contra un `FakeDatabase` en memoria. Guarda el correo en `AuthViewModel.usuarioActual` tras un inicio exitoso.
- **Registro (`RegisterScreen`)**: captura datos personales, región y comuna (desde API o *fallback* local), y restringe el correo a dominios `@duoc.cl` o `@admin.cl`. El `AuthViewModel` inserta el usuario en el `FakeDatabase` y muestra mensajes de estado.
- **Catálogo (`HomeScreen`)**: tras logear navega a `shop/home/{email}`. Muestra las tarjetas de producto con filtros por categoría (chips) y rango de precio (slider). Un chip abre el **carrito** mostrando cantidad y total acumulado. Cada producto permite agregar al carrito y abrir el detalle.
- **Detalle de producto (`ProductDetailScreen`)**: recibe el `productId`, muestra descripción extendida desde `ProductRepository` y permite sumar unidades al carrito usando el mismo `CatalogViewModel` compartido.
- **Carrito (`CartScreen`)**: lista las líneas agregadas, permite incrementar/decrementar o eliminar, vaciar el carrito y navegar al *checkout* de resultados. Calcula subtotal, total y cantidad con `CatalogViewModel`.
- **Checkout de demo (`CheckoutResultTabsScreen`)**: genera un `OrderSummary` con los ítems del carrito, número de pedido y fecha formateada para CL. Presenta pestañas para “Pago” y “Envío”, con mensajes estáticos.
- **Back Office visual (`BackOfficeListScreen` / `AddProductScreen`)**: muestra el mismo catálogo en tarjetas con acciones “Editar/Eliminar” deshabilitadas y un botón “Agregar” que abre un formulario sin lógica de guardado. Es solo demostrativo.

## Partes de la app y qué permiten
- **Acceso (Login/Registro)**: crea cuentas en memoria, valida dominios permitidos y mantiene el correo autenticado para el resto del flujo.
- **Catálogo**: filtra por categoría o precio, muestra tarjetas con imagen, descripción breve y botón “Agregar” para enviar al carrito.
- **Detalle de producto**: amplía la descripción, reutiliza la selección de cantidades y suma al mismo carrito compartido.
- **Carrito**: lista productos agregados, permite sumar/restar unidades, eliminar líneas, vaciar todo y ver subtotales/total.
- **Checkout (demo)**: presenta el resumen del pedido generado en el carrito y pestañas estáticas de pago/envío a modo de mock.
- **Back Office**: reutiliza el catálogo en modo lectura con un formulario de “agregar producto” sin persistencia.

## Navegación y estado
- El `NavHost` define una ruta raíz `login` y un grafo anidado `shop` para las pantallas autenticadas. `MainActivity` crea un solo `AuthViewModel` y un `RegionViewModel` globales.
- `HomeScreen`, `ProductDetailScreen`, `CartScreen` y `CheckoutResultTabsScreen` comparten la **misma instancia** de `CatalogViewModel` obtenida desde el `NavBackStackEntry` de `shop`, manteniendo el carrito y filtros vivos mientras se navega dentro del grafo.

## Datos y repositorios
- **Catálogo**: `ProductRepository` expone `getCatalog()` (productos con imágenes en `res/drawable`), `getById()` y descripciones extendidas en `getDetailDescription()`.
- **Usuarios**: `FakeDatabase` guarda registros en memoria y expone `registrar()` / `login()`.
- **Regiones/Comunas**: `ApiClient` configura Retrofit contra `http://10.0.2.2:8080/` y `RegionComunaRepository` define `GET /regions`. `RegionViewModel` consume la API y, si falla, usa un *fallback* local con tres regiones chilenas.

## Theming y componentes reutilizables
- Paleta y estilos en `ui/theme` más funciones auxiliares (`pastelButtonColors`, `pastelOutlinedTextFieldColors`, etc.).
- Componentes clave en `views/components/` como `ProductCard` (tarjeta del catálogo con botón “Agregar”), `InfoRow`, `SectionTitle`, entre otros, para mantener consistencia visual.

## Consideraciones
- Todo el almacenamiento (usuarios, carrito, catálogo) es **en memoria**; no hay persistencia local ni remota.
- El Back Office y el formulario de agregar producto son mockups sin CRUD real.
- La API de regiones se asume disponible en local (`10.0.2.2:8080`); si no responde, la app sigue operativa gracias al *fallback*.

## Estructura rápida del módulo `app`
- `MainActivity.kt`: arranque de Compose y grafo de navegación.
- `model/`: modelos `Usuario`, `Producto`, `Region` y el `FakeDatabase` para autenticación.
- `repository/`: `ProductRepository` (catálogo), `ApiClient` + `RegionComunaRepository` (regiones/comunas).
- `viewmodel/`: `AuthViewModel`, `CatalogViewModel` (filtros, carrito, resumen de pedido) y `RegionViewModel` (carga de regiones).
- `views/`: pantallas Compose; incluye subcarpeta `backoffice/` y `components/` con UI reutilizable.