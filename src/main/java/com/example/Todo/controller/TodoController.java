package com.example.Todo.controller;

import com.example.Todo.dto.TodoRequest;
import com.example.Todo.dto.TodoResponse;
import com.example.Todo.dto.PagedTodoResponse;
import com.example.Todo.model.User;
import com.example.Todo.service.TodoService;
import com.example.Todo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "*")
public class TodoController {

    private final TodoService todoService;
    private final UserService userService;

    public TodoController(TodoService todoService, UserService userService) {
        this.todoService = todoService;
        this.userService = userService;
    }

    // One API for all filters
    @GetMapping
    public ResponseEntity<PagedTodoResponse> getTodos(
            @RequestParam(required = false) Boolean completed,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader("Authorization") String authHeader
    ) {
        User user = userService.getUserFromJwt(authHeader);
        return ResponseEntity.ok(todoService.getTodos(user, completed, keyword,page,size));
    }

    @PostMapping
    public ResponseEntity<TodoResponse> createTodo(@Valid @RequestBody TodoRequest req,
                                                   @RequestHeader("Authorization") String authHeader) {
        User user = userService.getUserFromJwt(authHeader);
        TodoResponse created = todoService.createTodo(user, req);
        return ResponseEntity.created(URI.create("/api/todos/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoResponse> updateTodo(@PathVariable Long id,
                                                   @Valid @RequestBody TodoRequest req,
                                                   @RequestHeader("Authorization") String authHeader) {
        User user = userService.getUserFromJwt(authHeader);
        return ResponseEntity.ok(todoService.updateTodo(user, id, req));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<TodoResponse> toggle(@PathVariable Long id,
                                               @RequestParam boolean completed,
                                               @RequestHeader("Authorization") String authHeader) {
        User user = userService.getUserFromJwt(authHeader);
        return ResponseEntity.ok(todoService.toggleComplete(user, id, completed));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id,
                                           @RequestHeader("Authorization") String authHeader) {
        User user = userService.getUserFromJwt(authHeader);
        todoService.deleteTodo(user, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<Void> deleteAll(@RequestHeader("Authorization") String authHeader) {
        User user = userService.getUserFromJwt(authHeader);
        todoService.deleteAllTodos(user);
        return ResponseEntity.noContent().build();
    }
}
