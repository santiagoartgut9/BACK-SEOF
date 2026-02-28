# 🧪 Pruebas del E-Commerce Monolito

Este archivo contiene ejemplos completos para probar todos los endpoints del sistema.

---

## 📌 FLUJO COMPLETO DE PRUEBAS

### PASO 1: Registrar Usuarios

```bash
# Usuario 1 - Juan
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "juan",
    "email": "juan@example.com",
    "password": "1234",
    "fullName": "Juan Pérez"
  }'

# Usuario 2 - María
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "maria",
    "email": "maria@example.com",
    "password": "5678",
    "fullName": "María González"
  }'
```

**Respuesta esperada:**
```json
{
  "success": true,
  "message": "Usuario registrado exitosamente",
  "data": {
    "id": 0,
    "username": "juan",
    "email": "juan@example.com",
    "fullName": "Juan Pérez"
  }
}
```

---

### PASO 2: Login de Usuario

```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "juan",
    "password": "1234"
  }'
```

---

### PASO 3: Listar Usuarios

```bash
curl http://localhost:8080/api/users
```

---

### PASO 4: Crear Productos

```bash
# Laptop
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop Dell Inspiron 15",
    "description": "Laptop profesional Intel i7, 16GB RAM, 512GB SSD",
    "price": 1200.00,
    "stock": 10,
    "category": "Electrónica"
  }'

# Mouse
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Mouse Logitech MX Master 3",
    "description": "Mouse inalámbrico ergonómico",
    "price": 99.99,
    "stock": 50,
    "category": "Accesorios"
  }'

# Teclado
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Teclado Mecánico Keychron K2",
    "description": "Teclado mecánico compacto RGB",
    "price": 89.99,
    "stock": 30,
    "category": "Accesorios"
  }'

# Monitor
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Monitor LG UltraWide 34\"",
    "description": "Monitor curvo 3440x1440 IPS",
    "price": 599.99,
    "stock": 15,
    "category": "Electrónica"
  }'

# Audífonos
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Audífonos Sony WH-1000XM4",
    "description": "Audífonos con cancelación de ruido",
    "price": 349.99,
    "stock": 25,
    "category": "Audio"
  }'
```

---

### PASO 5: Listar Productos

```bash
# Todos los productos
curl http://localhost:8080/api/products

# Productos de una categoría
curl http://localhost:8080/api/products/category/Electrónica

# Producto específico por ID
curl http://localhost:8080/api/products/0
```

---

### PASO 6: Agregar Productos al Carrito

```bash
# Usuario 0: Agregar Laptop (ID 0)
curl -X POST http://localhost:8080/api/cart/add \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 0,
    "productId": 0,
    "quantity": 1
  }'

# Usuario 0: Agregar Mouse (ID 1)
curl -X POST http://localhost:8080/api/cart/add \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 0,
    "productId": 1,
    "quantity": 2
  }'

# Usuario 0: Agregar Monitor (ID 3)
curl -X POST http://localhost:8080/api/cart/add \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 0,
    "productId": 3,
    "quantity": 1
  }'
```

---

### PASO 7: Ver Carrito

```bash
curl http://localhost:8080/api/cart/0
```

**Respuesta esperada:**
```json
{
  "success": true,
  "message": "Operación exitosa",
  "data": {
    "userId": 0,
    "items": [
      {
        "productId": 0,
        "productName": "Laptop Dell Inspiron 15",
        "price": 1200.00,
        "quantity": 1,
        "subtotal": 1200.00
      },
      {
        "productId": 1,
        "productName": "Mouse Logitech MX Master 3",
        "price": 99.99,
        "quantity": 2,
        "subtotal": 199.98
      }
    ],
    "totalItems": 3,
    "total": 1399.98
  }
}
```

---

### PASO 8: Crear Orden

```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 0
  }'
```

**Esto hará:**
1. ✅ Validar stock
2. ✅ Descontar inventario
3. ✅ Crear la orden
4. ✅ Vaciar el carrito

---

### PASO 9: Ver Órdenes

```bash
# Órdenes de un usuario
curl http://localhost:8080/api/orders/user/0

# Detalle de una orden específica
curl http://localhost:8080/api/orders/0

# Todas las órdenes del sistema
curl http://localhost:8080/api/orders
```

---

### PASO 10: Verificar Stock Actualizado

```bash
# Ver que el stock de los productos disminuyó
curl http://localhost:8080/api/products/0
curl http://localhost:8080/api/products/1
```

---

## 🧪 PRUEBAS DE VALIDACIÓN

### ❌ Error: Stock Insuficiente

```bash
# Intentar agregar más productos de los disponibles
curl -X POST http://localhost:8080/api/cart/add \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 0,
    "productId": 0,
    "quantity": 999
  }'
```

**Respuesta esperada:**
```json
{
  "status": 400,
  "message": "Stock insuficiente para Laptop Dell Inspiron 15. Disponible: 10",
  "timestamp": "2026-02-27T..."
}
```

---

### ❌ Error: Usuario No Encontrado

```bash
curl http://localhost:8080/api/users/999
```

**Respuesta esperada:**
```json
{
  "status": 404,
  "message": "Usuario con ID 999 no encontrado",
  "timestamp": "2026-02-27T..."
}
```

---

### ❌ Error: Carrito Vacío

```bash
# Intentar crear orden sin productos en el carrito
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 0
  }'
```

**Respuesta esperada:**
```json
{
  "status": 400,
  "message": "El carrito está vacío",
  "timestamp": "2026-02-27T..."
}
```

---

### ❌ Error: Credenciales Inválidas

```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "juan",
    "password": "wrong_password"
  }'
```

---

## 🎯 PRUEBA DE TRANSACCIÓN CON ROLLBACK

### Escenario: Simular Falla en Creación de Orden

```bash
# 1. Crear productos con stock bajo
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Producto Stock Bajo",
    "description": "Solo quedan 2 unidades",
    "price": 50.00,
    "stock": 2,
    "category": "Test"
  }'

# 2. Usuario 1: Agregar al carrito 1 unidad
curl -X POST http://localhost:8080/api/cart/add \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "productId": 5,
    "quantity": 1
  }'

# 3. Usuario 0: Intentar agregar 2 unidades
curl -X POST http://localhost:8080/api/cart/add \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 0,
    "productId": 5,
    "quantity": 2
  }'

# 4. Usuario 1 hace orden (funciona, queda 1 en stock)
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{"userId": 1}'

# 5. Usuario 0 intenta orden (FALLA - stock insuficiente)
# Este debe hacer ROLLBACK si hubiera descuentos previos
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{"userId": 0}'
```

---

## 🔧 OPERACIONES ADICIONALES

### Actualizar Stock de Producto

```bash
curl -X PUT "http://localhost:8080/api/products/0/stock?stock=100"
```

---

### Actualizar Precio de Producto

```bash
curl -X PUT "http://localhost:8080/api/products/0/price?price=999.99"
```

---

### Eliminar Item del Carrito

```bash
curl -X DELETE http://localhost:8080/api/cart/0/item/1
```

---

### Vaciar Carrito Completo

```bash
curl -X DELETE http://localhost:8080/api/cart/0
```

---

## 📊 MÉTRICAS DEL SISTEMA

### Ventajas Observables

1. **Latencia CERO** entre módulos (todo en memoria)
2. **Sin serialización** (objetos Java directos)
3. **Thread-safe** (ConcurrentHashMap)
4. **Rollback simulado** (manual pero efectivo)

---

## 🎓 APRENDIZAJES PRÁCTICOS

Al ejecutar estas pruebas, observarás:

- ✅ Cómo los módulos se comunican internamente sin HTTP
- ✅ Cómo el stock se actualiza en tiempo real
- ✅ Cómo funciona el rollback manual en transacciones
- ✅ Validaciones de negocio (stock, usuarios, carrito vacío)
- ✅ Manejo de errores centralizado

---

**Nota**: Los IDs empiezan en 0 porque usamos `AtomicLong` iniciado en 0.
