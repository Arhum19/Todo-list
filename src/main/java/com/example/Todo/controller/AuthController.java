package com.example.Todo.controller;

import com.example.Todo.dto.AuthResponse;
import com.example.Todo.dto.LoginRequest;
import com.example.Todo.dto.SignupRequest;
import com.example.Todo.model.User;
import com.example.Todo.repo.UserRepository;
import com.example.Todo.security.jwtService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final jwtService jwtService;
    @Value("${app.admin.secret}")
    private String adminSecret;

    public AuthController(UserRepository userRepository, 
                         PasswordEncoder passwordEncoder, 
                         jwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Username already exists");
        }

        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");

        User user = new User(
                null,
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                roles
        );

        User saved = userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User registered successfully");
    }

    @PostMapping("/admin/signup")
    public ResponseEntity<?> signupAdmin(@Valid @RequestBody SignupRequest request,
                                         @RequestParam(required = false) String adminSecret) {
        // Simple admin secret check - you can enhance this
        if (!this.adminSecret.equals(adminSecret)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Invalid admin secret");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Username already exists");
        }

        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");
        roles.add("ROLE_ADMIN");

        User user = new User(
                null,
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                roles
        );

        User saved = userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Admin user registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElse(null);

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }

        String token = jwtService.generateToken(user.getUsername());
        return ResponseEntity.ok(new AuthResponse(token, user.getUsername(), user.getId(), user.getRoles()));
    }

    @PostMapping("/admin/login")
    public ResponseEntity<?> loginAdmin(@Valid @RequestBody LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElse(null);

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }

        if (!user.getRoles().contains("ROLE_ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("User is not an admin");
        }

        String token = jwtService.generateToken(user.getUsername());
        return ResponseEntity.ok(new AuthResponse(token, user.getUsername(), user.getId(), user.getRoles()));
    }
}
