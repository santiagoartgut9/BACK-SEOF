package com.microecommerce.catalog.product.service;

import com.microecommerce.catalog.product.model.Product;
import com.microecommerce.catalog.shared.exception.BusinessException;
import com.microecommerce.catalog.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final Map<Long, Product> productDatabase = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Product createProduct(String name, String description, BigDecimal price, Integer stock, String category) {
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("El precio debe ser mayor a cero");
        }

        if (stock < 0) {
            throw new BusinessException("El stock no puede ser negativo");
        }

        Long productId = idGenerator.getAndIncrement();
        Product product = new Product(productId, name, description, price, stock, category);
        productDatabase.put(productId, product);
        return product;
    }

    public Product getProductById(Long productId) {
        Product product = productDatabase.get(productId);
        if (product == null) {
            throw new ResourceNotFoundException("Producto", productId);
        }
        return product;
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(productDatabase.values());
    }

    public List<Product> getProductsByCategory(String category) {
        return productDatabase.values().stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public synchronized void updateStock(Long productId, Integer newStock) {
        Product product = getProductById(productId);

        if (newStock < 0) {
            throw new BusinessException("El stock no puede ser negativo");
        }

        product.setStock(newStock);
    }

    public Product updatePrice(Long productId, BigDecimal newPrice) {
        Product product = getProductById(productId);

        if (newPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("El precio debe ser mayor a cero");
        }

        product.setPrice(newPrice);
        return product;
    }
}
