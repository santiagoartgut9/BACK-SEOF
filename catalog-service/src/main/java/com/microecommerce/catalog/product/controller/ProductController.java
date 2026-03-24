package com.microecommerce.catalog.product.controller;

import com.microecommerce.catalog.product.model.Product;
import com.microecommerce.catalog.product.model.ProductRequest;
import com.microecommerce.catalog.product.service.ProductService;
import com.microecommerce.catalog.shared.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Product>> createProduct(@RequestBody ProductRequest request) {
        Product product = productService.createProduct(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getStock(),
                request.getCategory());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Producto creado exitosamente", product));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success(product));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<Product>>> getByCategory(@PathVariable String category) {
        List<Product> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(ApiResponse.success(products));
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<ApiResponse<Product>> updateStock(
            @PathVariable Long id,
            @RequestParam Integer stock) {

        productService.updateStock(id, stock);
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success("Stock actualizado", product));
    }

    @PutMapping("/{id}/price")
    public ResponseEntity<ApiResponse<Product>> updatePrice(
            @PathVariable Long id,
            @RequestParam BigDecimal price) {

        Product product = productService.updatePrice(id, price);
        return ResponseEntity.ok(ApiResponse.success("Precio actualizado", product));
    }
}
