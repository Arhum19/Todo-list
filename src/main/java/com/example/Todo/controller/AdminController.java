package com.example.Todo.controller;

import com.example.Todo.dto.TodoResponse;
import com.example.Todo.dto.UserResponse;
import com.example.Todo.model.User;
import com.example.Todo.model.todo;
import com.example.Todo.repo.TodoRepository;
import com.example.Todo.repo.UserRepository;
import com.example.Todo.mapper.TodoMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final UserRepository userRepository;
    private final TodoRepository todoRepository;

    public AdminController(UserRepository userRepository, TodoRepository todoRepository) {
        this.userRepository = userRepository;
        this.todoRepository = todoRepository;
    }

    // Get all users (Admin only)
    @GetMapping("/users")
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<Map<String, Object>> userList = users.stream()
                .filter(user -> user.getRoles() == null || user.getRoles().stream()
                        .map(Object::toString)
                        .noneMatch(s -> s.toUpperCase().contains("ADMIN")))
                .map(user -> {
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("id", user.getId());
                    userMap.put("username", user.getUsername());
                    userMap.put("roles", user.getRoles());
                    return userMap;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(userList);
    }

    // Get all todos from all users (Admin only)
    @GetMapping("/todos")
    public ResponseEntity<List<TodoResponse>> getAllTodos() {
        List<todo> todos = todoRepository.findAll();
        List<TodoResponse> responses = todos.stream()
                .map(TodoMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    // Get todos by specific user (Admin only)
    @GetMapping("/users/{userId}/todos")
    public ResponseEntity<List<TodoResponse>> getUserTodos(@PathVariable Long userId) {
        List<todo> todos = todoRepository.findByUserId(userId);
        List<TodoResponse> responses = todos.stream()
                .map(TodoMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    // Delete any user (Admin only)
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Delete all todos of this user first
        List<todo> userTodos = todoRepository.findByUserId(userId);
        todoRepository.deleteAll(userTodos);
        
        // Delete the user
        userRepository.delete(user);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "User and all their todos deleted successfully");
        return ResponseEntity.ok(response);
    }

    // Delete any todo (Admin only)
    @DeleteMapping("/todos/{todoId}")
    public ResponseEntity<Map<String, String>> deleteTodo(@PathVariable Long todoId) {
        todo t = todoRepository.findById(todoId)
                .orElseThrow(() -> new RuntimeException("Todo not found"));
        
        todoRepository.delete(t);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Todo deleted successfully");
        return ResponseEntity.ok(response);
    }

    // Get statistics (Admin only)
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        long totalUsers = userRepository.count();
        long totalTodos = todoRepository.count();
        long completedTodos = todoRepository.findByCompleted(true).size();
        long pendingTodos = todoRepository.findByCompleted(false).size();
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", totalUsers);
        stats.put("totalTodos", totalTodos);
        stats.put("completedTodos", completedTodos);
        stats.put("pendingTodos", pendingTodos);
        
        return ResponseEntity.ok(stats);
    }
}
