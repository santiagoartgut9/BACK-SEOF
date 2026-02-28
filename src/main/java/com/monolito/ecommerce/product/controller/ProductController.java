package com.monolito.ecommerce.product.controller;

import com.monolito.ecommerce.product.model.Product;
import com.monolito.ecommerce.product.model.ProductRequest;
import com.monolito.ecommerce.product.service.ProductService;
import com.monolito.ecommerce.shared.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controlador REST de Productos
 * 
 * ENDPOINTS:
 * POST   /api/products              - Crear producto
 * GET    /api/products              - Listar todos los productos
 * GET    /api/products/{id}         - Obtener producto por ID
 * GET    /api/products/category/{category} - Filtrar por categoría
 * PUT    /api/products/{id}/stock   - Actualizar stock
 * PUT    /api/products/{id}/price   - Actualizar precio
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Crear un nuevo producto
     * POST /api/products
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Product>> createProduct(@RequestBody ProductRequest request) {
        Product product = productService.createProduct(
            request.getName(),
            request.getDescription(),
            request.getPrice(),
            request.getStock(),
            request.getCategory()
        );

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success("Producto creado exitosamente", product));
    }

    /**
     * Listar todos los productos
     * GET /api/products
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    /**
     * Obtener producto por ID
     * GET /api/products/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success(product));
    }

    /**
     * Filtrar productos por categoría
     * GET /api/products/category/{category}
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<Product>>> getByCategory(@PathVariable String category) {
        List<Product> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    /**
     * Actualizar stock de producto
     * PUT /api/products/{id}/stock
     */
    @PutMapping("/{id}/stock")
    public ResponseEntity<ApiResponse<Product>> updateStock(
            @PathVariable Long id,
            @RequestParam Integer stock) {
        
        productService.updateStock(id, stock);
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success("Stock actualizado", product));
    }

    /**
     * Actualizar precio de producto
     * PUT /api/products/{id}/price
     */
    @PutMapping("/{id}/price")
    public ResponseEntity<ApiResponse<Product>> updatePrice(
            @PathVariable Long id,
            @RequestParam BigDecimal price) {
        
        Product product = productService.updatePrice(id, price);
        return ResponseEntity.ok(ApiResponse.success("Precio actualizado", product));
    }
}
