package com.example.Todo.service;
import com.example.Todo.dto.PagedTodoResponse;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import com.example.Todo.dto.TodoRequest;
import com.example.Todo.dto.TodoResponse;
import com.example.Todo.exception.NotFoundException;
import com.example.Todo.mapper.TodoMapper;
import com.example.Todo.model.todo;
import com.example.Todo.model.User;
import com.example.Todo.repo.TodoRepository;
import com.example.Todo.repo.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoService {

    private final TodoRepository todoRepo;
    private final UserRepository userRepo;

    public TodoService(TodoRepository todoRepo, UserRepository userRepo) {
        this.todoRepo = todoRepo;
        this.userRepo = userRepo;
    }

    // Fetch todos with optional filters (completed, pending, search) + pagination
    public PagedTodoResponse getTodos(User user, Boolean completed, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<todo> todoPage;

        if (keyword != null && !keyword.isEmpty()) {
            // search by title for this user
            todoPage = todoRepo.findByUserAndTitleContainingIgnoreCase(user, keyword, pageable);
        } else if (completed != null) {
            // filter by completed for this user
            todoPage = todoRepo.findByUserAndCompleted(user, completed, pageable);
        } else {
            // all todos for this user
            todoPage = todoRepo.findByUser(user, pageable);
        }

        List<TodoResponse> content = todoPage.getContent()
                .stream()
                .map(TodoMapper::toResponse)
                .collect(Collectors.toList());

        return new PagedTodoResponse(
                content,
                todoPage.getNumber(),
                todoPage.getSize(),
                todoPage.getTotalElements(),
                todoPage.getTotalPages()
        );
    }

    public TodoResponse createTodo(User user, TodoRequest req) {
        todo t = TodoMapper.toEntity(req, null, user);
        todo saved = todoRepo.save(t);
        return TodoMapper.toResponse(saved);
    }

    public TodoResponse updateTodo(User user, Long id, TodoRequest req) {
        todo existing = todoRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Todo not found"));

        if (!existing.getUser().getId().equals(user.getId())) {
            throw new NotFoundException("Todo not found for this user");
        }

        existing.setTitle(req.getTitle());
        existing.setDescription(req.getDescription());
        existing.setCompleted(req.isCompleted());
        existing.setUpdatedAt(Instant.now());

        return TodoMapper.toResponse(todoRepo.save(existing));
    }

    public void deleteTodo(User user, Long id) {
        todo existing = todoRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Todo not found"));
        if (!existing.getUser().getId().equals(user.getId())) {
            throw new NotFoundException("Todo not found for this user");
        }
        todoRepo.delete(existing);
    }

    public void deleteAllTodos(User user) {
        todoRepo.deleteAll(todoRepo.findByUser(user));
    }

    public TodoResponse toggleComplete(User user, Long id, boolean completed) {
        todo existing = todoRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Todo not found"));
        if (!existing.getUser().getId().equals(user.getId())) {
            throw new NotFoundException("Todo not found for this user");
        }
        existing.setCompleted(completed);
        existing.setUpdatedAt(Instant.now());
        return TodoMapper.toResponse(todoRepo.save(existing));
    }
}
