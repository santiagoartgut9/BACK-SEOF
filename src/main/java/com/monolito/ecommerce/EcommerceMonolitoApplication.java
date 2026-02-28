package com.monolito.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Aplicación Principal del Monolito E-Commerce
 * 
 * CARACTERÍSTICAS DE ESTE MONOLITO:
 * - Un único proceso en ejecución (Single Process)
 * - Un único artefacto .jar desplegable
 * - Stack tecnológico unificado: Java 17 + Spring Boot
 * - Memoria compartida entre todos los módulos
 * - Despliegue atómico: todo o nada
 * - SIN comunicación HTTP interna entre módulos
 * - SIN base de datos: almacenamiento en memoria con Map/List
 * 
 * ARQUITECTURA MODULAR:
 * - Este NO es un monolito caótico
 * - Está organizado en módulos internos coherentes
 * - Cada módulo tiene responsabilidades bien definidas
 * - La comunicación es por llamadas directas a métodos (in-memory)
 * 
 * MÓDULOS:
 * - user: Gestión de usuarios (registro, login)
 * - product: Catálogo de productos e inventario
 * - cart: Carrito de compras por usuario
 * - order: Procesamiento de órdenes
 * - shared: Componentes compartidos (excepciones, DTOs, utilidades)
 * 
 * @author Sistema Monolítico E-Commerce
 * @version 1.0.0
 */
@SpringBootApplication
public class EcommerceMonolitoApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceMonolitoApplication.class, args);
		System.out.println("\n" +
				"╔════════════════════════════════════════════════════════════╗\n" +
				"║   E-COMMERCE MONOLITO - Sistema Académico                 ║\n" +
				"║   Arquitectura: Monolito Modular                          ║\n" +
				"║   Almacenamiento: En Memoria (ConcurrentHashMap)          ║\n" +
				"║   Puerto: 8080                                            ║\n" +
				"╚════════════════════════════════════════════════════════════╝\n");
	}
}
