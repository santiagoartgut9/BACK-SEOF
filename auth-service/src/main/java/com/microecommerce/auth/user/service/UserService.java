package com.microecommerce.auth.user.service;

import com.microecommerce.auth.shared.exception.BusinessException;
import com.microecommerce.auth.shared.exception.ResourceNotFoundException;
import com.microecommerce.auth.user.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {

    private final Map<Long, User> userDatabase = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public User registerUser(String username, String email, String password, String fullName) {
        boolean usernameExists = userDatabase.values().stream()
                .anyMatch(u -> u.getUsername().equalsIgnoreCase(username));

        if (usernameExists) {
            throw new BusinessException("El nombre de usuario ya existe: " + username);
        }

        boolean emailExists = userDatabase.values().stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(email));

        if (emailExists) {
            throw new BusinessException("El email ya está registrado: " + email);
        }

        Long userId = idGenerator.getAndIncrement();
        User user = new User(userId, username, email, password, fullName);
        userDatabase.put(userId, user);
        return user;
    }

    public User login(String username, String password) {
        User user = userDatabase.values().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new BusinessException("Credenciales inválidas"));

        if (!user.getPassword().equals(password)) {
            throw new BusinessException("Credenciales inválidas");
        }

        return user;
    }

    public User getUserById(Long userId) {
        User user = userDatabase.get(userId);
        if (user == null) {
            throw new ResourceNotFoundException("Usuario", userId);
        }
        return user;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(userDatabase.values());
    }
}
