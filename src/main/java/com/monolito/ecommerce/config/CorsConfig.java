package com.monolito.ecommerce.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración CORS para permitir solicitudes desde el frontend.
 * 
 * Permite que aplicaciones en otros dominios/puertos accedan a la API.
 * Incluye:
 * - Desarrollo local: localhost:3000, localhost:3001, localhost:5173
 * - Projjducción: S3 bucket frontend
 */


@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(
                    "http://localhost:3000", 
                    "http://localhost:3001", 
                    "http://localhost:5173",
                    "http://local-ecommerce-frontend-414813662494.s3-website-us-east-1.amazonaws.com"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
