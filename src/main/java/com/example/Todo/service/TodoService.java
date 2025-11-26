package com.example.Todo.service;
import com.example.Todo.repo.TodoRepository;
import org.springframework.stereotype.Service;

import com.example.Todo.model.todo;
import  com.example.Todo.exception.NotFoundException;
import com.example.Todo.mapper.TodoMapper;
import com.example.Todo.dto.TodoRequest;
import com.example.Todo.dto.TodoResponse;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class TodoService {
    private final TodoRepository repo;

    public TodoService(TodoRepository repo) {
        this.repo = repo;
    }

    public List<TodoResponse> findAll() {
        return repo.findAll().stream()
                .map(TodoMapper::toResponse)
                .collect(Collectors.toList());
    }

    public TodoResponse findById(Long id) {
        todo t = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Todo not found with id: " + id));
        return TodoMapper.toResponse(t);
    }

    public TodoResponse createTodo(TodoRequest req) {
        todo t = TodoMapper.toEntity(req, null);
        t.setCreatedAt(Instant.now());
        t.setUpdatedAt(Instant.now());
        todo savedTodo = repo.save(t);
        return TodoMapper.toResponse(savedTodo);
    }

    public TodoResponse updateTodo(Long id, TodoRequest req) {
        todo existingTodo = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Todo not found with id: " + id));
        existingTodo.setTitle(req.getTitle());
        existingTodo.setDescription(req.getDescription());
        existingTodo.setCompleted(req.isCompleted());
        existingTodo.setUpdatedAt(java.time.Instant.now());
        todo updatedTodo = repo.save(existingTodo);
        return TodoMapper.toResponse(updatedTodo);

    }
    public TodoResponse toggleComplete(Long id, boolean completed){
        todo existing = repo.findById(id).orElseThrow(() -> new NotFoundException("Todo not found"));
        existing.setCompleted(completed);
        existing.setUpdatedAt(java.time.Instant.now());
        repo.save(existing);
        return TodoMapper.toResponse(existing);
    }

    public List<TodoResponse> findByCompleted(boolean completed) {
        return repo.findByCompleted(completed)
                .stream()
                .map(TodoMapper::toResponse)
                .collect(Collectors.toList());
    }


    public List<TodoResponse> searchByTitle(String keyword) {
        return repo.findByTitleContainingIgnoreCase(keyword)
                .stream()
                .map(TodoMapper::toResponse)
                .collect(Collectors.toList());
    }
    public  void delete(Long id){
        if(!repo.existsById(id)) throw new NotFoundException("Todo not Found");
        repo.deleteById(id);
    }

    public void deleteAll() {
        repo.deleteAll();
    }
}
