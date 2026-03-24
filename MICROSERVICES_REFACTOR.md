# Refactor: Monolito a Microservicios (versión mínima)

## Arquitectura actual

Se separó el monolito en 3 procesos:

1. **order-service** (`BACK-SEOF`) - Puerto **8081**
   - Responsabilidades: `cart` + `orders`
   - Endpoints: `/api/cart/**`, `/api/orders/**`
   - Orquesta llamadas HTTP a los otros dos servicios.

2. **auth-service** (`auth-service`) - Puerto **8082**
   - Responsabilidades: `users` (registro/login/listado)
   - Endpoints: `/api/users/**`

3. **catalog-service** (`catalog-service`) - Puerto **8083**
   - Responsabilidades: `products` + inventario
   - Endpoints: `/api/products/**`

## Cambios clave

- En `BACK-SEOF` se eliminaron los controladores locales de usuario y producto.
- `CartService` y `OrderService` ahora usan clientes HTTP:
  - `AuthClient` para validar usuarios.
  - `CatalogClient` para consultar y actualizar stock.
- Frontend (`front_seof`) ya consume múltiples base URLs:
  - Usuarios -> `http://localhost:8082/api`
  - Productos -> `http://localhost:8083/api`
  - Carrito/órdenes -> `http://localhost:8081/api`

## Cómo levantar todo

### Terminal 1 - Auth

```powershell
cd auth-service
mvn spring-boot:run
```

### Terminal 2 - Catalog

```powershell
cd catalog-service
mvn spring-boot:run
```

### Terminal 3 - Order

```powershell
cd BACK-SEOF
.\mvnw.cmd spring-boot:run
```

### Terminal 4 - Frontend

```powershell
cd front_seof
npm install
npm run dev
```

## Flujo esperado

1. Crear usuario (`auth-service`).
2. Crear productos (`catalog-service`).
3. Agregar al carrito (`order-service` valida usuario/producto via HTTP).
4. Crear orden (`order-service` valida stock y descuenta en `catalog-service`).

## Notas importantes

- Sigue siendo almacenamiento **en memoria**, así que los datos se pierden al reiniciar cada servicio.
- No hay API Gateway aún; el frontend llama directo a cada servicio.
- La creación de orden sigue con rollback manual de stock cuando hay error.
