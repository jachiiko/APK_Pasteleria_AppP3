# DiegoHerrera22AppMoviles007D_EV2_DHerrera_JAraya

Este proyecto corresponde a la **Evaluación 2** de la asignatura de Aplicaciones Móviles.  Es una app de pastelería construida con **Jetpack Compose**, que permite a un usuario registrarse, iniciar sesión y navegar por un catálogo de productos de repostería.  También incluye una pequeña gestión visual de productos (Back Office) y una pantalla de carrito, todo reutilizando la misma lista de productos en memoria.

## Integrantes
Diego Herrera
Joaquin Araya

## Sección
DESARROLLO DE APLICACIONES MOVILES_007D

## Requisitos

- **Android Studio Electric Eel** o superior.
- JDK 11 o posterior.
- Emulador o dispositivo físico con Android API 33 o superior.

## Ejecución

1. Clona o descarga este repositorio.
2. Abre el proyecto en Android Studio.
3. Ejecuta el proyecto en un emulador o dispositivo físico.
4. La app inicia en la pantalla de **login**.  Si no tienes cuenta puedes registrarte.

## Estructura principal del código

- `MainActivity.kt`: configura la navegación de la aplicación.  Aquí se define un grafo de navegación anidado con ruta `shop` que agrupa las pantallas autenticadas.
- `views/`: contiene las composables de cada pantalla.  Se ha añadido una subcarpeta `backoffice/` con las pantallas visuales de administración.
- `viewmodel/`: `CatalogViewModel` mantiene la lista de productos y el estado del carrito.  Se reutiliza en todas las pantallas a través del `NavBackStackEntry` de la ruta `shop`.
- `repository/`: carga el catálogo de productos desde un `FakeDatabase` (JSON embebido) y ofrece métodos de acceso.

## Navegación

La navegación utiliza un grafo anidado para agrupar las pantallas del catálogo bajo la ruta `shop`.  De este modo, los `ViewModel` compartidos (como el carrito) permanecen activos en todas las vistas internas.

```kotlin
NavHost(navController, startDestination = "login") {
    composable("login") { /* pantalla de login */ }
    composable("register") { /* pantalla de registro */ }
    navigation(startDestination = "home/{email}", route = "shop") {
        composable("home/{email}") { /* catálogo */ }
        composable("product/{id}") { /* detalle de producto */ }
        composable("cart") { /* carrito */ }
        composable("backoffice") { /* lista back office */ }
        composable("backoffice/add") { /* formulario para agregar */ }
    }
}
```

Al iniciar sesión correctamente, se navega a `shop/home/{email}`.  Dentro del catálogo, al pulsar sobre un producto se abre su detalle (`product/{id}`), y desde el chip del carrito se accede a la pantalla `cart`.  El botón “Back Office” en la pantalla de catálogo lleva a `backoffice` y desde ahí al formulario de agregar (`backoffice/add`).

## Back Office

La aplicación incorpora un panel de administración accesible tras iniciar sesión.  

- **Lista de productos** (`BackOfficeListScreen`): reutiliza la lista del catálogo y muestra cada producto en una tarjeta con botones “Editar” y “Eliminar” deshabilitados.  Incluye un botón “Agregar” que navega al formulario.
- **Agregar producto** (`AddProductScreen`): contiene campos para nombre, precio, categoría, descripción y URL de imagen.  El botón “Guardar” está deshabilitado porque no hay lógica implementada.

## Carrito

Desde cualquier pantalla dentro de `shop` puedes abrir el carrito.  El contenido del carrito está en memoria y se conserva mientras permanezcas en la app.  Actualmente solo lista los elementos agregados y muestra el total en pesos chilenos.

## Consideraciones

- Todas las pantallas de administración y carrito son **puramente visuales**; no realizan operaciones de persistencia.
- El catálogo proviene de un `FakeDatabase` en `ProductRepository`, por lo que para agregar productos reales habría que implementar almacenamiento.
- Si deseas extender la app, se recomienda seguir el patrón actual de reutilizar `ViewModel` a través de `NavBackStackEntry` para compartir estados entre pantallas.