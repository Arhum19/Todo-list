package com.example.Todo.controller;

import  org.springframework.http.ResponseEntity;
import  org.springframework.web.bind.annotation.*;


import  com.example.Todo.dto.TodoResponse;
import  com.example.Todo.dto.TodoRequest;
import  com.example.Todo.service.TodoService;
import  jakarta.validation.Valid;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "*")
public class TodoController {

    private final TodoService tsv;

    public TodoController(TodoService tsv) {
        this.tsv = tsv;
    }

    @GetMapping
    public ResponseEntity<List<TodoResponse>> getAllTodos() {
        return ResponseEntity.ok(tsv.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoResponse> getTodoById(@PathVariable Long id) {
        return ResponseEntity.ok(tsv.findById(id));
    }

    @PostMapping
    public ResponseEntity<TodoResponse> createTodo(@Valid @RequestBody TodoRequest req) {
        TodoResponse createdTodo = tsv.createTodo(req);
        return ResponseEntity.created(URI.create("/api/todos/" + createdTodo.getId())).body(createdTodo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoResponse> updateTodo(@PathVariable Long id, @Valid @RequestBody TodoRequest req) {
        return ResponseEntity.ok(tsv.updateTodo(id, req));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<TodoResponse> toggle(@PathVariable Long id, @RequestParam boolean completed) {
        return ResponseEntity.ok(tsv.toggleComplete(id, completed));
    }
    @GetMapping("/completed")
    public ResponseEntity<List<TodoResponse>> getCompletedTodos() {
        return ResponseEntity.ok(tsv.findByCompleted(true));
    }

    // New API: Get all pending todos
    @GetMapping("/pending")
    public ResponseEntity<List<TodoResponse>> getPendingTodos() {
        return ResponseEntity.ok(tsv.findByCompleted(false));
    }

    // New API: Search todos by title keyword
    @GetMapping("/search")
    public ResponseEntity<List<TodoResponse>> searchTodos(@RequestParam String keyword) {
        return ResponseEntity.ok(tsv.searchByTitle(keyword));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<TodoResponse> deleteTodo(@PathVariable Long id) {
        tsv.delete(id);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/deleteAll")
    public ResponseEntity<TodoResponse> deleteAllTodos() {
        tsv.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
