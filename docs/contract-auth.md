# Contrato Auth API ↔ App Móvil (v1)

Este documento define el contrato de comunicación entre la App Móvil y la API
para los procesos de registro y login de usuarios.

---

## 1) Modelos

### Region
```json
{
  "id": "string",
  "nombre": "string",
  "comunas": ["string"]
}
```

## 2) Registro de usuario

### POST /auth/register
Content-Type: application/json

Accept: application/json

#### RegisterRequest
```json
{
  "nombre": "string",
  "apellido": "string",
  "rut": "string",
  "direccion": "string",
  "region": {
    "id": "string",
    "nombre": "string",
    "comunas": ["string"]
  },
  "comuna": "string",
  "email": "string",
  "password": "string"
}
```
#### RegisterResponse (UserResponse)
Regla: la API nunca debe retornar el campo password.
```json
{
  "id": "string",
  "nombre": "string",
  "apellido": "string",
  "rut": "string",
  "direccion": "string",
  "region": {
    "id": "string",
    "nombre": "string",
    "comunas": ["string"]
  },
  "comuna": "string",
  "email": "string",
  "role": "ROLE_USER"
}
```
Errores esperados

400 Bad Request: datos faltantes o inválidos.

404 Not Found: región inválida o comuna no pertenece a la región.

409 Conflict: el email ya se encuentra registrado.

## 3) Login

### POST /auth/login

Content-Type: application/json

Accept: application/json

#### LoginRequest
```json
{
  "email": "string",
  "password": "string"
}
```
#### LoginResponse
```json
{
  "token": "string"
}
```
Errores esperados

400 Bad Request: datos faltantes.

401 Unauthorized: credenciales inválidas.

## 4) Regiones (para cargar combos en el móvil)

### GET /regions

#### Response 200 OK
```json
[
  {
    "id": "string",
    "nombre": "string",
    "comunas": ["string"]
  }
]
```

## 5) Reglas de negocio

La API valida que comuna esté incluida dentro de region.comunas.

La API valida que region.id exista en la colección regions y usa ese registro como fuente “oficial”.

El campo role es definido exclusivamente por el servidor:

Emails que terminan en @admin.com o @duocuc.cl → ROLE_ADMIN

Cualquier otro dominio → ROLE_USER

La API nunca debe exponer el campo password en ninguna respuesta.