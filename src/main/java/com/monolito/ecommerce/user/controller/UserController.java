package com.monolito.ecommerce.user.controller;

import com.monolito.ecommerce.shared.dto.ApiResponse;
import com.monolito.ecommerce.user.model.LoginRequest;
import com.monolito.ecommerce.user.model.RegisterRequest;
import com.monolito.ecommerce.user.model.User;
import com.monolito.ecommerce.user.model.UserResponse;
import com.monolito.ecommerce.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST de Usuarios
 * 
 * ENDPOINTS:
 * POST   /api/users/register - Registrar nuevo usuario
 * POST   /api/users/login    - Login de usuario
 * GET    /api/users          - Listar todos los usuarios
 * GET    /api/users/{id}     - Obtener usuario por ID
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registrar un nuevo usuario
     * POST /api/users/register
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@RequestBody RegisterRequest request) {
        User user = userService.registerUser(
            request.getUsername(),
            request.getEmail(),
            request.getPassword(),
            request.getFullName()
        );

        UserResponse response = new UserResponse(user);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success("Usuario registrado exitosamente", response));
    }

    /**
     * Login de usuario
     * POST /api/users/login
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponse>> login(@RequestBody LoginRequest request) {
        User user = userService.login(request.getUsername(), request.getPassword());
        UserResponse response = new UserResponse(user);
        return ResponseEntity.ok(ApiResponse.success("Login exitoso", response));
    }

    /**
     * Listar todos los usuarios
     * GET /api/users
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers()
            .stream()
            .map(UserResponse::new)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    /**
     * Obtener usuario por ID
     * GET /api/users/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        UserResponse response = new UserResponse(user);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
