# 🎯 GUÍA RÁPIDA DE INICIO

## ✅ Compilado Exitosamente

El proyecto se ha compilado y empaquetado correctamente. El artefacto generado es:

```
target/ecommerce-monolito-1.0.0.jar
```

---

## 🚀 Cómo Ejecutar

### Opción 1: Con Maven

```bash
mvn spring-boot:run
```

### Opción 2: Con el JAR generado

```bash
java -jar target/ecommerce-monolito-1.0.0.jar
```

La aplicación estará disponible en: **http://localhost:8080**

---

## 📋 Verificar que Funciona

Una vez iniciada la aplicación, deberías ver en la consola:

```
╔════════════════════════════════════════════════════════════╗
║   E-COMMERCE MONOLITO - Sistema Académico                 ║
║   Arquitectura: Monolito Modular                          ║
║   Almacenamiento: En Memoria (ConcurrentHashMap)          ║
║   Puerto: 8080                                            ║
╚════════════════════════════════════════════════════════════╝
```

---

## 🧪 Primera Prueba

```bash
# Crear un usuario
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "test",
    "email": "test@example.com",
    "password": "1234",
    "fullName": "Usuario de Prueba"
  }'

# Listar usuarios
curl http://localhost:8080/api/users
```

---

## 📂 Archivos Importantes

1. **README.md** - Documentación completa del proyecto
2. **TESTING.md** - Guía de pruebas con ejemplos curl
3. **api-tests.http** - Archivo para probar en VSCode/IntelliJ
4. **pom.xml** - Configuración Maven
5. **application.properties** - Configuración de Spring Boot

---

## 🏗️ Estructura del Código

```
src/main/java/com/monolito/ecommerce/
├── EcommerceMonolitoApplication.java  ← Clase principal
├── user/                               ← Módulo de usuarios
│   ├── controller/
│   ├── service/
│   └── model/
├── product/                            ← Módulo de productos
│   ├── controller/
│   ├── service/
│   └── model/
├── cart/                               ← Módulo de carrito
│   ├── controller/
│   ├── service/
│   └── model/
├── order/                              ← Módulo de órdenes
│   ├── controller/
│   ├── service/
│   └── model/
└── shared/                             ← Componentes compartidos
    ├── exception/
    └── dto/
```

---

## 🎓 Conceptos Demostrados

✅ **Monolito Modular**: No es código espagueti, está bien organizado  
✅ **Comunicación In-Memory**: Latencia cero entre módulos  
✅ **Thread-Safety**: ConcurrentHashMap + synchronized  
✅ **Transacciones Simuladas**: Rollback manual sin base de datos  
✅ **Arquitectura en Capas**: Controller → Service → Model  
✅ **REST API**: Endpoints bien definidos para frontend  
✅ **Manejo de Errores**: GlobalExceptionHandler centralizado  

---

## 🔧 Troubleshooting

### El puerto 8080 está ocupado

Edita `application.properties`:
```properties
server.port=8081
```

### No tienes Java 17

Verifica tu versión:
```bash
java -version
```

Debe ser 17 o superior. Descarga desde: https://adoptium.net/

---

## 📞 Endpoints Principales

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | /api/users/register | Registrar usuario |
| POST | /api/products | Crear producto |
| POST | /api/cart/add | Agregar al carrito |
| POST | /api/orders | Crear orden |
| GET | /api/users | Listar usuarios |
| GET | /api/products | Listar productos |
| GET | /api/cart/{userId} | Ver carrito |
| GET | /api/orders/user/{userId} | Ver órdenes |

---

## 🎯 Próximos Pasos

1. ✅ Lee el **README.md** completo
2. ✅ Ejecuta la aplicación con `mvn spring-boot:run`
3. ✅ Prueba los endpoints siguiendo **TESTING.md**
4. ✅ Inspecciona el código para entender la arquitectura
5. ✅ Experimenta modificando productos, usuarios, etc.

---

## 💡 Para el Frontend

Este backend está listo para:

- ✅ Comunicarse con cualquier frontend (React, Vue, Angular)
- ✅ Soporta CORS (puedes habilitarlo si es necesario)
- ✅ Respuestas JSON estandarizadas
- ✅ Manejo de errores consistente

Para habilitar CORS, agrega esta clase:

```java
@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                    .allowedOrigins("http://localhost:3000")
                    .allowedMethods("GET", "POST", "PUT", "DELETE");
            }
        };
    }
}
```

---

**¡Proyecto listo para usar! 🎉**
