package com.monolito.ecommerce.cart.service;

import com.monolito.ecommerce.cart.model.CartItem;
import com.monolito.ecommerce.product.model.Product;
import com.monolito.ecommerce.product.service.ProductService;
import com.monolito.ecommerce.shared.exception.BusinessException;
import com.monolito.ecommerce.user.service.UserService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servicio de Carrito de Compras
 * 
 * ALMACENAMIENTO EN MEMORIA:
 * - Map<userId, List<CartItem>>
 * - Cada usuario tiene su propia lista de items
 * 
 * COMUNICACIÓN ENTRE MÓDULOS EN EL MONOLITO:
 * - Llama directamente a ProductService y UserService
 * - NO hay HTTP ni serialización
 * - Latencia CERO: todo en la misma memoria
 * - Thread-safe con ConcurrentHashMap
 */
@Service
public class CartService {

    // Almacenamiento: Map<userId, List<CartItem>>
    private final Map<Long, List<CartItem>> cartDatabase = new ConcurrentHashMap<>();

    // Dependencias de otros módulos (inyección directa en memoria)
    private final ProductService productService;
    private final UserService userService;

    public CartService(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    /**
     * Agregar producto al carrito
     */
    public CartItem addToCart(Long userId, Long productId, Integer quantity) {
        // Validar que el usuario existe (comunicación interna)
        if (!userService.userExists(userId)) {
            throw new BusinessException("Usuario no encontrado");
        }

        // Validar que el producto existe y tiene stock
        Product product = productService.getProductById(productId);
        
        if (!productService.hasStock(productId, quantity)) {
            throw new BusinessException(
                String.format("Stock insuficiente para %s. Disponible: %d",
                    product.getName(), product.getStock())
            );
        }

        // Obtener o crear carrito del usuario
        List<CartItem> cart = cartDatabase.computeIfAbsent(userId, k -> new ArrayList<>());

        // Verificar si el producto ya está en el carrito
        CartItem existingItem = cart.stream()
            .filter(item -> item.getProductId().equals(productId))
            .findFirst()
            .orElse(null);

        if (existingItem != null) {
            // Incrementar cantidad
            Integer newQuantity = existingItem.getQuantity() + quantity;
            
            if (!productService.hasStock(productId, newQuantity)) {
                throw new BusinessException("Stock insuficiente para la cantidad solicitada");
            }
            
            existingItem.setQuantity(newQuantity);
            return existingItem;
        } else {
            // Agregar nuevo item
            CartItem newItem = new CartItem(
                productId,
                product.getName(),
                product.getPrice(),
                quantity
            );
            cart.add(newItem);
            return newItem;
        }
    }

    /**
     * Obtener carrito de un usuario
     */
    public List<CartItem> getCart(Long userId) {
        if (!userService.userExists(userId)) {
            throw new BusinessException("Usuario no encontrado");
        }

        return cartDatabase.getOrDefault(userId, new ArrayList<>());
    }

    /**
     * Calcular total del carrito
     */
    public BigDecimal calculateTotal(Long userId) {
        List<CartItem> cart = getCart(userId);
        
        return cart.stream()
            .map(CartItem::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Vaciar carrito (usado después de crear orden)
     */
    public void clearCart(Long userId) {
        cartDatabase.remove(userId);
    }

    /**
     * Eliminar un producto del carrito
     */
    public void removeFromCart(Long userId, Long productId) {
        List<CartItem> cart = getCart(userId);
        cart.removeIf(item -> item.getProductId().equals(productId));
    }

    /**
     * Obtener número de items en el carrito
     */
    public Integer getCartItemCount(Long userId) {
        List<CartItem> cart = getCart(userId);
        return cart.stream()
            .mapToInt(CartItem::getQuantity)
            .sum();
    }
}
