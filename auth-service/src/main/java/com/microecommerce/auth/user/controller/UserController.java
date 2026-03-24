package com.microecommerce.auth.user.controller;

import com.microecommerce.auth.shared.dto.ApiResponse;
import com.microecommerce.auth.user.model.LoginRequest;
import com.microecommerce.auth.user.model.RegisterRequest;
import com.microecommerce.auth.user.model.User;
import com.microecommerce.auth.user.model.UserResponse;
import com.microecommerce.auth.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@RequestBody RegisterRequest request) {
        User user = userService.registerUser(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                request.getFullName());

        UserResponse response = new UserResponse(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Usuario registrado exitosamente", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserResponse>> login(@RequestBody LoginRequest request) {
        User user = userService.login(request.getUsername(), request.getPassword());
        UserResponse response = new UserResponse(user);
        return ResponseEntity.ok(ApiResponse.success("Login exitoso", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers()
                .stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        UserResponse response = new UserResponse(user);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
