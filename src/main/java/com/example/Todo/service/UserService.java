package com.example.Todo.service;

import com.example.Todo.model.User;
import com.example.Todo.repo.UserRepository;
import com.example.Todo.security.jwtService;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repo;
    private final jwtService jwtService;

    public UserService(UserRepository repo, jwtService jwtService) {
        this.repo = repo;
        this.jwtService = jwtService;
    }

    public User getUserFromJwt(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid authorization header");
        }

        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);

        return repo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
