# 🏛️ E-COMMERCE MONOLITO - Sistema Académico

## 📋 Descripción

Este proyecto es un **sistema monolítico modular** de e-commerce desarrollado con propósitos académicos para demostrar los fundamentos, ventajas y desventajas de la arquitectura monolítica.

---

## 🎯 ¿QUÉ ES UN MONOLITO?

### Definición Técnica

Un **monolito** es una aplicación que cumple con las siguientes características:

1. ✅ **Un único proceso en ejecución**
   - Toda la aplicación corre en una sola JVM
   - No hay procesos separados para diferentes funcionalidades

2. ✅ **Un único artefacto desplegable**
   - Se genera un solo archivo `.jar` o `.war`
   - El despliegue es atómico: todo o nada

3. ✅ **Stack tecnológico unificado**
   - Java 17 + Spring Boot
   - Todo el código comparte las mismas dependencias

4. ✅ **Memoria compartida**
   - Todos los módulos acceden a la misma memoria (heap)
   - Comunicación directa por llamadas a métodos (in-memory)
   - Latencia CERO entre módulos

5. ✅ **SIN comunicación HTTP interna**
   - Los módulos NO se comunican por REST entre sí
   - Todo es invocación directa de métodos Java

6. ✅ **SIN base de datos externa (para este demo)**
   - Almacenamiento en memoria usando `ConcurrentHashMap`
   - Los datos se pierden al reiniciar (volátil)

---

## 📦 TIPO DE MONOLITO: Modular

### ❌ NO es un Monolito Caótico

Este proyecto demuestra que **NO todo monolito es desorganizado**. Está estructurado en **módulos internos** con responsabilidades bien definidas:

```
com.monolito.ecommerce/
├── user/           → Gestión de usuarios (registro, login)
├── product/        → Catálogo de productos e inventario
├── cart/           → Carrito de compras
├── order/          → Procesamiento de órdenes
└── shared/         → Componentes compartidos (excepciones, DTOs)
```

Cada módulo tiene su propia arquitectura en capas:
- **Controller**: Endpoints REST
- **Service**: Lógica de negocio
- **Model**: Modelos de dominio y DTOs

---

## 🏗️ Arquitectura del Sistema

### Comunicación Entre Módulos (In-Memory)

```
┌─────────────────────────────────────────────────┐
│           PROCESO ÚNICO (JVM)                   │
│                                                 │
│  ┌──────────┐    ┌──────────┐    ┌──────────┐ │
│  │  User    │    │ Product  │    │   Cart   │ │
│  │ Service  │◄───┤ Service  │◄───┤ Service  │ │
│  └──────────┘    └──────────┘    └──────────┘ │
│       ▲               ▲               ▲        │
│       │               │               │        │
│       └───────────────┴───────────────┘        │
│                       │                        │
│                  ┌──────────┐                  │
│                  │  Order   │                  │
│                  │ Service  │                  │
│                  └──────────┘                  │
│                                                 │
│     MEMORIA COMPARTIDA (ConcurrentHashMap)     │
│  ┌──────────────────────────────────────────┐  │
│  │  Map<Long, User>                         │  │
│  │  Map<Long, Product>                      │  │
│  │  Map<Long, Order>                        │  │
│  │  Map<Long, List<CartItem>>               │  │
│  └──────────────────────────────────────────┘  │
└─────────────────────────────────────────────────┘
```

**Ventaja clave**: Latencia CERO entre módulos (no hay red de por medio)

---

## 💾 Almacenamiento en Memoria

### Implementación

Se utilizan estructuras de datos thread-safe en memoria:

```java
// Almacenamiento
ConcurrentHashMap<Long, User>
ConcurrentHashMap<Long, Product>
ConcurrentHashMap<Long, Order>
ConcurrentHashMap<Long, List<CartItem>>

// Generación de IDs
AtomicLong (auto-incremental)
```

### Características

- ✅ **Thread-safe**: `ConcurrentHashMap` permite acceso concurrente
- ✅ **Sin ORM/JPA**: No hay capa de persistencia
- ⚠️ **Volátil**: Los datos se pierden al reiniciar
- ⚠️ **No escalable**: Limitado a memoria RAM de una máquina

---

## 🔄 Transacciones Simuladas

### El Problema

Sin base de datos, NO tenemos transacciones ACID automáticas. Este proyecto **simula manualmente** el comportamiento transaccional.

### Ejemplo: Crear Orden

```java
// PSEUDO-CÓDIGO de la transacción simulada
try {
    // 1. Validar stock de todos los productos
    for (item : cartItems) {
        if (!hasStock(item)) throw error;
    }
    
    // 2. Descontar inventario
    for (item : cartItems) {
        decreaseStock(item);  // Guarda cambios para rollback
    }
    
    // 3. Crear orden
    createOrder();
    
    // 4. Limpiar carrito
    clearCart();
    
} catch (Exception e) {
    // ROLLBACK MANUAL: Revertir todos los cambios
    for (change : stockChanges) {
        increaseStock(change);
    }
    throw error;
}
```

### Notas Académicas

- ✅ **Con DB**: `@Transactional` haría esto AUTOMÁTICAMENTE
- ✅ **Rollback**: Se revertiría todo sin código extra
- ⚠️ **Aquí**: Lo hacemos manual para demostrar el concepto

---

## 🚀 Funcionalidades

### 1️⃣ Gestión de Usuarios

- Registrar usuario
- Login simple (autenticación básica)
- Listar usuarios

### 2️⃣ Catálogo de Productos

- Crear productos
- Listar productos
- Filtrar por categoría
- Actualizar stock y precios

### 3️⃣ Carrito de Compras

- Agregar productos al carrito
- Calcular total
- Eliminar items
- Vaciar carrito

### 4️⃣ Procesamiento de Órdenes

- Crear orden desde carrito
- Validar stock en tiempo real
- Descontar inventario
- Simulación de transacciones con rollback
- Historial de órdenes

---

## 📡 API REST Endpoints

### Usuarios

```http
POST   /api/users/register     # Registrar usuario
POST   /api/users/login        # Login
GET    /api/users              # Listar todos
GET    /api/users/{id}         # Obtener por ID
```

### Productos

```http
POST   /api/products                    # Crear producto
GET    /api/products                    # Listar todos
GET    /api/products/{id}               # Obtener por ID
GET    /api/products/category/{cat}     # Filtrar por categoría
PUT    /api/products/{id}/stock         # Actualizar stock
PUT    /api/products/{id}/price         # Actualizar precio
```

### Carrito

```http
POST   /api/cart/add                    # Agregar al carrito
GET    /api/cart/{userId}               # Obtener carrito
DELETE /api/cart/{userId}               # Vaciar carrito
DELETE /api/cart/{userId}/item/{id}     # Eliminar item
```

### Órdenes

```http
POST   /api/orders                      # Crear orden
GET    /api/orders/{id}                 # Obtener por ID
GET    /api/orders/user/{userId}        # Órdenes de usuario
GET    /api/orders                      # Listar todas
```

---

## 🛠️ Tecnologías

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| Java | 17 | Lenguaje base |
| Spring Boot | 4.0.3 | Framework web |
| Maven | 3.x | Gestión de dependencias |
| Spring MVC | - | REST Controllers |

**SIN**:
- ❌ Base de datos (H2, MySQL, PostgreSQL)
- ❌ JPA/Hibernate
- ❌ Microservicios
- ❌ Docker (opcional para extensión)

---

## ⚙️ Instalación y Ejecución

### Requisitos Previos

- JDK 17 o superior
- Maven 3.6+

### Compilar y Ejecutar

```bash
# Compilar el proyecto
mvn clean install

# Ejecutar la aplicación
mvn spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

---

## 📝 Ejemplo de Flujo Completo

### 1. Registrar Usuario

```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "juan",
    "email": "juan@example.com",
    "password": "1234",
    "fullName": "Juan Pérez"
  }'
```

### 2. Crear Productos

```bash
# Producto 1
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop Dell",
    "description": "Laptop profesional",
    "price": 1200.00,
    "stock": 10,
    "category": "Electrónica"
  }'

# Producto 2
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Mouse Logitech",
    "description": "Mouse inalámbrico",
    "price": 25.00,
    "stock": 50,
    "category": "Accesorios"
  }'
```

### 3. Agregar al Carrito

```bash
curl -X POST http://localhost:8080/api/cart/add \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 0,
    "productId": 0,
    "quantity": 1
  }'

curl -X POST http://localhost:8080/api/cart/add \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 0,
    "productId": 1,
    "quantity": 2
  }'
```

### 4. Ver Carrito

```bash
curl http://localhost:8080/api/cart/0
```

### 5. Crear Orden

```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 0
  }'
```

---

## ✅ VENTAJAS del Monolito

### 1. **Simplicidad**
- Un solo proyecto, un solo repositorio
- Fácil de entender y navegar
- Curva de aprendizaje baja

### 2. **Latencia Cero Entre Módulos**
- Comunicación directa en memoria
- No hay overhead de red
- No hay serialización/deserialización

### 3. **Despliegue Sencillo**
- Un solo artefacto `.jar`
- No necesitas orquestación (Kubernetes, Docker Swarm)
- Ideal para desarrollo y testing

### 4. **Fácil Debugging**
- Todo el código está en un solo stacktrace
- Puedes hacer debug de principio a fin
- No hay "cajas negras" distribuidas

### 5. **Transacciones Reales (con DB)**
- Con base de datos: ACID garantizado
- Rollbacks automáticos
- Consistencia fuerte

### 6. **Testing E2E Simplificado**
- Puedes probar todo el flujo en un solo proceso
- No necesitas levantar múltiples servicios

---

## ⚠️ DESVENTAJAS del Monolito

### 1. **Escalabilidad Limitada**
- Solo escala verticalmente (más CPU/RAM a UNA máquina)
- No puedes escalar módulos independientemente
- Por ejemplo: Si "Order" necesita más recursos, tienes que escalar TODO

### 2. **Sin Persistencia (en este demo)**
- Los datos se pierden al reiniciar
- No apto para producción sin DB

### 3. **Single Point of Failure**
- Si el proceso falla, TODO cae
- No hay redundancia

### 4. **Acoplamiento Temporal**
- Todos los módulos se despliegan juntos
- Un bug en "Cart" requiere redesplegar "User", "Product", "Order"

### 5. **Límite de Tecnologías**
- Todo debe usar el mismo stack (Java/Spring)
- No puedes usar Node.js para "Cart" y Python para "Order"

---

## 🚫 ¿Qué NO es este Monolito?

### ❌ Monolito Distribuido (Anti-patrón)

Un **monolito distribuido** es el PEOR escenario:

```
┌─────────┐  HTTP   ┌─────────┐  HTTP   ┌─────────┐
│  User   ├────────►│ Product ├────────►│  Order  │
│ Service │         │ Service │         │ Service │
└─────────┘         └─────────┘         └─────────┘
     │                    │                    │
     ▼                    ▼                    ▼
 Shared DB          Shared DB          Shared DB
```

**Problemas**:
- ❌ Tiene las DESVENTAJAS de microservicios (complejidad, latencia)
- ❌ NO tiene las VENTAJAS de microservicios (escalado independiente)
- ❌ Acoplamiento por base de datos compartida
- ❌ Acoplamiento por HTTP (si User cae, Product no funciona)

**Este proyecto NO es esto**. Todo vive en el mismo proceso.

---

## 🔄 ¿Cuándo Migrar a Microservicios?

### Señales de que necesitas microservicios:

1. ✅ Tu equipo crece (>50 desarrolladores)
2. ✅ Necesitas escalar módulos independientemente
3. ✅ Quieres usar diferentes tecnologías por módulo
4. ✅ Tienes recursos para gestionar complejidad distribuida
5. ✅ Tu tráfico justifica la infraestructura extra

### Señales de que el monolito es suficiente:

1. ✅ Equipo pequeño (2-10 personas)
2. ✅ MVP o proyecto académico
3. ✅ Tráfico moderado
4. ✅ Simplicidad > Escalabilidad
5. ✅ Budget limitado para infraestructura

---

## 🎓 Evolución Posible

### De Monolito a Microservicios

Si este proyecto creciera, podrías extraer módulos:

```
MONOLITO ACTUAL:
┌──────────────────────────────┐
│  User + Product + Cart + Order │
└──────────────────────────────┘

MICROSERVICIOS FUTUROS:
┌──────┐   ┌─────────┐   ┌──────┐   ┌───────┐
│ User │   │ Product │   │ Cart │   │ Order │
└──────┘   └─────────┘   └──────┘   └───────┘
```

**Cambios necesarios**:
1. Cada servicio tendría su propia base de datos
2. Comunicación por HTTP/gRPC en vez de llamadas directas
3. Gestión de transacciones distribuidas (Saga pattern)
4. Service Discovery (Eureka, Consul)
5. API Gateway (Spring Cloud Gateway)

---

## 📚 Conceptos Clave Aprendidos

- ✅ Qué es un monolito y qué NO lo es
- ✅ Diferencia entre monolito modular y caótico
- ✅ Comunicación en memoria vs HTTP
- ✅ Simulación de transacciones sin base de datos
- ✅ Ventajas/desventajas de arquitecturas monolíticas
- ✅ Cuándo conviene monolito vs microservicios
- ✅ Thread-safety con `ConcurrentHashMap`
- ✅ Arquitectura en capas (Controller, Service, Model)

---

## 📂 Estructura del Proyecto

```
src/main/java/com/monolito/ecommerce/
│
├── EcommerceMonolitoApplication.java    # Punto de entrada
│
├── user/
│   ├── controller/
│   │   └── UserController.java
│   ├── service/
│   │   └── UserService.java
│   └── model/
│       ├── User.java
│       ├── RegisterRequest.java
│       ├── LoginRequest.java
│       └── UserResponse.java
│
├── product/
│   ├── controller/
│   │   └── ProductController.java
│   ├── service/
│   │   └── ProductService.java
│   └── model/
│       ├── Product.java
│       └── ProductRequest.java
│
├── cart/
│   ├── controller/
│   │   └── CartController.java
│   ├── service/
│   │   └── CartService.java
│   └── model/
│       ├── CartItem.java
│       ├── CartResponse.java
│       └── AddToCartRequest.java
│
├── order/
│   ├── controller/
│   │   └── OrderController.java
│   ├── service/
│   │   └── OrderService.java
│   └── model/
│       ├── Order.java
│       ├── OrderItem.java
│       ├── OrderStatus.java
│       └── CreateOrderRequest.java
│
└── shared/
    ├── exception/
    │   ├── ResourceNotFoundException.java
    │   ├── BusinessException.java
    │   └── GlobalExceptionHandler.java
    └── dto/
        ├── ApiResponse.java
        └── ErrorResponse.java
```

---

## 👨‍💻 Autor

Sistema desarrollado con propósitos académicos para demostrar fundamentos de arquitectura monolítica modular.

---

## 📄 Licencia

Proyecto académico - Uso educativo

---

## 🎯 Conclusión

Este proyecto demuestra que:

1. ✅ Los **monolitos bien estructurados** son válidos y efectivos
2. ✅ NO toda aplicación necesita ser microservicios
3. ✅ La arquitectura debe elegirse según necesidades REALES
4. ✅ Los monolitos son ideales para:
   - Proyectos pequeños/medianos
   - Equipos reducidos
   - MVPs
   - Aprendizaje

**Recuerda**: No hay arquitectura perfecta, solo arquitectura apropiada para el contexto. 🎯
